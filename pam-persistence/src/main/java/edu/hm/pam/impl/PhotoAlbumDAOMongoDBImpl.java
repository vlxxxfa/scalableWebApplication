package edu.hm.pam.impl;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.hm.pam.PhotoAlbumDAO;
import edu.hm.pam.entity.PhotoAlbum;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
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
public class PhotoAlbumDAOMongoDBImpl implements PhotoAlbumDAO {

    static final Logger logger = LoggerFactory.getLogger(PhotoAlbumDAOMongoDBImpl.class);

    // // TODO: 22.03.17 to insert a MongoConnection in an exclude class
    private MongoClient mongo = new MongoClient("localhost", 27017);
    private MongoDatabase db = mongo.getDatabase("qwertz");
    private MongoCollection<Document> collection = db.getCollection("users");

    @Override
    public boolean createPhotoAlbumByUserName(String userName, PhotoAlbum photoAlbum) {

        boolean status;

        Gson gson = new Gson();
        String str_representation = gson.toJson(photoAlbum);
        Document doc = Document.parse(str_representation);

        Document addNewPhotoAlbumToUser = new Document("$push", new Document("photoAlbumList", doc));

        try {
            Document foundUser = collection.find(eq("_id", userName)).first();
            Document foundUserWithSamePhotoAlbum = collection.find(
                    and(
                            eq("_id", userName),
                            eq("photoAlbumList.albumTitle", photoAlbum.getAlbumTitle()))).first();
            if (foundUser != null) {
                if (foundUserWithSamePhotoAlbum == null && photoAlbum.getAlbumTitle() != null) {
                    collection.updateOne(eq("_id", userName),
                            addNewPhotoAlbumToUser);
                    status = true;
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
    public List<PhotoAlbum> findAllPhotoAlbenByUserName(String userName) {

        List<PhotoAlbum> photoAlbumList = new ArrayList<>();

        try {
            Document foundUser = collection.find(new Document("_id", userName)).first();
            List<Document> embeddedPhotoAlbenOfUser = (List<Document>) foundUser.get("photoAlbumList");

            if (embeddedPhotoAlbenOfUser == null || embeddedPhotoAlbenOfUser.isEmpty()) {
                return photoAlbumList;
            } else {

                // Exception: org.bson.codecs.configuration.CodecConfigurationException:
                // Can't find a codec for class com.mongodb.DBRef.
                // URL: http://stackoverflow.com/questions/31827635/resolve-dbref-into-json
                CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry());
                final DocumentCodec codec = new DocumentCodec(codecRegistry, new BsonTypeClassMap());

                for (Document documentForPhotoAlbum : embeddedPhotoAlbenOfUser) {
                    String toJson = documentForPhotoAlbum.toJson(codec);
                    PhotoAlbum photoAlbum = new Gson().fromJson(toJson, PhotoAlbum.class);
                    // System.out.println(photoAlbum.getAlbumTitle());
                    photoAlbumList.add(photoAlbum);
                }
            }
        } catch (NullPointerException npe) {
            logger.error(npe.getMessage(), npe);
        }
        return photoAlbumList;
    }

    @Override
    public PhotoAlbum updatePhotoAlbum(PhotoAlbum photoAlbum) {
        return null;
    }

    @Override
    public boolean deletePhotoAlbumByUserName(String userName, String albumTitle) {
        boolean status;

        BasicDBObject sq = new BasicDBObject("userName", userName);
        BasicDBObject idoc = new BasicDBObject("albumTitle", albumTitle);
        BasicDBObject odoc = new BasicDBObject("photoAlbumList", idoc);
        BasicDBObject delq = new BasicDBObject("$pull", odoc);

        try {
            Document foundUser = collection.find(eq("_id", userName)).first();
            Document foundPhotoAlbum = collection.find(
                    and(
                            eq("_id", userName),
                            eq("photoAlbumList.albumTitle", albumTitle))).first();
            if (foundUser != null) {
                if (foundPhotoAlbum != null) {
                    collection.updateOne(sq, delq);
                    status = true;
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

    public static void main(String[] args) {

        PhotoAlbumDAOMongoDBImpl photoAlbumDAOMongoDB = new PhotoAlbumDAOMongoDBImpl();

        // Photo photo = new Photo();
        // photo.setTitle("London");
        // Photo photo1 = new Photo();
        // photo1.setTitle("Bayern");
        // Photo photo2 = new Photo();
        // photo2.setTitle("Paris");

        // List<PhotoAlbum> photoAlbumList = new ArrayList<>();
        // photoAlbumList.add(photoAlbum);
    PhotoAlbum album = new PhotoAlbum();
    album.setAlbumTitle("album");

        // photo.setPhotoAlben(photoAlbumList);
        // photoAlbumDAOMongoDB.createPhotoAlbumByUserName("Ortlieb", photoAlbum);
        // photoAlbumDAOMongoDB.createPhotoAlbumByUserName("Faerman", photoAlbumSecond);
        // photoAlbumDAOMongoDB.testCreatePhotoAlbum("Faerman", photoAlbum);
        // System.out.println(photoAlbumDAOMongoDB.findAllPhotoAlbenByUserName("qw"));
        photoAlbumDAOMongoDB.createPhotoAlbumByUserName("vfaerman", album);
        // boolean status = photoAlbumDAOMongoDB.deletePhotoAlbum(photoAlbum);
        // Photo status = photoAlbumDAOMongoDB.findPhoto(photo);
        // Photo status = photoAlbumDAOMongoDB.updatePhoto(photo);
        // List<PhotoAlbum> allPhotoAlbenByUserName = photoAlbumDAOMongoDB.findAllPhotoAlbenByUserName("Faerman");
        // for (PhotoAlbum photoAlbum1 : allPhotoAlbenByUserName) {
        //     System.out.println(photoAlbum1.toString());
        //
        // }
    }
}