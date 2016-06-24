package com.mhetrerajat.restaurants;

import io.realm.RealmObject;

/**
 * Created by rajatmhetre on 24/06/16.
 */
public class RestaurantPhotoModel extends RealmObject {
    private String photo_ref;
    private Integer height, width;

    public RestaurantPhotoModel() {
    }

    public String getPhoto_ref() {
        return photo_ref;
    }

    public void setPhoto_ref(String photo_ref) {
        this.photo_ref = photo_ref;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}