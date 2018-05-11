package com.easyoffer.android.easyofferapp.data;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Mauryn on 5/11/2018.
 */


//https://stackoverflow.com/questions/37448186/setpersistenceenabledtrue-crashes-app
public class MApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
