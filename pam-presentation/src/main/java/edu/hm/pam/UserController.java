package edu.hm.pam;

import edu.hm.pam.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    // final static Logger LOGGER = Logger.getLogger( UserController.class.getName() );

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/createUser", method = RequestMethod.GET)
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

    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    public boolean deleteUser(@RequestBody User user) {
        return userService.deleteUser(user);
    }

    @RequestMapping(value = "/findAllUser", method = RequestMethod.GET)
    public List<User> findAllUser(){
        return userService.findAllUser();
    }

    // Testing
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public User create() {
        User user = new User();
        user.setUserName("created");
        user.setPassWord("create");

        if (!createUser(user)) {
            return null;
        }
        return user;
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public String find() {
        User user = new User();
        user.setUserName("created");
        user.setPassWord("find");

        if (findUser(user) == null) {
            return "Object doesn't found";
        }
        return "Object founded";
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String update() {
        User user = new User();
        user.setUserName("created");
        user.setPassWord("update");

        if (updateUser(user) == null) {
            return "Object doesn't found";
        }
        return "Object updated";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete() {
        User user = new User();
        user.setUserName("created");
        user.setPassWord("delete");

        if (!deleteUser(user)) {
            return "Object doesn't found";
        }
        return "Object deleted";
    }

}
