package com.easyoffer.android.easyofferapp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.easyoffer.android.easyofferapp.R;
import com.easyoffer.android.easyofferapp.fragments.OfferDetailFragment;

import butterknife.ButterKnife;

/**
 * Created by Mauryn on 4/11/2018.
 */

public class OfferDetailActivity extends AppCompatActivity {

    public static final String EXTRA_OFFER_KEY = "offer_key";
    Bundle bundle;
    private String offer_key;
//    static boolean mTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);

        ButterKnife.bind(this);


        if (savedInstanceState == null) {

            offer_key = getIntent().getStringExtra(EXTRA_OFFER_KEY);

            bundle = getIntent().getExtras();
            FragmentManager fragmentManager = getSupportFragmentManager();
            final OfferDetailFragment offerDetailFragment = new OfferDetailFragment();
            offerDetailFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.detail_fragment_container, offerDetailFragment).commit();//.addToBackStack(null).commit();

        }
    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);

        currentState.putBundle("offer_details", bundle);

    }


}
