package edu.hm.pam;

import edu.hm.pam.entity.PhotoAlbum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/{userName}/photoAlben/")
public class PhotoAlbumController {

    // static final Logger logger = LoggerFactory.getLogger(PhotoController.class);

    private PhotoAlbumService photoAlbumService;

    @Autowired
    public PhotoAlbumController(PhotoAlbumService photoAlbumService) {
        this.photoAlbumService = photoAlbumService;
    }

    @RequestMapping(value = "createPhotoAlbumByUserName")
    public boolean createPhotoAlbumByUserName(@PathVariable String userName, @RequestBody PhotoAlbum photoAlbum) {
        return photoAlbumService.createPhotoAlbumByUserName(userName, photoAlbum);
    }

    @RequestMapping(path = "findAllPhotoAlbenByUserName")
    public List<PhotoAlbum> findAllPhotoAlbenByUserName(@PathVariable("userName") String userName) {
        return photoAlbumService.findAllPhotoAlbenByUserName(userName);
    }

    @RequestMapping(path = "updatePhotoAlbum", method = RequestMethod.GET)
    public PhotoAlbum updatePhotoAlbum(@RequestBody PhotoAlbum photoAlbum) {
        return photoAlbumService.updatePhotoAlbum(photoAlbum);
    }

    @RequestMapping(value = "deletePhotoAlbumByUserName/{albumTitle}", method = RequestMethod.GET)
    public boolean deletePhotoAlbumByUserName(@PathVariable("userName") String userName,
                                              @PathVariable("albumTitle") String albumTitle) {
        return photoAlbumService.deletePhotoAlbumByUserName(userName, albumTitle);
    }

}
