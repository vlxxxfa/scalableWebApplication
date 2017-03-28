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
@RequestMapping(value = "/users/")
public class UserController {

    // final static Logger LOGGER = Logger.getLogger( UserController.class.getName() );

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "createUser", method = RequestMethod.GET)
    public boolean createUser(@RequestBody User user) {
        return this.userService.createUser(user);
    }

    @RequestMapping(value = "findUser/{userName}/{passWord}", method = RequestMethod.GET)
    public boolean findUser(@PathVariable(value = "userName") String userName,
                         @PathVariable(value = "passWord") String password) {
        return this.userService.findUser(userName, password);
    }

    @RequestMapping(path = "updateUser", method = RequestMethod.GET)
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @RequestMapping(value = "deleteUser/{userName}", method = RequestMethod.GET)
    public boolean deleteUser(@PathVariable(value = "userName") String userName) {
        return userService.deleteUser(userName);
    }

    @RequestMapping(value = "findAllUsers", method = RequestMethod.GET)
    public List<User> findAllUsers(){
        return userService.findAllUsers();
    }
}
