package edu.hm.pam.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.hm.pam.UserDAO;
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
 * Created by vlfa on 15.03.17.
 */
@Service
public class UserDAOMongoDBImpl implements UserDAO {

    static final Logger logger = LoggerFactory.getLogger(UserDAOMongoDBImpl.class);

    // // TODO: 22.03.17 to insert a MongoConnection in an exclude class
    private MongoClient mongo = new MongoClient("localhost", 27017);
    private MongoDatabase db = mongo.getDatabase("qwertz");
    private MongoCollection<Document> collection = db.getCollection("users");

    @Override
    public boolean createUser(User user) {

        boolean status;
        Gson gson = new Gson();
        String str_representation = gson.toJson(user);

        try {
            Document doc = Document.parse(str_representation).append("_id", user.getUserName());
            collection.insertOne(doc);
            status = true;
        } catch (MongoWriteException mwe) {
            status = false;
            logger.error(mwe.getMessage(), mwe);
        }
        return status;
    }

    @Override
    public User findUserByUserName(String userName) {

        User foundUser;

        try {
            Document document = collection.find(new Document("_id", userName)).first();
            String toJson = document.toJson();
            foundUser = new Gson().fromJson(toJson, User.class);
        } catch (NullPointerException npe) {
            foundUser = null;
            logger.error(npe.getMessage(), npe);
        } catch (JsonSyntaxException jse) {
            foundUser = null;
            logger.error(jse.getMessage(), jse);
        }
        return foundUser;
    }

    @Override
    public User updateUser(User user) {

        String str_representation = new Gson().toJson(user);
        User updatedUser;

        try {
            Document document = collection.find(new Document("_id", user.getUserName())).first();
            Document newDocument = Document.parse(str_representation);
            collection.findOneAndReplace(document, newDocument);

            String toJson = newDocument.toJson();
            updatedUser = this.findUserByUserName(toJson);
        } catch (NullPointerException npe) {
            updatedUser = null;
            logger.error(npe.getMessage(), npe);
        }
        return updatedUser;
    }

    @Override
    public boolean deleteUser(User user) {

        boolean status;

        try {
            if (findUserByUserName(user.getUserName()) != null) {
                collection.findOneAndDelete(new Document("_id", user.getUserName()));
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
    public List<User> findAllUsers() {

        User foundUser;
        List<User> userList = new ArrayList<>();

        try {
            List<Document> listOfFoundedDocuments = collection.find().into(new ArrayList<>());

            for (Document document : listOfFoundedDocuments) {
                String toJson = document.toJson();
                foundUser = new Gson().fromJson(toJson, User.class);
                System.out.print(foundUser);
                userList.add(foundUser);
            }
        } catch (NullPointerException npe) {
            foundUser = null;
            logger.error(npe.getMessage(), npe);
        } catch (JsonSyntaxException jse) {
            foundUser = null;
            logger.error(jse.getMessage(), jse);
        }
        return userList;
    }

    public static void main(String[] args) {

        UserDAOMongoDBImpl userDAOMongoDB = new UserDAOMongoDBImpl();

        User user = new User();

        user.setUserName("Ortlieb");
        user.setPassWord("password");

        PhotoAlbum photoAlbum1 = new PhotoAlbum();
        photoAlbum1.setAlbumTitle("album1");
        PhotoAlbum photoAlbum2 = new PhotoAlbum();
        photoAlbum2.setAlbumTitle("new Album2");
        PhotoAlbum photoAlbum3 = new PhotoAlbum();
        photoAlbum3.setAlbumTitle("new Album3");
        PhotoAlbum photoAlbum4 = new PhotoAlbum();
        photoAlbum4.setAlbumTitle("new Album4");
        PhotoAlbum photoAlbum5 = new PhotoAlbum();
        photoAlbum5.setAlbumTitle("new Album5");
        PhotoAlbum photoAlbum6 = new PhotoAlbum();
        photoAlbum6.setAlbumTitle("new Album6");
        PhotoAlbum photoAlbum7 = new PhotoAlbum();
        photoAlbum7.setAlbumTitle("new Album7");

        List<PhotoAlbum> photoAlbumList = new ArrayList<>();
        photoAlbumList.add(photoAlbum1);
        photoAlbumList.add(photoAlbum2);
        photoAlbumList.add(photoAlbum3);
        photoAlbumList.add(photoAlbum4);
        photoAlbumList.add(photoAlbum5);
        photoAlbumList.add(photoAlbum6);
        photoAlbumList.add(photoAlbum7);

        user.setPhotoAlbumList(photoAlbumList);

        Photo photo1 = new Photo();
        photo1.setTitle("photo1");
        Photo photo2 = new Photo();
        photo2.setTitle("photo1");
        Photo photo3 = new Photo();
        photo3.setTitle("photo1");
        Photo photo4 = new Photo();
        photo4.setTitle("photo1");
        Photo photo5 = new Photo();
        photo5.setTitle("photo1");

        List<Photo> photoList = new ArrayList<>();
        photoList.add(photo1);
        photoList.add(photo2);
        photoList.add(photo3);
        photoList.add(photo4);
        photoList.add(photo5);

        photoAlbum1.setPhotoList(photoList);

        // userDAOMongoDB.findAllPhotoAlbenByUserId(user);
        userDAOMongoDB.createUser(user);
        // userDAOMongoDB.deleteUser(user);
        // User status = userDAOMongoDB.findUser(user);
        // User status = userDAOMongoDB.updateUser(user);
        // userDAOMongoDB.findAllUsers();
        System.out.println(userDAOMongoDB.findUserByUserName("Faerman"));
    }
}
