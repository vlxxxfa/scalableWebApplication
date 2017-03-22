package edu.hm.pam;

import edu.hm.pam.entity.Photo;

/**
 * Created by vlfa on 15.03.17.
 */
public interface PhotoDAO {

    boolean savePhoto(Photo photo);

    Photo findPhoto(Photo photo);

    Photo updatePhoto(Photo photo);

    boolean deletePhoto(Photo photo);
}
