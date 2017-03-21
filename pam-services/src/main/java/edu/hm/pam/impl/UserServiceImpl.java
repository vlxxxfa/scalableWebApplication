package edu.hm.pam.impl;

import edu.hm.pam.UserDAO;
import edu.hm.pam.UserService;
import edu.hm.pam.entity.ext.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by vlfa on 15.03.17.
 */
@Component
public class UserServiceImpl implements UserService {

    private final static Logger LOGGER = Logger.getLogger( UserServiceImpl.class.getName() );

    UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void createUser(User user) {
        if(LOGGER.isTraceEnabled()) {
            LOGGER.trace("user obtained in service module "+ user);
        }
        userDAO.createUser(user);
    }

    @Override
    public User findUser(User user) {
        return userDAO.findUser(user);
    }
    //
    // @Override
    // public User updateUser(User user) {
    //     return userDAO.updateUser(user);
    // }
    //
    // @Override
    // public void deleteUser(User user) {
    //     userDAO.deleteUser(user);
    // }
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
