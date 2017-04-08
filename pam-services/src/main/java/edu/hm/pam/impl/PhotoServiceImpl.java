package edu.hm.pam.impl;

import edu.hm.pam.PhotoDAO;
import edu.hm.pam.PhotoService;
import edu.hm.pam.entity.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
@Component
public class PhotoServiceImpl implements PhotoService {

    private PhotoDAO photoDAO;


    @Autowired
    public PhotoServiceImpl(PhotoDAO photoDAO) {
        this.photoDAO = photoDAO;
    }

    @Override
    public boolean savePhotoByAlbumTitleOfUser(String userName, String albumTitle, Photo photo) throws IOException {
        return photoDAO.savePhotoByAlbumTitleOfUser(userName, albumTitle, photo);
    }

    @Override
    public List<Photo> findAllPhotosByUserNameAndPhotoAlbumTitle(String userName, String albumTitle) throws IOException {
        return photoDAO.findAllPhotosByUserNameAndPhotoAlbumTitle(userName, albumTitle);
    }

    @Override
    public boolean deletePhotoByUserNameAndPhotoAlbumTitle(String userName, String albumTitle, Photo photo) {
        return photoDAO.deletePhotoByUserNameAndPhotoAlbumTitle(userName, albumTitle, photo);
    }
}
