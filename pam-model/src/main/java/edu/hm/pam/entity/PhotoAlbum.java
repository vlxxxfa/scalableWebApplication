package edu.hm.pam.entity;

import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
// @Entity(value="photoAlbum")
public class PhotoAlbum {

    private String albumTitle;

    private List<Photo> listOfPhotos;

    private User user;

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public void setListOfPhotos(List<Photo> listOfPhotos) {
        this.listOfPhotos = listOfPhotos;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public List<Photo> getListOfPhotos() {
        return listOfPhotos;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
