package com.mhetrerajat.restaurants;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import io.realm.Realm;
import io.realm.RealmList;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private String BASE_URL= "https://maps.googleapis.com/maps/api/place/";
    private String NEXT_PAGE_TOKEN;

    @BindView(R.id.main_activity_cl) CoordinatorLayout mMainActivityCL;

    @BindView(R.id.restaurants_rv) RecyclerView mRestaurantsRV;

    private EventBus mRestaurantEventBus;
    private LinearLayoutManager mRestaurantsLLM;
    private RestaurantRVAdapter mRestaurantAdapter;
    private List<Result> mRestaurantList;
    private List<RestaurantModel> mRestaurantDbFetchedList;
    Realm mRealm;
    private Integer totalItemCount, visibleItemCount, firstVisibleItem, previousTotal = 0, VISIBLE_THRESHOLD = 5;
    private Boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Views
        ButterKnife.bind(this);

        // Retrofit Job
        fetchRestaurants();

        // Init
        mRestaurantList = new ArrayList<>();
        mRestaurantDbFetchedList = new ArrayList<>();
        mRealm = Realm.getDefaultInstance();

        // Event Bus
        mRestaurantEventBus = EventBus.getDefault();

        // Recycler View Init
        mRestaurantsLLM = new LinearLayoutManager(this);
        mRestaurantsRV.setLayoutManager(mRestaurantsLLM);
        mRestaurantsRV.setItemAnimator(new DefaultItemAnimator());
        mRestaurantAdapter = new RestaurantRVAdapter(mRestaurantDbFetchedList, mMainActivityCL);
        mRestaurantsRV.setAdapter(mRestaurantAdapter);

        if (mRestaurantsRV.getLayoutManager() instanceof LinearLayoutManager) {
            mRestaurantsRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    totalItemCount = mRestaurantsLLM.getItemCount();
                    visibleItemCount = mRestaurantsLLM.getChildCount();
                    firstVisibleItem = mRestaurantsLLM.findFirstVisibleItemPosition();

                    Log.d(TAG, "FVI : " + firstVisibleItem);
                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
                        // End has been reached
                        fetchMoreRestaurantsByPageToken();
                        Log.d(TAG, "end is here fetch more");
                        loading = true;
                    }
                }
            });
        }

    }

    public void fetchRestaurants(){
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        RestaurantService mRestaurantService = retrofit.create(RestaurantService.class);

        Map<String, String> params = new HashMap<>();
        params.put("key", getResources().getString(R.string.REMOTE_API_KEY));
        params.put("location", "-33.8670522,151.1957362");
        params.put("type", "restaurant");

        Call<Restaurant> mCallRestaurant = mRestaurantService.getPlaces(params);
        mCallRestaurant.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Response<Restaurant> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    Restaurant data = response.body();
                    mRestaurantList = data.getResults();
                    NEXT_PAGE_TOKEN = data.getNextPageToken();
                    mRestaurantEventBus.post(new RestaurantFetchFromDbEvent(mRestaurantList, NEXT_PAGE_TOKEN));
                    Log.d(TAG, String.valueOf(mRestaurantList.size()));
                }else{
                    mRestaurantDbFetchedList = mRealm.where(RestaurantModel.class).findAll();
                    if(mRestaurantDbFetchedList.size() != 0){
                        mRestaurantEventBus.post(new RestaurantEvent(mRestaurantDbFetchedList));
                    }
                    mRestaurantEventBus.post(new RestaurantErrorEvent("Oops! Something went wrong."));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mRestaurantDbFetchedList = mRealm.where(RestaurantModel.class).findAll();
                if(mRestaurantDbFetchedList.size() != 0){
                    mRestaurantEventBus.post(new RestaurantEvent(mRestaurantDbFetchedList));
                }
                mRestaurantEventBus.post(new RestaurantErrorEvent("Please check your internet connection."));
            }
        });
    }

    public void fetchMoreRestaurantsByPageToken(){
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        RestaurantService mRestaurantService = retrofit.create(RestaurantService.class);

        Map<String, String> params = new HashMap<>();
        params.put("key", getResources().getString(R.string.REMOTE_API_KEY));
        params.put("pagetoken", NEXT_PAGE_TOKEN);

        Call<Restaurant> mCallRestaurantByPageToken = mRestaurantService.getPlacesByPageToken(params);
        mCallRestaurantByPageToken.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Response<Restaurant> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    Restaurant data = response.body();
                    mRestaurantList = data.getResults();
                    NEXT_PAGE_TOKEN = data.getNextPageToken();
                    mRestaurantEventBus.post(new RestaurantFetchFromDbEvent(mRestaurantList, NEXT_PAGE_TOKEN));
                }else{
                    mRestaurantDbFetchedList = mRealm.where(RestaurantModel.class).findAll();
                    if(mRestaurantDbFetchedList.size() != 0){
                        mRestaurantEventBus.post(new RestaurantEvent(mRestaurantDbFetchedList));
                    }
                    mRestaurantEventBus.post(new RestaurantErrorEvent("Oops! Something went wrong."));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mRestaurantDbFetchedList = mRealm.where(RestaurantModel.class).findAll();
                if(mRestaurantDbFetchedList.size() != 0){
                    mRestaurantEventBus.post(new RestaurantEvent(mRestaurantDbFetchedList));
                }
                mRestaurantEventBus.post(new RestaurantErrorEvent("Please check your internet connection."));
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mRestaurantEventBus.register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRestaurantEventBus.unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRestaurantEventBus.unregister(this);
    }

    public void onEventMainThread(RestaurantFetchFromDbEvent mEvent){
        mRestaurantList = mEvent.getmRestaurantList();

        final List<RestaurantModel> mRestaurantModelList = new ArrayList<RestaurantModel>();

        for (Result currentItem : mRestaurantList){
            RestaurantModel mRestaurantModel = new RestaurantModel();
            mRestaurantModel.setLatitude(currentItem.getGeometry().getLocation().getLat());
            mRestaurantModel.setLongitude(currentItem.getGeometry().getLocation().getLng());
            mRestaurantModel.setRating(currentItem.getRating());
            mRestaurantModel.setName(currentItem.getName());
            mRestaurantModel.setPlace_id(currentItem.getPlaceId());
            mRestaurantModel.setVicinity(currentItem.getVicinity());
            mRestaurantModel.setReference(currentItem.getReference());
            mRestaurantModel.setPrice_level(currentItem.getPrice_level());

            List<Photo> mRestaurantPhotoList = currentItem.getPhotos();
            RealmList<RestaurantPhotoModel> mRestaurantPhotosList = new RealmList<RestaurantPhotoModel>();
            for (Photo currentPhotoItem : mRestaurantPhotoList){
                RestaurantPhotoModel mRestaurantPhotoModel = new RestaurantPhotoModel();
                mRestaurantPhotoModel.setHeight(currentPhotoItem.getHeight());
                mRestaurantPhotoModel.setWidth(currentPhotoItem.getWidth());
                mRestaurantPhotoModel.setPhoto_ref(currentPhotoItem.getPhotoReference());
                mRestaurantPhotosList.add(mRestaurantPhotoModel);
            }

            mRestaurantModel.setPhoto_references(mRestaurantPhotosList);
            mRestaurantModelList.add(mRestaurantModel);
        }

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(mRestaurantModelList);
            }
        }, new Realm.Transaction.OnSuccess(){

            @Override
            public void onSuccess() {
                mRestaurantDbFetchedList = mRealm.where(RestaurantModel.class).findAll();
                Log.d(TAG, "DB Fetched Length : " + String.valueOf(mRestaurantDbFetchedList.size()));
                mRestaurantEventBus.post(new RestaurantEvent(mRestaurantDbFetchedList));
            }
        });
    }

    public void onEventMainThread(RestaurantEvent mEvent){
        mRestaurantDbFetchedList = mEvent.getmRestaurantList();
        mRestaurantAdapter = new RestaurantRVAdapter(mRestaurantDbFetchedList, mMainActivityCL);
        mRestaurantsRV.setAdapter(mRestaurantAdapter);
        mRestaurantAdapter.notifyItemRangeChanged(0, mRestaurantDbFetchedList.size());
        if(firstVisibleItem != null){
            mRestaurantsRV.scrollToPosition(firstVisibleItem);
        }
    }

    public void onEventMainThread(RestaurantErrorEvent mEvent){
        Snackbar mSnackbar = Snackbar
                .make(mMainActivityCL, mEvent.getMessage(), Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fetchRestaurants();
                        Log.d(TAG, "RETRY");
                    }
                });
        View mSnackbarView = mSnackbar.getView();
        mSnackbarView.setBackgroundColor(Color.RED);
        mSnackbar.setActionTextColor(Color.WHITE);
        mSnackbar.show();
    }
}
