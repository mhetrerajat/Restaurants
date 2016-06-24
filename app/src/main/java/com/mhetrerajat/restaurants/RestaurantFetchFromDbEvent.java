package com.mhetrerajat.restaurants;

import java.util.List;

/**
 * Created by rajatmhetre on 24/06/16.
 */
public class RestaurantFetchFromDbEvent {

    public List<Result> mRestaurantList;
    public String NEXT_PAGE_TOKEN;

    public RestaurantFetchFromDbEvent(List<Result> mRestaurantList, String NEXT_PAGE_TOKEN) {
        this.mRestaurantList = mRestaurantList;
        this.NEXT_PAGE_TOKEN = NEXT_PAGE_TOKEN;
    }

    public List<Result> getmRestaurantList() {
        return mRestaurantList;
    }

    public String getNEXT_PAGE_TOKEN() {
        return NEXT_PAGE_TOKEN;
    }
}
