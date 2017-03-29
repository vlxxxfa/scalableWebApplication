package edu.hm.pam;

import edu.hm.pam.entity.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/{userName}/{albumTitle}/photos/")
public class PhotoController {

    // static final Logger logger = LoggerFactory.getLogger(PhotoController.class);

    private PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @RequestMapping(path = "createPhotoByAlbumTitleOfUser", method = RequestMethod.POST)
    public boolean createPhotoByAlbumTitleOfUser(
            @PathVariable String userName, @PathVariable String albumTitle, @RequestBody Photo photo) {
        return this.photoService.createPhotoByAlbumTitleOfUser(userName, albumTitle, photo);
    }

    @RequestMapping(path = "findAllPhotosByUserNameAndPhotoAlbumTitle")
    public List<Photo> findAllPhotosByUserNameAndPhotoAlbumTitle(@PathVariable(value = "userName") String userName,
                                                                 @PathVariable(value = "albumTitle") String albumTitle) {
        return photoService.findAllPhotosByUserNameAndPhotoAlbumTitle(userName, albumTitle);
    }

    @RequestMapping(value = "findPhoto", method = RequestMethod.GET)
    public Photo findPhoto(@RequestBody Photo photo) {
        return this.photoService.findPhoto(photo);
    }

    @RequestMapping(path = "updatePhoto", method = RequestMethod.GET)
    public Photo updatePhoto(@RequestBody Photo photo) {
        return photoService.updatePhoto(photo);
    }

    @RequestMapping(value = "deletePhotoByUserNameAndPhotoAlbumTitle", method = RequestMethod.POST)
    public boolean deletePhotoByUserNameAndPhotoAlbumTitle(@PathVariable(value = "userName") String userName,
                                                           @PathVariable(value = "albumTitle") String albumTitle,
                                                           @RequestBody Photo photo) {
        return photoService.deletePhotoByUserNameAndPhotoAlbumTitle(userName, albumTitle, photo);
    }

    @RequestMapping(value = "findAllPhotos", method = RequestMethod.GET)
    public List<Photo> findAllPhotos() {
        return photoService.findAllPhotos();
    }
}
