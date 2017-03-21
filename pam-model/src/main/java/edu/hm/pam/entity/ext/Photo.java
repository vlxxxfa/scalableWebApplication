package edu.hm.pam.entity.ext;

import edu.hm.pam.entity.BaseEntity;

/**
 * Created by vlfa on 15.03.17.
 */
// @Entity(value="photo")
public class Photo extends BaseEntity {

    private String title;
    private PhotoAlbum photoAlbum;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPhotoAlbum(PhotoAlbum photoAlbum) {
        this.photoAlbum = photoAlbum;
    }
}
