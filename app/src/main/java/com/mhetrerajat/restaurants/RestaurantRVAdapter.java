package com.mhetrerajat.restaurants;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rajatmhetre on 24/06/16.
 */
public class RestaurantRVAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    private String TAG = RestaurantRVAdapter.class.getSimpleName();

    public List<RestaurantModel> getmRestaurantList() {
        return mRestaurantList;
    }

    private List<RestaurantModel> mRestaurantList;
    private CoordinatorLayout mMainActivityCL;

    public RestaurantRVAdapter(List<RestaurantModel> mRestaurantList, CoordinatorLayout mMainActivityCL) {
        this.mRestaurantList = mRestaurantList;
        this.mMainActivityCL = mMainActivityCL;
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item, parent, false);
        RestaurantViewHolder mRestaurantVH = new RestaurantViewHolder(mView);
        return mRestaurantVH;
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {

        Log.d(TAG, String.valueOf(position) + " : " + mRestaurantList.get(position).getPhoto_references().size());
        if(mRestaurantList.get(position).getPhoto_references().size() != 0){
            StringBuilder PHOTO_URL = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?photoreference=")
                    .append(mRestaurantList.get(position).getPhoto_references().get(0).getPhoto_ref())
                    .append("&maxheight=")
                    .append(70)
                    .append("&key=")
                    .append(mMainActivityCL.getContext().getResources().getString(R.string.REMOTE_API_KEY));
            //Log.d(TAG, PHOTO_URL.toString());
            Picasso.with(mMainActivityCL.getContext()).load(PHOTO_URL.toString()).placeholder(R.drawable.shop).fit().into(holder.mRestaurantImage);
        }

        holder.mRestaurantName.setText(mRestaurantList.get(position).getName());
        holder.mRestaurantVicinity.setText(mRestaurantList.get(position).getVicinity());
    }

    @Override
    public int getItemCount() {
        return mRestaurantList.size();
    }


}
