package edu.hm.pam.impl;

import edu.hm.pam.UserDAO;
import edu.hm.pam.UserService;
import edu.hm.pam.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
@Component
public class UserServiceImpl implements UserService {

    private UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean createUser(User user) {
        return this.userDAO.createUser(user);
    }

    @Override
    public boolean findUser(String userName, String password) {
        return userDAO.findUser(userName, password);
    }

    @Override
    public User updateUser(User user) {
        return userDAO.updateUser(user);
    }

    @Override
    public boolean deleteUser(String userName) {
        return userDAO.deleteUser(userName);
    }

    @Override
    public List<User> findAllUsers(){
        return userDAO.findAllUsers();
    }
    //
    // @Override
    // public User logIn(User user) {
    //     return userDAO.logIn(user);
    // }
    //
    // @Override
    // public User logOut(User user) {
    //     return logOut(user);
    // }
}
