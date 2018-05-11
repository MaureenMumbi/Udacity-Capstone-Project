package com.easyoffer.android.easyofferapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.easyoffer.android.easyofferapp.R;
import com.easyoffer.android.easyofferapp.fragments.SearchResultsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mauryn on 4/30/2018.
 */

public class SearchResultsActivity extends AppCompatActivity {

    public static final String EXTRA_SEARCH_QUERY= "search_query";
    private String query;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;



    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        ButterKnife.bind(this);
        query = getIntent().getStringExtra(EXTRA_SEARCH_QUERY);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {


            Bundle bundle = getIntent().getExtras();

            FragmentManager fragmentManager = getSupportFragmentManager();
            final SearchResultsFragment searchFragment = new SearchResultsFragment();
            searchFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id
                    .search_fragment_container, searchFragment).addToBackStack(null)
                    .commit();


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.action_settings) {
//            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
//            startActivity(startSettingsActivity);
//            return true;
//        } else
            if (id == R.id.action_favorites) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);


    }
}
