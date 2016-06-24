package com.mhetrerajat.restaurants;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by rajatmhetre on 24/06/16.
 */
public class RestaurantsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Configure Realm
        RealmConfiguration mRealmConfig = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(mRealmConfig);
    }

}
