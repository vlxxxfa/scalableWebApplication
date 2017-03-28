package edu.hm.pam.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.hm.pam.PhotoDAO;
import edu.hm.pam.entity.Photo;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    // // TODO: 22.03.17 to insert a MongoConnection in an exclude class
    private MongoClient mongo = new MongoClient("localhost", 27017);
    private MongoDatabase db = mongo.getDatabase("qwertz");
    private MongoCollection<Document> collection = db.getCollection("users");

    public boolean createPhotoByAlbumTitleOfUser(String userName, String albumTitle, Photo photo) {
        boolean status = false;

        Gson gson = new Gson();
        String str_representation = gson.toJson(photo);
        Document doc = Document.parse(str_representation);

        BasicDBObject query = new BasicDBObject();
        query.put("_id", userName);
        query.put("photoAlbumList.albumTitle", albumTitle);

        BasicDBObject data = new BasicDBObject();
        data.put("photoAlbumList.$.photoList", doc);

        BasicDBObject command = new BasicDBObject();
        command.put("$push", data);

        try {
            Document foundPhotoInPhotoAlbumByUser = collection.find(
                    and(
                            eq("_id", userName),
                            eq("photoAlbumList.albumTitle", albumTitle),
                            eq("photoAlbumList.photoList.title", photo.getTitle()))).first();
            if (foundPhotoInPhotoAlbumByUser == null){
                collection.updateOne(query, command);
                status = true;
                System.out.println("Photo didn't' exist -> Photo inserted");
            } else {
                System.out.println("Photo exist's' -> Photo didn't insert");
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

        Photo photo = new Photo();
        photo.setTitle("photo2");


        // photo.setPhotoAlben(photoAlbumList);
        // boolean status = photoDAOMongoDB.savePhoto(photo);
        // boolean status = photoDAOMongoDB.deletePhoto(photo);
        // Photo status = photoDAOMongoDB.findPhoto(photo);
        // Photo status = photoDAOMongoDB.updatePhoto(photo);
        // System.out.print(photoDAOMongoDB.findAllPhotos());
        System.out.println(photoDAOMongoDB.createPhotoByAlbumTitleOfUser("test", "albumSecond2", photo));
        // System.out.println(photoDAOMongoDB.findAllPhotosByUserNameAndPhotoAlbumTitle("test", "album"));
        // photoDAOMongoDB.getPhotoAlbum("Faerman", "album");
        // System.out.println(status);
    }
}