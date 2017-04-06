package edu.hm.pam.impl;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBRef;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import edu.hm.pam.PhotoDAO;
import edu.hm.pam.entity.Photo;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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
    private GridFS gfsPhoto = new GridFS(mongo.getDB("qwertz"), "photos");

    @Override
    public boolean savePhotoByAlbumTitleOfUser(String userName, String albumTitle, Photo photo) throws IOException {

        //  convert a MultipartFile to File
        // File file = new File(photo.getMultipartFile().getOriginalFilename());
        // photo.getMultipartFile().transferTo(file);

        GridFSInputFile inputFile = gfsPhoto.createFile(photo.getFile());
        inputFile.setFilename(photo.getTitle());
        inputFile.save();

        System.out.println(gfsPhoto.find(photo.getTitle()));

        DBRef ref = new DBRef("photos", photo.getTitle());

        // GridFSBucket gridFSBucket = GridFSBuckets.create(db, "photos");
        //
        // ObjectId fileId = null;
        // try {
        //     InputStream streamToUploadFrom = photo.getMultipartFile().getInputStream();
        //     GridFSUploadOptions options = new GridFSUploadOptions()
        //             .metadata(new Document("contentType", "image/jpeg"));
        //     fileId = gridFSBucket.uploadFromStream(photo.getTitle(), streamToUploadFrom, options);
        // } catch (FileNotFoundException fne){
        //     logger.error("Exception: " + fne.getMessage());
        // }
        //
        // GridFSFile out = gridFSBucket.find( new BasicDBObject( "_id" , photo.getTitle() ) ).first();
        // //Save loaded image from database into new image file
        // FileOutputStream outputImage = new FileOutputStream("../");
        // System.out.println(out.getFilename());
        // outputImage.close();

        //     GridFS gridFS = new GridFS(mongo.getDB("qwertz"));

        // GridFSInputFile gridFSInputFile1 = gridFS.createFile(photo.getMultipartFile().getBytes());
        // System.out.println("PhotoDAOMongoDBImpl: " + photo.getMultipartFile().getOriginalFilename());
        // System.out.println(gridFSInputFile1.getOutputStream());
        // GridFSInputFile gridFSInputFile = null;
        //
        // try {
        //     InputStream inputStream = photo.getMultipartFile().getInputStream();
        //     gridFSInputFile = gridFS.createFile(inputStream);
        //     gridFSInputFile.setFilename(photo.getMultipartFile().getOriginalFilename());
        //     gridFSInputFile.setContentType(photo.getMultipartFile().getContentType());
        //     // gridFSInputFile.saveChunks();
        //     // gridFSInputFile.save();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        boolean status;

        BasicDBObject query = new BasicDBObject();
        query.put("_id", userName);
        query.put("photoAlbumList.albumTitle", albumTitle);

        BasicDBObject data = new BasicDBObject();
        data.put("photoAlbumList.$.photoList", ref);

        BasicDBObject command = new BasicDBObject();
        command.put("$push", data);

        try {
            Document foundUser = collection.find(eq("_id", userName)).first();
            Document foundUserWithPhotoAlbum = collection.find(
                    and(
                            eq("_id", userName),
                            eq("photoAlbumList.albumTitle", albumTitle))).first();
            if (foundUser != null) {
                if (foundUserWithPhotoAlbum != null && albumTitle != null) {
                    collection.updateOne(query, command);
                    status = true;
                    System.out.println("Photo inserted");
                } else {
                    status = false;
                }
            } else {
                status = false;
            }
        } catch (MongoWriteException mwe) {
            logger.error(mwe.getMessage(), mwe);
            status = false;
        }
        return status;
    }

    @Override
    public List<Photo> findAllPhotosByUserNameAndPhotoAlbumTitle(String userName, String albumTitle) {

        List<Photo> photoList = new ArrayList<>();
        Document documentForFoundedPhotoAlbum = null;

        try {
            Document foundUser = collection.find(new Document("_id", userName)).first();
            List<Document> embeddedPhotoAlbenOfUser = (List<Document>) foundUser.get("photoAlbumList");

            for (Document searchDocumentByAlbumTitle : embeddedPhotoAlbenOfUser) {
                if (searchDocumentByAlbumTitle.containsValue(albumTitle)) {
                    documentForFoundedPhotoAlbum = searchDocumentByAlbumTitle;
                    System.out.println("AlbumTitle found");
                    break;
                }
            }

            List<DBRef> referencesInFoundenPhotoAlbum = (List<DBRef>) documentForFoundedPhotoAlbum.get("photoList");
            List<GridFSDBFile> photosGridFSDBFiles = new ArrayList<>();

            for (DBRef dbRef : referencesInFoundenPhotoAlbum) {

                GridFSDBFile imageForOutput = gfsPhoto.findOne((String) dbRef.getId());
                photosGridFSDBFiles.add(imageForOutput);
                System.out.println(imageForOutput);
            }
        } catch (NullPointerException npe) {
            logger.error(npe.getMessage(), npe);
        }
        return photoList;
    }

    @Override
    public boolean deletePhotoByUserNameAndPhotoAlbumTitle(String userName, String albumTitle, Photo photo) {
        boolean status;

        Gson gson = new Gson();
        String str_representation = gson.toJson(photo);
        Document doc = Document.parse(str_representation);

        BasicDBObject query = new BasicDBObject();
        query.put("_id", userName);
        query.put("photoAlbumList.albumTitle", albumTitle);

        BasicDBObject data = new BasicDBObject();
        data.put("photoAlbumList.$.photoList", doc);

        BasicDBObject command = new BasicDBObject();
        command.put("$pull", data);

        try {
            Document foundUser = collection.find(eq("_id", userName)).first();
            Document foundPhotoAlbumByUser = collection.find(
                    and(
                            eq("_id", userName),
                            eq("photoAlbumList.albumTitle", albumTitle))).first();
            if (foundUser != null) {
                if (foundPhotoAlbumByUser != null) {
                    UpdateResult updateResult = collection.updateOne(query, command);
                    // TODO: ueberpruefen, ob Document erfolgreich aktualisiert ist...
                    if (updateResult.wasAcknowledged()) {
                        status = true;
                    } else {
                        status = false;
                    }
                } else {
                    status = false;
                }
            } else {
                status = false;
            }
        } catch (NullPointerException npe) {
            status = false;
            logger.error(npe.getMessage(), npe);
        }
        return status;
    }

    public static void main(String[] args) throws IOException {

        PhotoDAOMongoDBImpl photoDAOMongoDB = new PhotoDAOMongoDBImpl();

        File file = new File("/Users/vlfa/Dropbox/velo.jpeg");

        Photo photo = new Photo();
        photo.setTitle("velo");
        photo.setFile(file);

        // photo.setPhotoAlben(photoAlbumList);
        // boolean status = photoDAOMongoDB.savePhoto(photo);
        // boolean status = photoDAOMongoDB.deletePhoto(photo);
        // Photo status = photoDAOMongoDB.findPhoto(photo);
        // Photo status = photoDAOMongoDB.updatePhoto(photo);
        // System.out.print(photoDAOMongoDB.findAllPhotos());
        System.out.println(photoDAOMongoDB.findAllPhotosByUserNameAndPhotoAlbumTitle("vfaerman", "album"));
        // System.out.println(photoDAOMongoDB.createPhotoByAlbumTitleOfUser("vfaerman", "album", photo));
        // System.out.println(photoDAOMongoDB.deletePhotoByUserNameAndPhotoAlbumTitle("Test", "newAlbum1", photo));
        // System.out.println(photoDAOMongoDB.findAllPhotosByUserNameAndPhotoAlbumTitle("test", "album"));
        // photoDAOMongoDB.getPhotoAlbum("Faerman", "album");
        // System.out.println(status);
    }
}