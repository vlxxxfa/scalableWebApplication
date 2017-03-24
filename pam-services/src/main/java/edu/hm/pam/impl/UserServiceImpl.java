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
    public User findUser(User user) {
        return userDAO.findUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userDAO.updateUser(user);
    }

    @Override
    public boolean deleteUser(User user) {
        return userDAO.deleteUser(user);
    }

    @Override
    public List<User> findAllUser(){
        return userDAO.findAllUser();
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
