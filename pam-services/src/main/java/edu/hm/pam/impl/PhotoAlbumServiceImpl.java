package edu.hm.pam.impl;

import edu.hm.pam.PhotoAlbumDAO;
import edu.hm.pam.PhotoAlbumService;
import edu.hm.pam.entity.PhotoAlbum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
@Component
public class PhotoAlbumServiceImpl implements PhotoAlbumService {

    private PhotoAlbumDAO photoAlbumDAO;

    @Autowired
    public PhotoAlbumServiceImpl(PhotoAlbumDAO photoAlbumDAO) {
        this.photoAlbumDAO = photoAlbumDAO;
    }

    @Override
    public List<PhotoAlbum> findAllPhotoAlbenByUserName(String userName){
        return photoAlbumDAO.findAllPhotoAlbenByUserName(userName);
    }

    @Override
    public boolean createPhotoAlbum(PhotoAlbum photoAlbum) {
        return this.photoAlbumDAO.createPhotoAlbum(photoAlbum);
    }

    @Override
    public PhotoAlbum updatePhotoAlbum(PhotoAlbum photoAlbum) {
        return photoAlbumDAO.updatePhotoAlbum(photoAlbum);
    }

    @Override
    public boolean deletePhotoAlbum(PhotoAlbum photoAlbum) {
        return photoAlbumDAO.deletePhotoAlbum(photoAlbum);
    }
}
