package edu.hm.pam;

import edu.hm.pam.entity.User;

import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
public interface UserDAO {

    boolean createUser(User user);

    User findUser(User user);

    User updateUser(User user);

    boolean deleteUser(User user);

    List<User> findAllUser();

    // User logIn(User user);
    //
    // User logOut(User user);
}
