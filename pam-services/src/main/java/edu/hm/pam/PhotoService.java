package edu.hm.pam;

import edu.hm.pam.entity.Photo;

import java.io.IOException;
import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
public interface PhotoService {

    boolean createPhotoByAlbumTitleOfUser(String userName, String albumTitle, Photo photo) throws IOException;

    List<Photo> findAllPhotosByUserNameAndPhotoAlbumTitle(String userName, String albumTitle);

    Photo findPhoto(Photo photo);

    Photo updatePhoto(Photo photo);

    boolean deletePhotoByUserNameAndPhotoAlbumTitle(String userName, String albumTitle, Photo photo);

    List<Photo> findAllPhotos();
}
