package edu.hm.pam;

import edu.hm.pam.entity.Photo;

import java.util.List;

/**
 * Created by vlfa on 15.03.17.
 */
public interface PhotoService {

    boolean savePhoto(Photo photo);

    Photo findPhoto(Photo photo);

    Photo updatePhoto(Photo photo);

    boolean deletePhoto(Photo photo);

    List<Photo> findAllPhotos();
}
