package edu.hm.pam.entity;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Created by vlfa on 15.03.17.
 */
// @Entity(value="photo")
public class Photo {

    private String title;

    private MultipartFile multipartFile;

    private File file;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "title='" + title + '\'' +
                ", multipartFile=" + multipartFile +
                '}';
    }
}


