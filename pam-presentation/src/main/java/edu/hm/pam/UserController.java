package edu.hm.pam;

import edu.hm.pam.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
@CrossOrigin
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

    @RequestMapping(value = "/findUserByUserName/{userName}", method = RequestMethod.GET)
    public User findUserByUserName(@PathVariable(value = "userName") String userName) {
        System.out.println("testController");
        return this.userService.findUserByUserName(userName);
    }

    @RequestMapping(path = "/updateUser", method = RequestMethod.GET)
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    public boolean deleteUser(@RequestBody User user) {
        return userService.deleteUser(user);
    }

    @RequestMapping(value = "/findAllUsers", method = RequestMethod.GET)
    public List<User> findAllUsers(){
        return userService.findAllUsers();
    }
}
