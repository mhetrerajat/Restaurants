package com.mhetrerajat.restaurants;

/**
 * Created by rajatmhetre on 24/06/16.
 */
public class RestaurantErrorEvent {

    public String getMessage() {
        return message;
    }

    public RestaurantErrorEvent(String message) {

        this.message = message;
    }

    public String message;
}
