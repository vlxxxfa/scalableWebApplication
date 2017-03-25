package edu.hm.pam.entity;

import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
// @Entity(value="photoAlbum")
public class PhotoAlbum {

    private String albumTitle;

    private List<Photo> photoList;

    private User user;

    public String getAlbumTitle() {
        return albumTitle;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setAlbumTitle(String albumTitle) {

        this.albumTitle = albumTitle;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
