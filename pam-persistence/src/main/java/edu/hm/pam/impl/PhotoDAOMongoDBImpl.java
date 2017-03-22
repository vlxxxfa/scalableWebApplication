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
    private MongoCollection<Document> collection = db.getCollection("photos");
    private MongoCollection<Document> collectionForUsers = db.getCollection("users");


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


    public static void main(String[] args) {

        PhotoDAO photoDAOMongoDB = new PhotoDAOMongoDBImpl();

        Photo photo = new Photo();
        photo.setTitle("Amerika");
        Photo photo1 = new Photo();
        photo1.setTitle("Bayern");
        Photo photo2 = new Photo();
        photo2.setTitle("Paris");

        List<Photo> photoList = new ArrayList<>();
        photoList.add(photo);
        photoList.add(photo1);
        photoList.add(photo2);

        PhotoAlbum photoAlbum = new PhotoAlbum();
        photoAlbum.setAlbumTitle("world");
        photoAlbum.setListOfPhotos(photoList);

        List<PhotoAlbum> photoAlbumList = new ArrayList<>();
        photoAlbumList.add(photoAlbum);

        // photo.setPhotoAlben(photoAlbumList);
        // boolean status = photoDAOMongoDB.savePhoto(photo);
        boolean status = photoDAOMongoDB.deletePhoto(photo);
        // Photo status = photoDAOMongoDB.findPhoto(photo);
        // Photo status = photoDAOMongoDB.updatePhoto(photo);

        System.out.println(status);
    }
}