package edu.hm.pam.impl;

import com.mongodb.DBRef;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import edu.hm.pam.PhotoDAO;
import edu.hm.pam.entity.Photo;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Created by vlfa on 16.03.17.
 */
@Service
public class PhotoDAOMongoDBImpl implements PhotoDAO {

    static final Logger logger = LoggerFactory.getLogger(PhotoDAOMongoDBImpl.class);

    // TODO: 22.03.17 to insert a MongoConnection in an exclude class
    private MongoClient mongo = new MongoClient("localhost", 27017);
    private MongoDatabase db = mongo.getDatabase("qwertz");
    private MongoCollection<Document> collection = db.getCollection("users");
    private GridFS gridFS = new GridFS(mongo.getDB("qwertz"), "photos");

    @Override
    public boolean savePhotoByAlbumTitleOfUser(String userName, String albumTitle, Photo photo) throws IOException {

        boolean status;

        GridFSInputFile gridFSInputFile = gridFS.createFile(photo.getMultipartFile().getInputStream());

        String extensionRemoved = photo.getMultipartFile().getOriginalFilename().split("\\.")[0];

        gridFSInputFile.setFilename(extensionRemoved);
        gridFSInputFile.setContentType(photo.getMultipartFile().getContentType());
        gridFSInputFile.saveChunks();
        gridFSInputFile.save();

        Document query = new Document();
        query.put("_id", userName);
        query.put("photoAlbumList.albumTitle", albumTitle);

        Document data = new Document();
        data.put("photoAlbumList.$.photoList", new DBRef("qwertz", "photos", gridFSInputFile.getId()));

        Document command = new Document();
        command.put("$push", data);

        try {
            Document foundUser = collection.find(eq("_id", userName)).first();
            Document foundUserWithPhotoAlbum = collection.find(
                    and(
                            eq("_id", userName),
                            eq("photoAlbumList.albumTitle", albumTitle))).first();
            if (foundUser != null) {
                if (foundUserWithPhotoAlbum != null) {
                    collection.updateOne(query, command);
                    status = true;
                } else {
                    System.out.println("Photoalbum didn't find");
                    status = false;
                }
            } else {
                System.out.println("User didn't find");
                status = false;
            }
        } catch (MongoWriteException mwe) {
            logger.error(mwe.getMessage(), mwe);
            status = false;
        }
        return status;
    }

    @Override
    public List<Photo> findAllPhotosByUserNameAndPhotoAlbumTitle(String userName, String albumTitle) throws IOException {

        List<Photo> photoList = new ArrayList<>();
        Document documentForFoundedPhotoAlbum = null;
        Photo photo;

        try {
            Document foundUser = collection.find(new Document("_id", userName)).first();
            List<Document> embeddedPhotoAlbenOfUser = (List<Document>) foundUser.get("photoAlbumList");

            for (Document searchDocumentByAlbumTitle : embeddedPhotoAlbenOfUser) {
                if (searchDocumentByAlbumTitle.containsValue(albumTitle)) {
                    documentForFoundedPhotoAlbum = searchDocumentByAlbumTitle;
                    break;
                }
            }
            List<DBRef> referencesInFoundedPhotoAlbum = (List<DBRef>) documentForFoundedPhotoAlbum.get("photoList");

            if (referencesInFoundedPhotoAlbum == null || referencesInFoundedPhotoAlbum.isEmpty()) {
                return photoList;
            } else {

                for (DBRef dbRef : referencesInFoundedPhotoAlbum) {
                    GridFSDBFile imageForOutput = gridFS.findOne((ObjectId) dbRef.getId());
                    photo = new Photo();
                    if (imageForOutput != null) {
                        MultipartFile multiPartFile = createMultiPartFile(imageForOutput);
                        photo.setId(imageForOutput.getId().toString());
                        photo.setMultipartFile(multiPartFile);
                        photoList.add(photo);
                    } else {
                        return photoList;
                    }
                }
            }
        } catch (NullPointerException npe) {
            logger.error(npe.getMessage(), npe);
        }
        return photoList;
    }

    @Override
    public boolean deletePhotoByUserNameAndPhotoAlbumTitle(String userName, String albumTitle, String id) {

        boolean status = false;

        if (this.findAndDeletePhotoByPhotoTitleAndReferenceOfPhoto(userName, albumTitle, id)) {
            gridFS.remove(gridFS.findOne(new ObjectId(id)));
            status = true;
        }
        return status;
    }

    private boolean findAndDeletePhotoByPhotoTitleAndReferenceOfPhoto(String userName, String albumTitle, String id) {
        boolean status = false;

        Document documentForFoundedPhotoAlbum = null;
        List<DBRef> dbRefList;

        try {
            Document foundUser = collection.find(new Document("_id", userName)).first();
            List<Document> embeddedPhotoAlbenOfUser = (List<Document>) foundUser.get("photoAlbumList");

            for (Document searchDocumentByAlbumTitle : embeddedPhotoAlbenOfUser) {
                if (searchDocumentByAlbumTitle.containsValue(albumTitle)) {
                    documentForFoundedPhotoAlbum = searchDocumentByAlbumTitle;
                    break;
                }
            }
            List<DBRef> referencesInFoundedPhotoAlbum = (List<DBRef>) documentForFoundedPhotoAlbum.get("photoList");
            dbRefList = new ArrayList<>(referencesInFoundedPhotoAlbum);

            if (referencesInFoundedPhotoAlbum == null || referencesInFoundedPhotoAlbum.isEmpty()) {
                status = false;
            } else {
                for (DBRef dbRef : referencesInFoundedPhotoAlbum) {
                    if (dbRef.getId().toString().contains(id)) {
                        dbRefList.remove(dbRef);
                    }
                }
                Document query = new Document();
                query.put("_id", userName);
                query.put("photoAlbumList.albumTitle", albumTitle);

                // pullAll delete all elements
                Document data = new Document();
                data.put("photoAlbumList.$.photoList", referencesInFoundedPhotoAlbum);

                Document command = new Document();
                command.put("$pullAll", data);

                collection.updateOne(query, command);

                // pushAll added new elements
                Document newData = new Document();
                newData.put("photoAlbumList.$.photoList", dbRefList);

                Document newCommand = new Document();
                newCommand.put("$pushAll", newData);

                collection.updateOne(query, newCommand);

                status = true;
            }
        } catch (NullPointerException npe) {
            logger.error(npe.getMessage(), npe);
        }
        return status;
    }

    private MultipartFile createMultiPartFile(GridFSDBFile gridFSDBFile) throws IOException {

        String fileName = gridFSDBFile.getFilename();
        String originalFileName = gridFSDBFile.getFilename();
        String contentType = gridFSDBFile.getContentType();

        MultipartFile multipartFile = new MockMultipartFile(fileName, originalFileName, contentType, gridFSDBFile.getInputStream());
        return multipartFile;
    }
}