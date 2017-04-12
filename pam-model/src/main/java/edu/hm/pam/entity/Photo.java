package edu.hm.pam.entity;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by vlfa on 15.03.17.
 */
// @Entity(value="photo")
public class Photo {

    private MultipartFile multipartFile;

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "title='" + multipartFile.getName() + '\'' +
                ", multipartFile=" + multipartFile +
                '}';
    }
}


