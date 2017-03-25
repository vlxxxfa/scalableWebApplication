package edu.hm.pam;

import edu.hm.pam.entity.User;

import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
public interface UserService {

    boolean createUser(User user);

    User findUserByUserName(String userName);

    User updateUser(User user);

    boolean deleteUser(User user);

    List<User> findAllUsers();

    // User logIn(User user);
    //
    // User logOut(User user);
}
