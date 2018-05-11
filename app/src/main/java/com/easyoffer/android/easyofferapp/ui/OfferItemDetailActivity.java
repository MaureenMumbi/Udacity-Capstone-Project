package com.easyoffer.android.easyofferapp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.easyoffer.android.easyofferapp.R;
import com.easyoffer.android.easyofferapp.fragments.OfferItemDetailFragment;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mauryn on 5/3/2018.
 */


public class OfferItemDetailActivity extends AppCompatActivity {
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    private HashMap<String, String> clicked_item = new HashMap<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        ButterKnife.bind(this);


        if (savedInstanceState == null) {

            Bundle bundle = getIntent().getExtras();
            clicked_item = (HashMap<String, String>) getIntent().getSerializableExtra("clicked_item");

//
            FragmentManager fragmentManager = getSupportFragmentManager();
            final OfferItemDetailFragment itemofferDetailFragment = new OfferItemDetailFragment();
            itemofferDetailFragment.setArguments(bundle);
            fragmentManager.beginTransaction().add(R.id.itemdetail_fragment_container, itemofferDetailFragment).commit(); //.addToBackStack(null)


        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(clicked_item.get("name"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://stackoverflow.com/questions/10863572/programmatically-go-back-to-the-previous-fragment-in-the-backstack
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 1) {
//                    if (findViewById(R.id.ite)==null) {

                        fragmentManager.popBackStack();//}
//                    else{
//                        finish();
//                    }
                } else {
                    finish();
                }



            }
        });


    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("offer_details", clicked_item);
    }
}
