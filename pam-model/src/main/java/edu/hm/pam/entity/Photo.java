package edu.hm.pam.entity;

/**
 * Created by vlfa on 15.03.17.
 */
// @Entity(value="photo")
public class Photo {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Photo photo = (Photo) o;
        if (!this.title.equals(photo.getTitle())) {
            return false;
        }
        return title.equals(photo.title);
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }
}
