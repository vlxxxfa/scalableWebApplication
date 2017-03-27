package edu.hm.pam.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.hm.pam.UserDAO;
import edu.hm.pam.entity.User;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            Document document = collection.find(eq("_id", userName)).first();
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
    public boolean deleteUser(String userName) {
        return false;
    }

    // @Override
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

        user.setUserName("Faerman");
        user.setPassWord("password");

        // userDAOMongoDB.findAllPhotoAlbenByUserId(user);
        userDAOMongoDB.createUser(user);
        // userDAOMongoDB.deleteUser(user);
        // User status = userDAOMongoDB.findUser(user);
        // User status = userDAOMongoDB.updateUser(user);
        // userDAOMongoDB.findAllUsers();
        System.out.println(userDAOMongoDB.findUserByUserName("Faerman"));
    }
}
