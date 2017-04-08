package edu.hm.pam;

import edu.hm.pam.entity.Photo;

import java.io.IOException;
import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
public interface PhotoDAO {

    boolean savePhotoByAlbumTitleOfUser(String userName, String albumTitle, Photo photo) throws IOException;

    List<Photo> findAllPhotosByUserNameAndPhotoAlbumTitle(String userName, String albumTitle) throws IOException;

    boolean deletePhotoByUserNameAndPhotoAlbumTitle(String userName, String albumTitle, Photo photo);
}
