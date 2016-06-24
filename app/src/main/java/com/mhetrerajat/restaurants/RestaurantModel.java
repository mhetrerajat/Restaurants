package com.mhetrerajat.restaurants;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rajatmhetre on 24/06/16.
 */
public class RestaurantModel extends RealmObject {

    @PrimaryKey
    private String place_id;

    private Double latitude, longitude, rating;
    private String name, reference, vicinity;
    private Integer price_level;
    private RealmList<RestaurantPhotoModel> photo_references = new RealmList<RestaurantPhotoModel>();


    public RestaurantModel() {
    }

    public RealmList<RestaurantPhotoModel> getPhoto_references() {
        return photo_references;
    }

    public void setPhoto_references(RealmList<RestaurantPhotoModel> photo_references) {
        this.photo_references = photo_references;
    }

    public Integer getPrice_level() {
        return price_level;
    }

    public void setPrice_level(Integer price_level) {
        this.price_level = price_level;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}


