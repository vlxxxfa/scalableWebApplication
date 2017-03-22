package edu.hm.pam;

import edu.hm.pam.entity.PhotoAlbum;

/**
 * Created by vlfa on 15.03.17.
 */
public interface PhotoAlbumDAO {

    boolean savePhotoAlbum(PhotoAlbum photoAlbum);

    PhotoAlbum findPhotoAlbum(PhotoAlbum photoAlbum);

    PhotoAlbum updatePhotoAlbum(PhotoAlbum photoAlbum);

    boolean deletePhotoAlbum(PhotoAlbum photoAlbum);
}
