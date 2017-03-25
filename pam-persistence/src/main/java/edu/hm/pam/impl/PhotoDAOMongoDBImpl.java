package edu.hm.pam.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.hm.pam.PhotoDAO;
import edu.hm.pam.entity.Photo;
import edu.hm.pam.entity.PhotoAlbum;
import edu.hm.pam.entity.User;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vlfa on 16.03.17.
 */
@Service
public class PhotoDAOMongoDBImpl implements PhotoDAO {

    static final Logger logger = LoggerFactory.getLogger(PhotoDAOMongoDBImpl.class);

    // // TODO: 22.03.17 to insert a MongoConnection in an exclude class
    private MongoClient mongo = new MongoClient("localhost", 27017);
    private MongoDatabase db = mongo.getDatabase("qwertz");
    private MongoCollection<Document> collection = db.getCollection("users");

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
            List<Document> embeddedPhotosOfPhotoAlbum = (List<Document>) documentForFoundedPhotoAlbum.get("photoList");

            for (Document documentForPhoto : embeddedPhotosOfPhotoAlbum) {
                String toJon = documentForPhoto.toJson();
                Photo photo = new Gson().fromJson(toJon, Photo.class);
                System.out.println(photo.getTitle());
                photoList.add(photo);
            }

        } catch (NullPointerException npe) {
            logger.error(npe.getMessage(), npe);
        }
        return photoList;
    }

    @Override
    public boolean savePhoto(Photo photo) {
        boolean status;
        Gson gson = new Gson();
        String str_representation = gson.toJson(photo);

        try {
            Document doc = Document.parse(str_representation).append("_id", photo.getTitle());
            collection.insertOne(doc);
            status = true;
        } catch (MongoWriteException mwe) {
            status = false;
            logger.error(mwe.getMessage(), mwe);
        }
        return status;
    }

    @Override
    public Photo findPhoto(Photo photo) {
       Photo foundPhoto;

        try {
            Document document = collection.find(new Document("_id", photo.getTitle())).first();
            String toJson = document.toJson();
            foundPhoto = new Gson().fromJson(toJson, Photo.class);
        } catch (NullPointerException npe) {
            foundPhoto = null;
            logger.error(npe.getMessage(), npe);
        } catch (JsonSyntaxException jse) {
            foundPhoto = null;
            logger.error(jse.getMessage(), jse);

        }
        return foundPhoto;
    }

    @Override
    public Photo updatePhoto(Photo photo) {
        String str_representation = new Gson().toJson(photo);
        Photo updatedPhoto;

        try {
            Document document = collection.find(new Document("_id", photo.getTitle())).first();
            Document newDocument = Document.parse(str_representation);
            collection.findOneAndReplace(document, newDocument);

            String toJson = newDocument.toJson();
            updatedPhoto = this.findPhoto(new Gson().fromJson(toJson, Photo.class));
        } catch (NullPointerException npe) {
            updatedPhoto = null;
            logger.error(npe.getMessage(), npe);
        }
        return updatedPhoto;
    }

    @Override
    public boolean deletePhoto(Photo photo) {
        boolean status;

        try {
            if (findPhoto(photo) != null) {
                collection.findOneAndDelete(new Document("_id", photo.getTitle()));
                status = true;
            } else {
                status = false;
            }
        } catch (NullPointerException npe) {
            status = false;
            logger.error(npe.getMessage(), npe);
        }
        return status;
    }

    @Override
    public List<Photo> findAllPhotos() {

        Photo foundPhoto;
        List<Photo> photoList = new ArrayList<>();

        try {
            List<Document> listOfFoundedDocuments = collection.find().into(new ArrayList<>());

            for (Document document : listOfFoundedDocuments) {
                String toJson = document.toJson();
                foundPhoto = new Gson().fromJson(toJson, Photo.class);
                photoList.add(foundPhoto);
            }
        } catch (NullPointerException npe) {
            foundPhoto = null;
            logger.error(npe.getMessage(), npe);
        } catch (JsonSyntaxException jse) {
            foundPhoto = null;
            logger.error(jse.getMessage(), jse);
        }
        return photoList;
    }


    public static void main(String[] args) {

        PhotoDAOMongoDBImpl photoDAOMongoDB = new PhotoDAOMongoDBImpl();

        User user = new User();
        user.setUserName("Faerman");
        user.setPassWord("password");

        PhotoAlbum photoAlbum1 = new PhotoAlbum();
        photoAlbum1.setAlbumTitle("");


        // photo.setPhotoAlben(photoAlbumList);
        // boolean status = photoDAOMongoDB.savePhoto(photo);
        // boolean status = photoDAOMongoDB.deletePhoto(photo);
        // Photo status = photoDAOMongoDB.findPhoto(photo);
        // Photo status = photoDAOMongoDB.updatePhoto(photo);
        // System.out.print(photoDAOMongoDB.findAllPhotos());
        System.out.println(photoDAOMongoDB.findAllPhotosByUserNameAndPhotoAlbumTitle("Faerman", "new Album1"));

        // System.out.println(status);
    }
}