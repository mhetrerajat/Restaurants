package com.mhetrerajat.restaurants;

import java.util.List;

/**
 * Created by rajatmhetre on 23/06/16.
 */
public class RestaurantEvent {

    public List<RestaurantModel> mRestaurantList;

    public RestaurantEvent(List<RestaurantModel> mRestaurantList) {
        this.mRestaurantList = mRestaurantList;
    }

    public List<RestaurantModel> getmRestaurantList() {
        return mRestaurantList;
    }
}



