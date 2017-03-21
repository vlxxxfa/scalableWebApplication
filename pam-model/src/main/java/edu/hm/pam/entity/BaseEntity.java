package edu.hm.pam.entity;

import org.bson.types.ObjectId;

/**
 * Created by vlfa on 16.03.17.
 */
public abstract class BaseEntity {

    // @Id
    // @Property("id")
    protected ObjectId id;

    public BaseEntity() {
        super();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
