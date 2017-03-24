package edu.hm.pam;

import edu.hm.pam.entity.PhotoAlbum;
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
@RequestMapping(value = "/photoAlbum")
public class PhotoAlbumController {

    // static final Logger logger = LoggerFactory.getLogger(PhotoController.class);

    private PhotoAlbumService photoAlbumService;

    @Autowired
    public PhotoAlbumController(PhotoAlbumService photoAlbumService) {
        this.photoAlbumService = photoAlbumService;
    }

    @RequestMapping(path = "/savePhotoAlbum", method = RequestMethod.GET)
    public boolean savePhotoAlbum(@RequestBody PhotoAlbum photoAlbum) {
        return this.photoAlbumService.savePhotoAlbum(photoAlbum);
    }

    @RequestMapping(value = "/findPhotoAlbum", method = RequestMethod.GET)
    public PhotoAlbum findPhotoAlbum(@RequestBody PhotoAlbum photoAlbum) {
        return this.photoAlbumService.findPhotoAlbum(photoAlbum);
    }

    @RequestMapping(path = "/updatePhotoAlbum", method = RequestMethod.GET)
    public PhotoAlbum updatePhotoAlbum(@RequestBody PhotoAlbum photoAlbum) {
        return photoAlbumService.updatePhotoAlbum(photoAlbum);
    }

    @RequestMapping(value = "/deletePhotoAlbum", method = RequestMethod.GET)
    public boolean deletePhotoAlbum(@RequestBody PhotoAlbum photoAlbum) {
        return photoAlbumService.deletePhotoAlbum(photoAlbum);
    }

    @RequestMapping(value = "/findAllPhotoAlben", method = RequestMethod.GET)
    public List<PhotoAlbum> findAllPhotoAlben() {
        return photoAlbumService.findAllPhotoAlben();
    }

    // Testing
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public PhotoAlbum create() {
        PhotoAlbum photoAlbum = new PhotoAlbum();
        photoAlbum.setAlbumTitle("Amerika");
        if (!savePhotoAlbum(photoAlbum)) {
            return null;
        }
        return photoAlbum;
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public String find() {
        PhotoAlbum photoAlbum = new PhotoAlbum();
        photoAlbum.setAlbumTitle("Amerika");

        if (updatePhotoAlbum(photoAlbum) == null) {
            return "Object doesn't found";
        }
        return "Object founded";
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String update() {
        PhotoAlbum photoAlbum = new PhotoAlbum();
        photoAlbum.setAlbumTitle("Amerika");

        if (updatePhotoAlbum(photoAlbum) == null) {
            return "Object is null, no updated";
        }
        return "Object exist's, UPDATED";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete() {

        PhotoAlbum photoAlbum = new PhotoAlbum();
        photoAlbum.setAlbumTitle("Amerika");

        if (!deletePhotoAlbum(photoAlbum)) {
            return "Object is null, no deleted";
        }
        return "Object is deleted";
    }
}
