package com.mhetrerajat.restaurants;

import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by rajatmhetre on 23/06/16.
 */
public interface RestaurantService {

    @GET("nearbysearch/json?&radius=1000")
    Call<Restaurant> getPlaces(@QueryMap Map<String, String> params);

    @GET("nearbysearch/json?")
    Call<Restaurant> getPlacesByPageToken(@QueryMap Map<String, String> params);
}
