package edu.hm.pam.impl;

import edu.hm.pam.PhotoDAO;
import edu.hm.pam.PhotoService;
import edu.hm.pam.entity.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public boolean createPhotoByAlbumTitleOfUser(String userName, String albumTitle, Photo photo){
        return photoDAO.createPhotoByAlbumTitleOfUser(userName, albumTitle, photo);
    }

    @Override
    public List<Photo> findAllPhotosByUserNameAndPhotoAlbumTitle(String userName, String albumTitle){
        return photoDAO.findAllPhotosByUserNameAndPhotoAlbumTitle(userName, albumTitle);
    }

    @Override
    public Photo findPhoto(Photo photo) {
        return photoDAO.findPhoto(photo);
    }

    @Override
    public Photo updatePhoto(Photo photo) {
        return photoDAO.updatePhoto(photo);
    }

    @Override
    public boolean deletePhotoByUserNameAndPhotoAlbumTitle(String userName, String albumTitle, Photo photo) {
        return photoDAO.deletePhotoByUserNameAndPhotoAlbumTitle(userName, albumTitle, photo);
    }

    @Override
    public List<Photo> findAllPhotos() {
        return photoDAO.findAllPhotos();
    }
}
