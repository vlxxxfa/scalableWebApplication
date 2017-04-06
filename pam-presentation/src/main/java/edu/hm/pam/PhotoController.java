package edu.hm.pam;

import edu.hm.pam.entity.Photo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import java.io.IOException;
import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
@CrossOrigin(origins = "*")
@RestController
@MultipartConfig(fileSizeThreshold = 20971520)
@RequestMapping(value = "/users/{userName}/{albumTitle}/photos/")
public class PhotoController {

    static final Logger logger = LoggerFactory.getLogger(PhotoController.class);

    private PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @RequestMapping(value = "createPhotoByAlbumTitleOfUser/", method = RequestMethod.POST)
    public boolean createPhotoByAlbumTitleOfUser(
            @PathVariable String userName,
            @PathVariable String albumTitle,
            @RequestParam("file") MultipartFile file) throws IOException {

        System.out.println("MultipartFile OriginalFilename is: " + file.getOriginalFilename());
        System.out.println("MultipartFile Name is: " + file.getName());

        boolean result;

        if (!file.isEmpty()) {
            try {
                System.out.println("worked");
                Photo photo = new Photo();
                photo.setTitle(file.getOriginalFilename());
                photo.setMultipartFile(file);
                result = this.photoService.savePhotoByAlbumTitleOfUser(userName, albumTitle, photo);
            } catch (Exception e) {
                result = false;
                System.out.println("failed to upload the file: " + file + " => " + e.getMessage());
            }
        } else {
            result = false;
            System.out.println("failed to upload the file: " + file + ", because the file was empty.");
        }

        // Photo photo = new Photo();
        // photo.setPhotoData(file.getBytes());
        // photo.setTitle(file.getOriginalFilename());
        return result;
    }

    @RequestMapping(path = "findAllPhotosByUserNameAndPhotoAlbumTitle")
    public List<Photo> findAllPhotosByUserNameAndPhotoAlbumTitle(@PathVariable(value = "userName") String userName,
                                                                 @PathVariable(value = "albumTitle") String albumTitle) {
        return photoService.findAllPhotosByUserNameAndPhotoAlbumTitle(userName, albumTitle);
    }

    @RequestMapping(value = "deletePhotoByUserNameAndPhotoAlbumTitle", method = RequestMethod.POST)
    public boolean deletePhotoByUserNameAndPhotoAlbumTitle(@PathVariable(value = "userName") String userName,
                                                           @PathVariable(value = "albumTitle") String albumTitle,
                                                           @RequestBody Photo photo) {
        return photoService.deletePhotoByUserNameAndPhotoAlbumTitle(userName, albumTitle, photo);
    }
}
