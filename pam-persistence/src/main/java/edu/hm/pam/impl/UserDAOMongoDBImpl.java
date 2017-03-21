package edu.hm.pam.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.hm.pam.UserDAO;
import edu.hm.pam.entity.ext.Photo;
import edu.hm.pam.entity.ext.PhotoAlbum;
import edu.hm.pam.entity.ext.User;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
@Service
public class UserDAOMongoDBImpl implements UserDAO {

    MongoClient mongo = new MongoClient("localhost", 27017);
    MongoDatabase db = mongo.getDatabase("qwertz");
    MongoCollection<Document> collection = db.getCollection("users");

    @Override
    public boolean createUser(User user) {

        boolean status = true;

        Gson gson = new Gson();
        String str_representation = gson.toJson(user);

        try {
            Document doc = Document.parse(str_representation);
            collection.insertOne(doc);
        } catch (MongoWriteException e) {
            status = false;
        }
        return status;
    }

    @Override
    public User findUser(User user) {

        Document document;
        User foundUser;

        try {
            document = collection.find(new Document("userName", user.getUserName())).first();
            String toJson = document.toJson();
            foundUser = new Gson().fromJson(toJson, User.class);
        } catch (NullPointerException n) {
            foundUser = null;
        } catch (JsonSyntaxException j) {
            foundUser = null;
        }
        return foundUser;
    }

    public User findUser2(User user) {

        Document document = collection.find(new Document("userName", user.getUserName())).first();
        String json = document.toJson();
        System.out.println(document.toJson());

        Gson g = new Gson();
        User user1 = g.fromJson(json, User.class);

        return user1;
    }

    public static void main(String[] args) {

        UserDAOMongoDBImpl userDAOMongoDB = new UserDAOMongoDBImpl();

        User user = new User();
        user.setUserName("admin");
        user.setPassWord("admin");

        Photo photo1 = new Photo();
        photo1.setTitle("photoTitle1");
        Photo photo2 = new Photo();
        photo2.setTitle("photoTitle2");
        List<Photo> photoList = new ArrayList<>();
        photoList.add(photo1);
        photoList.add(photo2);

        PhotoAlbum photoAlbum = new PhotoAlbum();
        photoAlbum.setAlbumTitle("AlbumTItle");
        photoAlbum.setListOfPhotos(photoList);

        List<PhotoAlbum> photoAlbumList = new ArrayList<>();
        photoAlbumList.add(photoAlbum);

        user.setPhotoAlben(photoAlbumList);
        boolean status = userDAOMongoDB.createUser(user);
        // User status = userDAOMongoDB.findUser(user);

        System.out.println(status);
    }

    // @Override
    // public User updateUser(User user) {
    //     return null;
    // }

    //
    // public User findUser2(User user) {
    //
    //     Bson filter = new Document("userName",user.getUserName());
    //     Object obj = collection.find().filter(filter).first();
    //
    //     return (User) obj;
    // }

    //
    // @Override
    // public void deleteUser(User user) {
    //
    // }
    //
    // @Override
    // public User logIn(User user) {
    //     return null;
    // }
    //
    // @Override
    // public User logOut(User user) {
    //     return null;
    // }

    // String db = new String("test");
    // MongoClient mongoClient = new MongoClient();
    // Morphia morphia = new Morphia();
    // Datastore datastore = morphia.createDatastore(mongoClient, db);
    //
    // public void createUser(User user) {
    //     try {
    //         datastore.save(new User(user.getUserName(), user.getPassWord(), user.getEmail()));
    //     } catch (MongoClientException e) {
    //         e.printStackTrace();
    //     }
    // }
    //
    // public Query<User> findUser(User user) {
    //     return datastore.createQuery(User.class).field("_id").equal(user.getId());
    // }
    //
    // public Query<User> updateUser(User user) {
    //     Query<User> updateQuery = datastore.createQuery(User.class).field("_id").equal(user.getId());
    //     UpdateOperations<User> ops = datastore.createUpdateOperations(User.class)
    //             .set("userName", user.getUserName())
    //             .set("passWord", user.getPassWord())
    //             .set("email", user.getEmail());
    //     datastore.update(updateQuery, ops);
    //     return findUser(user);
    // }
    //
    // public void deleteUser(User user) {
    // }
    //
    // public User logIn(User user) {
    //     return null;
    // }
    //
    // public User logOut(User user) {
    //     return null;
    // }


    // public static void main(String[] args){
    //
    //     String dbName = new String("test");
    //     MongoClient mongo = new MongoClient();
    //     Morphia morphia = new Morphia();
    //     Datastore datastore = morphia.createDatastore(mongo, dbName);
    //
    //     // morphia.mapPackage("edu.hm.vlfa.pam.entity");
    //
    //     User user = new User("admin", "admin");
    //
    //     Key<User> saveUser = datastore.save(user);
    //     System.out.println(saveUser.getId());
    //
    //     // Key<User> readuser = datastore.getKey()
    // }

}
