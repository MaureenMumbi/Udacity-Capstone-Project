package com.easyoffer.android.easyofferapp.ui;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.easyoffer.android.easyofferapp.R;
import com.easyoffer.android.easyofferapp.data.FavoritesHelper;
import com.easyoffer.android.easyofferapp.data.Offer;
import com.easyoffer.android.easyofferapp.data.RefresherService;
import com.easyoffer.android.easyofferapp.fragments.AllOffersFragment;
import com.easyoffer.android.easyofferapp.fragments.FeaturedOffersFragment;
import com.easyoffer.android.easyofferapp.fragments.OfferCategoriesFragment;
import com.easyoffer.android.easyofferapp.idlingResource.SimpleIdlingResource;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.easyoffer.android.easyofferapp.data.FavoriteOfferWidget.FAVOURITE_OFFER_LIST;
import static com.easyoffer.android.easyofferapp.ui.SearchResultsActivity.EXTRA_SEARCH_QUERY;

public class MainActivity extends AppCompatActivity {


    @Nullable
    private SimpleIdlingResource simpleIdlingResource;

    @VisibleForTesting
    @Nullable
    public IdlingResource getIdlingResource(){
        if(simpleIdlingResource == null){
            simpleIdlingResource = new SimpleIdlingResource();

        }
        return simpleIdlingResource;
    }


    String TAG = MainActivity.class.getSimpleName();
    private FragmentPagerAdapter mPagerAdapter;
    private ValueEventListener mPostListener;
    private AdView mAdView;
    private FirebaseAnalytics mFirebaseAnalytics;
    @BindView(R.id.vpcontainer)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    private Menu mMenu;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.main_activity_container)
    View containerview;
    @BindView(R.id.adView)
    AdView getmAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//


        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[]{
                    new FeaturedOffersFragment(),
                    new AllOffersFragment(),
                    new OfferCategoriesFragment()//,
                    //new FavoritedFragment()

            };

            private final String[] mFragmentNames = new String[]{
                    getString(R.string.featured),
                    getString(R.string.alloffers),
                    getString(R.string.categories) //,
                    //"Favorites"
            };

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }


            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }

        };

        setSupportActionBar(toolbar);
//        handleIntent(getIntent(), savedInstanceState);
        mViewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        MobileAds.initialize(this, String.valueOf(R.string.SAMPLE_AD_MOB_ID));
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        AdRequest adRequest = new AdRequest.Builder().build();
        getmAdView.loadAd(adRequest);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        if (IsConnected()) {
            Log.i(TAG, "internet connection present.");

        } else {
            Log.e(TAG, "Please check your internet connection.");
            Snackbar
                    .make(containerview, "No network connection.", Snackbar.LENGTH_INDEFINITE).
                    setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refresh();
                            IsConnected();
                        }
                    }).show();

        }
        if (savedInstanceState == null) {
            refresh();
        }


    }


    private void refresh() {
        startService(new Intent(this, RefresherService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mRefreshingReceiver,
                new IntentFilter(RefresherService.BROADCAST_ACTION_STATE_CHANGE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mRefreshingReceiver);
    }

    private boolean mIsRefreshing = false;


    private BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (RefresherService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                mIsRefreshing = intent.getBooleanExtra(RefresherService.EXTRA_REFRESHING, false);

                updateRefreshingUI();
            }
        }
    };

    private void updateRefreshingUI() {
        mSwipeRefreshLayout.setRefreshing(mIsRefreshing);
    }


    private boolean IsConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;


    }


    private void handleIntent(Intent intent, Bundle bundle) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow

            Intent search_intent = new Intent(this, SearchResultsActivity.class);
            search_intent.putExtra(EXTRA_SEARCH_QUERY, query);
            //    bundle.putString(EXTRA_SEARCH_QUERY,query);
            startActivity(search_intent);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        mMenu = menu;


        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.action_settings) {
//
//
//            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
//            startActivity(startSettingsActivity);
//            return true;
//        } else
            if (id == R.id.action_favorites) {

            Intent intent = new Intent(this, FavoritesActivity.class);
            ArrayList<Offer> favs = FavoritesHelper.getFavorites(this);
            intent.putExtra(FAVOURITE_OFFER_LIST,favs );
            startActivity(intent);
;
            return true;

        }

        return super.onOptionsItemSelected(item);


    }


}
