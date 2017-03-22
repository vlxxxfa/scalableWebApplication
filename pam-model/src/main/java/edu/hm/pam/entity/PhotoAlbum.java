package edu.hm.pam.entity;

import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
// @Entity(value="photoAlbum")
public class PhotoAlbum {

    private String albumTitle;

    private List<Photo> listOfPhotos;

    private List<User> users;

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public void setListOfPhotos(List<Photo> listOfPhotos) {
        this.listOfPhotos = listOfPhotos;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
