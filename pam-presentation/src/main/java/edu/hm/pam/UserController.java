package edu.hm.pam;

import edu.hm.pam.entity.ext.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by vlfa on 15.03.17.
 */
@RestController
public class UserController {

    // final static Logger LOGGER = Logger.getLogger( UserController.class.getName() );

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/createUser", method = RequestMethod.PUT)
    public boolean createUser(@RequestBody User user) {
        return this.userService.createUser(user);
    }

    @RequestMapping(value = "/findUser", method = RequestMethod.GET)
    public User findUser(@RequestBody User user) {
        return this.userService.findUser(user);
    }

    @RequestMapping(path = "/updateUser", method = RequestMethod.GET)
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.DELETE)
    public boolean deleteUser(@RequestBody User user) {
        return userService.deleteUser(user);
    }
    //
    // public User logIn(@RequestBody User user) {
    //     return userService.logIn(user);
    // }
    //
    // public User logOut(@RequestBody User user) {
    //     return userService.logOut(user);
    // }

    // @RequestMapping(value = "/delete")
    // public User delete() {
    //     User user = new User();
    //     user.setUserName("delete");
    //     user.setPassWord("delete");
    //
    //     if (!deleteUser(user)) {
    //         return null;
    //     }
    //     return user;
    // }
    //
    // @RequestMapping(value = "/create")
    // public User create() {
    //     User user = new User();
    //     user.setUserName("create");
    //     user.setPassWord("create");
    //
    //     if (!createUser(user)) {
    //         return null;
    //     }
    //     return user;
    // }
    //
    // @RequestMapping(value = "/find")
    // public User find() {
    //     User user = new User();
    //     user.setUserName("find");
    //     user.setPassWord("find");
    //
    //     if (findUser(user) == null) {
    //         return null;
    //     }
    //     return user;
    // }
    //
    // @RequestMapping(value = "/update")
    // public User update() {
    //     User user = new User();
    //     user.setUserName("update");
    //     user.setPassWord("update");
    //
    //     if (updateUser(user) == null) {
    //         return null;
    //     }
    //     return user;
    // }

}
