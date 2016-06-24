package com.mhetrerajat.restaurants;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rajatmhetre on 24/06/16.
 */
public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.restaurant_item_name) TextView mRestaurantName;
    @BindView(R.id.restaurant_item_vicinity) TextView mRestaurantVicinity;
    @BindView(R.id.restaurant_item_image) ImageView mRestaurantImage;

    public RestaurantViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        //mRestaurantName = (TextView) itemView.findViewById(R.id.restaurant_item_name);
        //mRestaurantImage = (ImageView) itemView.findViewById(R.id.restaurant_item_image);
       // mRestaurantVicinity = (TextView) itemView.findViewById(R.id.restaurant_item_vicinity);
    }
}
