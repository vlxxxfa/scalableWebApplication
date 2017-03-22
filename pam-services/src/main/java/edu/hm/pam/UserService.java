package edu.hm.pam;

import edu.hm.pam.entity.User;

/**
 * Created by vlfa on 15.03.17.
 */
public interface UserService {

    boolean createUser(User user);

    User findUser(User user);

    User updateUser(User user);

    boolean deleteUser(User user);

    // User logIn(User user);
    //
    // User logOut(User user);
}
