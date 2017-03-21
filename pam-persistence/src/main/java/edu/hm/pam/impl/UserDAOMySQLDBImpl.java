package edu.hm.pam.impl;

import edu.hm.pam.UserDAO;
import edu.hm.pam.entity.ext.User;

/**
 * Created by vlfa on 16.03.17.
 */
public class UserDAOMySQLDBImpl implements UserDAO {

    @Override
    public boolean createUser(User user) {
        return false;
    }

    // @Override
    // public void createUser(User user) {
    //
    // }
    //
    @Override
    public User findUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }
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
}
