package edu.hm.pam;

import edu.hm.pam.entity.Photo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
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

    @RequestMapping(value = "savePhotoByAlbumTitleOfUser/", method = RequestMethod.POST)
    public boolean savePhotoByAlbumTitleOfUser(
            @PathVariable String userName,
            @PathVariable String albumTitle,
            @RequestParam("file") MultipartFile file) throws IOException {

        boolean result;

        if (!file.isEmpty()) {
            try {
                Photo photo = new Photo();
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
        return result;
    }

    @RequestMapping(path = "findAllPhotosByUserNameAndPhotoAlbumTitle", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Photo>> findAllPhotosByUserNameAndPhotoAlbumTitle(@PathVariable(value = "userName") String userName,
                                                                                 @PathVariable(value = "albumTitle") String albumTitle) throws IOException {
        final List<Photo> allPhotosByUserNameAndPhotoAlbumTitle = photoService.findAllPhotosByUserNameAndPhotoAlbumTitle(userName, albumTitle);

        List<Photo> toReturnPhotoList = new ArrayList<>(allPhotosByUserNameAndPhotoAlbumTitle.size());

        for (Photo photo : allPhotosByUserNameAndPhotoAlbumTitle) {
            Photo addPhotoToPhotoList = new Photo();
            addPhotoToPhotoList.setId(photo.getId());
            addPhotoToPhotoList.setTitle(photo.getMultipartFile().getOriginalFilename());
            addPhotoToPhotoList.setBase64(Base64.getEncoder().withoutPadding().encodeToString(photo.getMultipartFile().getBytes()));
            toReturnPhotoList.add(addPhotoToPhotoList);
        }
        ResponseEntity<List<Photo>> body = ResponseEntity.ok()
                .body(toReturnPhotoList);

        return body;
    }

    @RequestMapping(value = "deletePhotoByUserNameAndPhotoAlbumTitle/{id}", method = RequestMethod.POST)
    public boolean deletePhotoByUserNameAndPhotoAlbumTitle(@PathVariable(value = "userName") String userName,
                                                           @PathVariable(value = "albumTitle") String albumTitle,
                                                           @PathVariable(value = "id") String id) {
        return photoService.deletePhotoByUserNameAndPhotoAlbumTitle(userName, albumTitle, id);
    }
}