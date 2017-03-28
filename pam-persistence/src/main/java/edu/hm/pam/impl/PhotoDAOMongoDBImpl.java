package edu.hm.pam.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
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

    // TODO: 22.03.17 to insert a MongoConnection in an exclude class
    private MongoClient mongo = new MongoClient("localhost", 27017);
    private MongoDatabase db = mongo.getDatabase("qwertz");
    private MongoCollection<Document> collection = db.getCollection("users");

    @Override
    public boolean createPhotoByAlbumTitleOfUser(String userName, String albumTitle, Photo photo) {
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
        command.put("$push", data);

        try {
            Document foundPhotoInPhotoAlbumByUser = collection.find(
                    and(
                            eq("_id", userName),
                            eq("photoAlbumList.albumTitle", albumTitle),
                            eq("photoAlbumList.photoList.title", photo.getTitle()))).first();
            if (foundPhotoInPhotoAlbumByUser == null) {
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
        return null;
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
        System.out.println(photoDAOMongoDB.deletePhotoByUserNameAndPhotoAlbumTitle(
                "Test", "newAlbum1", photo));
        // System.out.println(photoDAOMongoDB.findAllPhotosByUserNameAndPhotoAlbumTitle("test", "album"));
        // photoDAOMongoDB.getPhotoAlbum("Faerman", "album");
        // System.out.println(status);
    }
}