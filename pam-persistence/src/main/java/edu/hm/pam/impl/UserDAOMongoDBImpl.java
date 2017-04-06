package edu.hm.pam.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.hm.pam.UserDAO;
import edu.hm.pam.entity.User;
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
        Document doc = Document.parse(str_representation);

        try {
            Document foundUser = collection.find(
                    eq("_id", user.getUserName())).first();
            if (foundUser == null && (user.getUserName() != null && user.getPassWord() != null)) {
                collection.insertOne(doc.append("_id", user.getUserName()));
                status = true;
            } else {
                status = false;
            }
        } catch (MongoWriteException mwe) {
            status = false;
            logger.error(mwe.getMessage(), mwe);
        }
        return status;
    }

    @Override
    public boolean findUser(String userName, String password) {

        boolean foundUser;

        try {
            Document document = collection.find(
                    and(
                            eq("_id", userName),
                            eq("passWord", password))).first();
            if (document != null) {
                foundUser = true;
            } else {
                foundUser = false;
            }
            // String toJson = document.toJson();
            // foundUser = new Gson().fromJson(toJson, User.class);
        } catch (NullPointerException npe) {
            foundUser = false;
            logger.error(npe.getMessage(), npe);
        } catch (JsonSyntaxException jse) {
            foundUser = false;
            logger.error(jse.getMessage(), jse);
        }
        return foundUser;
    }

    @Override
    public User updateUser(User user) {

        User updatedUser;

        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("passWord", user.getPassWord());
        updateFields.append("email", user.getEmail());

        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        BasicDBObject searchQuery = new BasicDBObject("_id", user.getUserName());

        collection.updateOne(searchQuery, setQuery);

        try {
            Document foundUser = collection.find(
                    eq("_id", user.getUserName())).first();
            String toJson = foundUser.toJson();
            updatedUser = new Gson().fromJson(toJson, User.class);

        } catch (NullPointerException npe) {
            updatedUser = null;
            logger.error(npe.getMessage(), npe);
        } catch (IllegalArgumentException iae) {
            updatedUser = null;
            logger.error(iae.getMessage(), iae);
        }
        return updatedUser;
    }

    @Override
    public boolean deleteUser(String userName) {
        boolean userIsDeleted;

        try {
            Document document = collection.find(eq("_id", userName)).first();
            if (document != null) {
                collection.deleteOne(document);
                userIsDeleted = true;
            } else {
                userIsDeleted = false;
            }
        } catch (NullPointerException npe) {
            userIsDeleted = false;
            logger.error(npe.getMessage(), npe);
        }
        return userIsDeleted;
    }

    @Override
    public List<User> findAllUsers() {

        List<User> userList = new ArrayList<>();
        User foundUser;

        try {
            List<Document> listOfFoundedDocuments = collection.find().into(new ArrayList<>());

            // Exception: org.bson.codecs.configuration.CodecConfigurationException:
            // Can't find a codec for class com.mongodb.DBRef.
            // URL: http://stackoverflow.com/questions/31827635/resolve-dbref-into-json
            CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry());
            final DocumentCodec codec = new DocumentCodec(codecRegistry, new BsonTypeClassMap());

            for (Document document : listOfFoundedDocuments) {
                String toJson = document.toJson(codec);
                foundUser = new Gson().fromJson(toJson, User.class);
                System.out.print(foundUser);
                userList.add(foundUser);
            }
        } catch (NullPointerException npe) {
            logger.error(npe.getMessage(), npe);
        } catch (JsonSyntaxException jse) {
            logger.error(jse.getMessage(), jse);
        }
        return userList;
    }

    public static void main(String[] args) {

        UserDAOMongoDBImpl userDAOMongoDB = new UserDAOMongoDBImpl();

        User user = new User();

        user.setUserName("Faerman");
        user.setPassWord("yyy");
        user.setEmail("xxx@hghg.de");

        // userDAOMongoDB.findAllPhotoAlbenByUserId(user);
        // userDAOMongoDB.createUser(user);
        // userDAOMongoDB.deleteUser(user);
        userDAOMongoDB.updateUser(user);
        // User status = userDAOMongoDB.findUser(user);
        // User status = userDAOMongoDB.updateUser(user);
        // userDAOMongoDB.findAllUsers();
        // System.out.println(userDAOMongoDB.findUserByUserName("Faerman"));
    }
}
