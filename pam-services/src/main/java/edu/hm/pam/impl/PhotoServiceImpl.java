package edu.hm.pam.impl;

import edu.hm.pam.PhotoDAO;
import edu.hm.pam.PhotoService;
import edu.hm.pam.entity.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public boolean savePhoto(Photo photo) {
        return this.photoDAO.savePhoto(photo);
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
    public boolean deletePhoto(Photo photo) {
        return photoDAO.deletePhoto(photo);
    }

}
