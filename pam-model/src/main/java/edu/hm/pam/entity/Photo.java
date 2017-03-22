package edu.hm.pam.entity;

/**
 * Created by vlfa on 15.03.17.
 */
// @Entity(value="photo")
public class Photo {

    private String title;
    private PhotoAlbum photoAlbum;

    public String getTitle() {
        return title;
    }

    public PhotoAlbum getPhotoAlbum() {
        return photoAlbum;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPhotoAlbum(PhotoAlbum photoAlbum) {
        this.photoAlbum = photoAlbum;
    }
}
