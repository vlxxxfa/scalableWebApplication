package edu.hm.pam;

import edu.hm.pam.entity.User;

import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
public interface UserDAO {

    boolean createUser(User user);

    User findUserByUserName(String userName);

    User updateUser(User user);

    boolean deleteUser(String userName);

    List<User> findAllUsers();

    // User logIn(User user);
    //
    // User logOut(User user);
}
