package edu.hm.vlfa.pam;


import edu.hm.pam.UserDAO;
import edu.hm.pam.impl.UserDAOMongoDBImpl;

/**
 * Created by vlfa on 16.03.17.
 */
public class UserServiceTest {

    UserDAO userDAO = new UserDAOMongoDBImpl();

    // @Test
    // public void getUser(){
    //     User user = new User();
    //     user.setUserName("username");
    //     user.setPassWord("password");
    //     user.setEmail("xxx@xxx");
    //     userDAO.createUser(user);
    // }

    // private User user;
    //
    // public UserServiceTest() {
    //     this.user.setUserName("admin");
    //     this.user.setPassWord("admin");
    // }
    //
    // @Test
    // public void createUser() throws Exception {
    //
    //     User user = this.user;
    //     User createUser = new User();
    //     createUser.setUserName("admin");
    //     createUser.setPassWord("admin");
    //     assertEquals(user.getUserName(), createUser.getUserName());
    // }
    //
    // @Test
    // public void updateUser() throws Exception {
    //
    // }
    //
    // @Test
    // public void deleteUser() throws Exception {
    //
    // }
    //
    // @Test
    // public void logIn() throws Exception {
    //
    // }
    //
    // @Test
    // public void logOut() throws Exception {
    //
    // }

}