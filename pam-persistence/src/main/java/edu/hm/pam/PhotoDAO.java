package edu.hm.pam;

import edu.hm.pam.entity.Photo;

import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
public interface PhotoDAO {

    List<Photo> findAllPhotosByUserNameAndPhotoAlbumTitle(String userName, String albumTitle);

    boolean createPhotoByAlbumTitleOfUser(String userName, String albumTitle, Photo photo);

    Photo findPhoto(Photo photo);

    Photo updatePhoto(Photo photo);

    boolean deletePhoto(Photo photo);

    List<Photo> findAllPhotos();
}
