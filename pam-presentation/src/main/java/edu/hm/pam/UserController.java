package edu.hm.pam;

import edu.hm.pam.entity.ext.Photo;
import edu.hm.pam.entity.ext.PhotoAlbum;
import edu.hm.pam.entity.ext.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping(path = "/createUser", method = RequestMethod.GET)
    public boolean createUser(@RequestBody User user) {
        return this.userService.createUser(user);
    }

    @RequestMapping(value = "/findUser", method = RequestMethod.GET)
    public User findUser(@RequestBody User user) {
        return this.userService.findUser(user);
    }

    // @RequestMapping(path = "/updatedUser", method = RequestMethod.GET)
    // public User updateUser(@RequestBody User user) {
    //     return userService.updateUser(user);
    // }
    //
    // public void deleteUser(@RequestBody User user) {
    //     userService.deleteUser(user);
    // }
    //
    // public User logIn(@RequestBody User user) {
    //     return userService.logIn(user);
    // }
    //
    // public User logOut(@RequestBody User user) {
    //     return userService.logOut(user);
    // }

    @RequestMapping(value = "/test")
    public String doTest() {
        User user = new User();
        user.setUserName("admin");
        user.setPassWord("admin");

        Photo photo1 = new Photo();
        photo1.setTitle("photoTitle1");
        Photo photo2 = new Photo();
        photo2.setTitle("photoTitle2");
        List<Photo> photoList = new ArrayList<>();
        photoList.add(photo1);
        photoList.add(photo2);

        PhotoAlbum photoAlbum = new PhotoAlbum();
        photoAlbum.setAlbumTitle("AlbumTItle");
        photoAlbum.setListOfPhotos(photoList);

        List<PhotoAlbum> photoAlbumList = new ArrayList<>();
        photoAlbumList.add(photoAlbum);

        user.setPhotoAlben(photoAlbumList);

        if (findUser(user) == null) {
            return "Done: User doesn't find";
        }
        return "Done: User find";
    }
}
