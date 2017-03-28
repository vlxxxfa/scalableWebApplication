package edu.hm.pam;

import edu.hm.pam.entity.User;

import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
public interface UserService {

    boolean createUser(User user);

    boolean findUser(String userName, String password);

    User updateUser(User user);

    boolean deleteUser(String userName);

    List<User> findAllUsers();

    // User logIn(User user);
    //
    // User logOut(User user);
}
