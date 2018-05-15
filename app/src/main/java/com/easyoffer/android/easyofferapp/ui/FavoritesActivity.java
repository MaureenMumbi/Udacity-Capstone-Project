package com.easyoffer.android.easyofferapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.easyoffer.android.easyofferapp.R;
import com.easyoffer.android.easyofferapp.fragments.FavoriteFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mauryn on 4/27/2018.
 */

public class FavoritesActivity extends AppCompatActivity {

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    private Menu mMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_offer);

        ButterKnife.bind(this);


        if (savedInstanceState == null) {


            Bundle bundle = getIntent().getExtras();
            FragmentManager fragmentManager = getSupportFragmentManager();
            final FavoriteFragment favoriteFragment = new FavoriteFragment();
            favoriteFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.favorite_fragment_container, favoriteFragment).addToBackStack(null)
                    .commit();
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        mMenu = menu;
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
