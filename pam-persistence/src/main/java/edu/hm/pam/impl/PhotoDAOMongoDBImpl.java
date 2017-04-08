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
        gridFSInputFile.setFilename(photo.getTitle());
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
                    System.out.println("Photo inserted");
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
        Photo photo = new Photo();
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

            if (referencesInFoundenPhotoAlbum == null || referencesInFoundenPhotoAlbum.isEmpty()) {
                return photoList;
            } else {

                for (DBRef dbRef : referencesInFoundenPhotoAlbum) {
                    GridFSDBFile imageForOutput = gridFS.findOne((ObjectId) dbRef.getId());

                    System.out.println("Foundedn GridFSDBFile with fileName: " + imageForOutput.getFilename());

                    MultipartFile multiPartFile = createMultiPartFile(imageForOutput);

                    photo.setTitle(multiPartFile.getOriginalFilename());

                    photo.setMultipartFile(multiPartFile);

                    photoList.add(photo);

                    System.out.println("Photo '" + photo.getTitle() +  "' is added to photolist for present");
                }
            }
        } catch (NullPointerException npe) {
            logger.error(npe.getMessage(), npe);
        }
        return photoList;
    }

    private MultipartFile createMultiPartFile(GridFSDBFile gridFSDBFile) throws IOException {

        String fileName = gridFSDBFile.getFilename();
        String originalFileName = gridFSDBFile.getFilename();
        String contentType = gridFSDBFile.getContentType();

        MultipartFile multipartFile = new MockMultipartFile(fileName, originalFileName, contentType, gridFSDBFile.getInputStream());
        return multipartFile;
    }

    @Override
    public boolean deletePhotoByUserNameAndPhotoAlbumTitle(String userName, String albumTitle, Photo photo) {

        boolean status = false;

        while (gridFS.findOne(photo.getTitle()) != null) {
            gridFS.remove(gridFS.findOne(photo.getTitle()));
            status = true;
        }
        return status;
    }

    public static void main(String[] args) throws IOException {

        PhotoDAOMongoDBImpl photoDAOMongoDB = new PhotoDAOMongoDBImpl();

        File file = new File("/Users/vlfa/Dropbox/velo.jpeg");

        // String name = "velo";
        // String originalFileName = "velo.jpeg";
        // String contentType = "image/jpeg";
        // byte[] content = null;
        //
        // BufferedImage bufferedImage = ImageIO.read(file);
        //
        // // get DataBufferBytes from Raster
        // WritableRaster raster = bufferedImage .getRaster();
        // DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
        //
        // content = data.getData();
        //
        // MultipartFile multipartFile = new MockMultipartFile(name,
        //         originalFileName, contentType, content);

        Photo photo = new Photo();
        photo.setTitle("ba");
        photo.setFile(file);
        // photo.setMultipartFile(multipartFile);

        // photo.setPhotoAlben(photoAlbumList);
        // boolean status = photoDAOMongoDB.savePhoto(photo);
        // boolean status = photoDAOMongoDB.deletePhoto(photo);
        // Photo status = photoDAOMongoDB.findPhoto(photo);
        // Photo status = photoDAOMongoDB.updatePhoto(photo);
        // System.out.print(photoDAOMongoDB.findAllPhotos());
        // System.out.println(photoDAOMongoDB.findAllPhotosByUserNameAndPhotoAlbumTitle("vfaerman", "album"));
        // System.out.println(photoDAOMongoDB.savePhotoByAlbumTitleOfUser("vfaerman", "album", photo));
        // System.out.println(photoDAOMongoDB.deletePhotoByUserNameAndPhotoAlbumTitle("Test", "admin", photo));
        System.out.println(photoDAOMongoDB.findAllPhotosByUserNameAndPhotoAlbumTitle("asd", "album"));
        // photoDAOMongoDB.getPhotoAlbum("Faerman", "album");
        // System.out.println(status);
    }
}