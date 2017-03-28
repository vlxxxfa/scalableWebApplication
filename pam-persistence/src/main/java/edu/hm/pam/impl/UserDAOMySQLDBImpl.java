package edu.hm.pam.impl;

import edu.hm.pam.UserDAO;
import edu.hm.pam.entity.User;

import java.util.List;

/**
 * Created by vlfa on 16.03.17.
 */
public class UserDAOMySQLDBImpl implements UserDAO {

    @Override
    public boolean createUser(User user) {
        return false;
    }

    @Override
    public boolean findUser(String userName, String password) {
        return false;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public boolean deleteUser(String userName) {
        return false;
    }

    @Override
    public List<User> findAllUsers() {
        return null;
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
}
