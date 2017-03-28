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
                if (foundUserWithSamePhotoAlbum == null) {
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

            for (Document documentForPhotoAlbum : embeddedPhotoAlbenOfUser) {
                String toJson = documentForPhotoAlbum.toJson();
                PhotoAlbum photoAlbum = new Gson().fromJson(toJson, PhotoAlbum.class);
                // System.out.println(photoAlbum.getAlbumTitle());
                photoAlbumList.add(photoAlbum);
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
}