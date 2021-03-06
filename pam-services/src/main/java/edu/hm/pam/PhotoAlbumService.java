package edu.hm.pam;

import edu.hm.pam.entity.PhotoAlbum;

import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
public interface PhotoAlbumService {

    boolean createPhotoAlbumByUserName(String userName, PhotoAlbum photoAlbum);

    List<PhotoAlbum> findAllPhotoAlbenByUserName(String userName);

    PhotoAlbum updatePhotoAlbum(PhotoAlbum photoAlbum);

    boolean deletePhotoAlbumByUserName(String userName, String albumTitle);
}
