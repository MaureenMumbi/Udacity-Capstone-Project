package com.easyoffer.android.easyofferapp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.easyoffer.android.easyofferapp.R;
import com.easyoffer.android.easyofferapp.fragments.CategoriesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mauryn on 4/16/2018.
 */

public class CategoryDetailActivity extends AppCompatActivity {
    public static String EXTRA_CATEGORY_KEY = "EXTRA_CATEGORY_KEY";
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    private String getExtraCategoryKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        ButterKnife.bind(this);

        if (savedInstanceState == null) {

            getExtraCategoryKey = getIntent().getStringExtra(EXTRA_CATEGORY_KEY);


            Bundle bundle = getIntent().getExtras();


            FragmentManager fragmentManager = getSupportFragmentManager();
            final CategoriesFragment categoryDetailFragment = new CategoriesFragment();
            categoryDetailFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.category_fragment_container, categoryDetailFragment).addToBackStack(null)
                    .commit();//.addToBackStack(null).commit();

        }


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getExtraCategoryKey);
        Log.i("Category Title", getExtraCategoryKey);


    }

}
