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
@RequestMapping(value = "/photoAlbum")
public class PhotoAlbumController {

    // static final Logger logger = LoggerFactory.getLogger(PhotoController.class);

    private PhotoAlbumService photoAlbumService;

    @Autowired
    public PhotoAlbumController(PhotoAlbumService photoAlbumService) {
        this.photoAlbumService = photoAlbumService;
    }

    @RequestMapping(path = "findAllPhotoAlbenByUserName/{userName}")
    public List<PhotoAlbum> findAllPhotoAlbenByUserName(@PathVariable("userName") String userName){
        return photoAlbumService.findAllPhotoAlbenByUserName(userName);
    }

    @RequestMapping(path = "/createPhotoAlbum", method = RequestMethod.GET)
    public boolean createPhotoAlbum(@RequestBody PhotoAlbum photoAlbum) {
        return this.photoAlbumService.createPhotoAlbum(photoAlbum);
    }

    @RequestMapping(path = "/updatePhotoAlbum", method = RequestMethod.GET)
    public PhotoAlbum updatePhotoAlbum(@RequestBody PhotoAlbum photoAlbum) {
        return photoAlbumService.updatePhotoAlbum(photoAlbum);
    }

    @RequestMapping(value = "/deletePhotoAlbum", method = RequestMethod.GET)
    public boolean deletePhotoAlbum(@RequestBody PhotoAlbum photoAlbum) {
        return photoAlbumService.deletePhotoAlbum(photoAlbum);
    }

}
