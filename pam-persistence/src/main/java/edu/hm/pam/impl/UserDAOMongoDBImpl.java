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

    private MongoClient mongo = new MongoClient("localhost", 27017);
    private MongoDatabase db = mongo.getDatabase("qwertz");
    private MongoCollection<Document> collection = db.getCollection("users");

    private Document document;
    private User foundUser;
    private User updatedUser;
    private boolean status = true;
    private Gson gson;

    @Override
    public boolean createUser(User user) {

        gson = new Gson();
        String str_representation = gson.toJson(user);

        try {
            Document doc = Document.parse(str_representation).append("_id", user.getUserName());
            collection.insertOne(doc);
        } catch (MongoWriteException e) {
            status = false;
        }
        return status;
    }

    @Override
    public User findUser(User user) {

        try {
            document = collection.find(new Document("_id", user.getUserName())).first();
            String toJson = document.toJson();
            foundUser = new Gson().fromJson(toJson, User.class);
        } catch (NullPointerException npe) {
            foundUser = null;
        } catch (JsonSyntaxException jse) {
            foundUser = null;
        }
        return foundUser;
    }

    @Override
    public User updateUser(User user) {

        gson = new Gson();
        String str_representation = gson.toJson(user);

        try {
            document = collection.find(new Document("_id", user.getUserName())).first();
            Document newDocument = Document.parse(str_representation);
            collection.findOneAndReplace(document, newDocument);

            String toJson = newDocument.toJson();
            updatedUser = this.findUser(new Gson().fromJson(toJson, User.class));
        } catch (NullPointerException npe) {
            updatedUser = null;
        }
        return updatedUser;
    }

    @Override
    public boolean deleteUser(User user) {
        try {
            document = collection.findOneAndDelete(new Document("_id", user.getUserName()));
            if (collection.find(new Document("_id", user.getUserName())) == null) {
                return status;
            }
        } catch (NullPointerException npe) {
            status = false;
        }
        return status;
    }

    public static void main(String[] args) {

        UserDAOMongoDBImpl userDAOMongoDB = new UserDAOMongoDBImpl();

        User user = new User();

        user.setUserName("create");
        user.setPassWord("find");

        Photo photo1 = new Photo();
        photo1.setTitle("xxx");
        Photo photo2 = new Photo();
        photo2.setTitle("xxx");
        List<Photo> photoList = new ArrayList<>();
        photoList.add(photo1);
        photoList.add(photo2);

        PhotoAlbum photoAlbum = new PhotoAlbum();
        photoAlbum.setAlbumTitle("xxx");
        photoAlbum.setListOfPhotos(photoList);

        List<PhotoAlbum> photoAlbumList = new ArrayList<>();
        photoAlbumList.add(photoAlbum);

        user.setPhotoAlben(photoAlbumList);
        // boolean status = userDAOMongoDB.createUser(user);
        boolean status = userDAOMongoDB.deleteUser(user);
        // User status = userDAOMongoDB.findUser(user);
        // User status = userDAOMongoDB.updateUser(user);

        System.out.println(status);
    }

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
