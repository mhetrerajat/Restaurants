package com.mhetrerajat.restaurants;

import java.util.List;

/**
 * Created by rajatmhetre on 24/06/16.
 */
public class RestaurantLoadMoreEvent {

    public List<Result> mRestaurantList;

    public RestaurantLoadMoreEvent(List<Result> mRestaurantList, String mNextPageToken) {
        this.mRestaurantList = mRestaurantList;
        this.mNextPageToken = mNextPageToken;
    }

    public String getmNextPageToken() {
        return mNextPageToken;
    }

    public String mNextPageToken;


    public List<Result> getmRestaurantList() {
        return mRestaurantList;
    }
}
