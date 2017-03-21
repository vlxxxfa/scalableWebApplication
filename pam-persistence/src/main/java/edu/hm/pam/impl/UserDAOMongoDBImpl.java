package edu.hm.pam.impl;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.hm.pam.UserDAO;
import edu.hm.pam.entity.ext.User;
import org.bson.Document;
import org.springframework.stereotype.Service;

/**
 * Created by vlfa on 15.03.17.
 */
@Service
public class UserDAOMongoDBImpl implements UserDAO {

    MongoClient mongo = new MongoClient("localhost", 27017);
    MongoDatabase db = mongo.getDatabase("qwertz");
    MongoCollection collection = db.getCollection("users");

    @Override
    public void createUser(User user) {

        Gson gson = new Gson();
        String str_representation = gson.toJson(user);
        Document doc = Document.parse(str_representation);

        collection.insertOne(doc);
    }

    @Override
    public User findUser(User user) {
        // DBObject query = new BasicDBObject("_id", user.getId());
        // DBCursor cursor = collection.find(query);
        return null;
    }
    //
    // @Override
    // public User updateUser(User user) {
    //     return null;
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
