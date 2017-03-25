package edu.hm.pam;

import edu.hm.pam.entity.PhotoAlbum;

import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
public interface PhotoAlbumDAO {

    List<PhotoAlbum> findAllPhotoAlbenByUserName(String userName);

    boolean createPhotoAlbum(PhotoAlbum photoAlbum);

    PhotoAlbum updatePhotoAlbum(PhotoAlbum photoAlbum);

    boolean deletePhotoAlbum(PhotoAlbum photoAlbum);
}
