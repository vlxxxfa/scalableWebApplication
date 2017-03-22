package edu.hm.pam;

import edu.hm.pam.entity.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by vlfa on 15.03.17.
 */
@RestController
@RequestMapping(value = "/photo")
public class PhotoController {

    // static final Logger logger = LoggerFactory.getLogger(PhotoController.class);

    private PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @RequestMapping(path = "/savePhoto", method = RequestMethod.GET)
    public boolean savePhoto(@RequestBody Photo photo) {
        return this.photoService.savePhoto(photo);
    }

    @RequestMapping(value = "/findPhoto", method = RequestMethod.GET)
    public Photo findPhoto(@RequestBody Photo photo) {
        return this.photoService.findPhoto(photo);
    }

    @RequestMapping(path = "/updatePhoto", method = RequestMethod.GET)
    public Photo updatePhoto(@RequestBody Photo photo) {
        return photoService.updatePhoto(photo);
    }

    @RequestMapping(value = "/deletePhoto", method = RequestMethod.GET)
    public boolean deletePhoto(@RequestBody Photo photo) {
        return photoService.deletePhoto(photo);
    }

    // Testing
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public Photo create() {
        Photo photo = new Photo();
        photo.setTitle("Amerika");
        if (!savePhoto(photo)) {
            return null;
        }
        return photo;
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public String find() {
        Photo photo = new Photo();
        photo.setTitle("Amerika");

        if (updatePhoto(photo) == null) {
            return "Object doesn't found";
        }
        return "Object founded";
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String update() {
        Photo photo = new Photo();
        photo.setTitle("Amerika");

        if (updatePhoto(photo) == null) {
            return "Object is null, no updated";
        }
        return "Object exist's, UPDATED";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete() {

        Photo photo = new Photo();
        photo.setTitle("Amerika");

        if (!deletePhoto(photo)) {
            return "Object is null, no deleted";
        }
        return "Object is deleted";
    }
}
