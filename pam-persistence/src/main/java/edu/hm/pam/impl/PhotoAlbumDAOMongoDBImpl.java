package edu.hm.pam.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.hm.pam.PhotoAlbumDAO;
import edu.hm.pam.entity.Photo;
import edu.hm.pam.entity.PhotoAlbum;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public List<PhotoAlbum> findAllPhotoAlbenByUserName(String userName) {

        List<PhotoAlbum> photoAlbumList = new ArrayList<>();

        try {
            Document foundUser = collection.find(new Document("_id", userName)).first();
            List<Document> embeddedPhotoAlbenOfUser = (List<Document>) foundUser.get("photoAlbumList");

            for (Document documentForPhotoAlbum : embeddedPhotoAlbenOfUser) {
                String toJson = documentForPhotoAlbum.toJson();
                PhotoAlbum photoAlbum = new Gson().fromJson(toJson, PhotoAlbum.class);
                System.out.println(photoAlbum.getAlbumTitle());
                photoAlbumList.add(photoAlbum);
            }
        } catch (NullPointerException npe) {
            logger.error(npe.getMessage(), npe);
        }
        return photoAlbumList;
    }

    @Override
    public boolean createPhotoAlbum(PhotoAlbum photoAlbum) {
        return false;
    }

    @Override
    public PhotoAlbum updatePhotoAlbum(PhotoAlbum photoAlbum) {
        String str_representation = new Gson().toJson(photoAlbum);
        PhotoAlbum updatedPhotoAlbum;

        try {
            Document document = collection.find(new Document("_id", photoAlbum.getAlbumTitle())).first();
            Document newDocument = Document.parse(str_representation);
            collection.findOneAndReplace(document, newDocument);

            String toJson = newDocument.toJson();
            updatedPhotoAlbum = this.findPhotoAlbum(new Gson().fromJson(toJson, PhotoAlbum.class));
        } catch (NullPointerException npe) {
            updatedPhotoAlbum = null;
            logger.error(npe.getMessage(), npe);
        }
        return updatedPhotoAlbum;
    }

    @Override
    public boolean deletePhotoAlbum(PhotoAlbum photoAlbum) {
        boolean status;

        try {
            if (findPhotoAlbum(photoAlbum) != null) {
                collection.findOneAndDelete(new Document("_id", photoAlbum.getAlbumTitle()));
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

    public PhotoAlbum findPhotoAlbum(PhotoAlbum photoAlbum) {

        PhotoAlbum foundPhotoAlbum;

        try {
            Document document = collection.find(new Document("_id", photoAlbum.getAlbumTitle())).first();
            String toJson = document.toJson();
            foundPhotoAlbum = new Gson().fromJson(toJson, PhotoAlbum.class);
        } catch (NullPointerException npe) {
            foundPhotoAlbum = null;
            logger.error(npe.getMessage(), npe);
        } catch (JsonSyntaxException jse) {
            foundPhotoAlbum = null;
            logger.error(jse.getMessage(), jse);

        }
        return foundPhotoAlbum;
    }

    // @Override
    public boolean createPhotoAlbumByUserName(String userName, PhotoAlbum photoAlbum) {
        boolean status = false;
        // boolean foundPhotoAlbumByUser = false;
        // Document foundUser;

        Gson gson = new Gson();
        String str_representation = gson.toJson(photoAlbum);
        Document doc = Document.parse(str_representation);

        // try {
        // foundUser = collection.find(eq("_id", userName)).first();
        // List<Document> embeddedPhotoAlbenOfUser = (List<Document>) foundUser.get("photoAlbumList");
        // for (Document embeddedPhotoAlben : embeddedPhotoAlbenOfUser) {
        //
        //     String toJson = embeddedPhotoAlben.toJson();
        //     PhotoAlbum photoAlbumFromCollectionByUserName = new Gson().fromJson(toJson, PhotoAlbum.class);
        //
        //     if (photoAlbumFromCollectionByUserName.equals(photoAlbum)) {
        //         foundPhotoAlbumByUser = true;
        //         break;
        //     } else {
        //         foundPhotoAlbumByUser = false;
        //     }
        // }
        // if (foundPhotoAlbumByUser == false) {



        // $pull: added to the existed document
        // $set: delete and add a new document
        collection.updateOne(eq("_id", userName), new Document("$push",
                new Document("photoAlbumList", Arrays.asList(doc)
                ).append("photoAlbumList", doc)));
        status = true;
        // }
        // } catch (MongoWriteException mwe) {
        //     status = false;
        //     logger.error(mwe.getMessage(), mwe);
        // }
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

        List<Photo> photoList = new ArrayList<>();
        // photoList.add(photo);
        // photoList.add(photo1);
        // photoList.add(photo2);

        PhotoAlbum photoAlbum = new PhotoAlbum();
        photoAlbum.setAlbumTitle("test");
        photoAlbum.setPhotoList(photoList);

        // List<PhotoAlbum> photoAlbumList = new ArrayList<>();
        // photoAlbumList.add(photoAlbum);

        // photo.setPhotoAlben(photoAlbumList);
        boolean status = photoAlbumDAOMongoDB.createPhotoAlbumByUserName("Puniegova", photoAlbum);
        // boolean status = photoAlbumDAOMongoDB.deletePhotoAlbum(photoAlbum);
        // Photo status = photoAlbumDAOMongoDB.findPhoto(photo);
        // Photo status = photoAlbumDAOMongoDB.updatePhoto(photo);

        System.out.println(status);
    }
}