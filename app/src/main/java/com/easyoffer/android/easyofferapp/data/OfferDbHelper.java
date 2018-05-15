package com.easyoffer.android.easyofferapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mauryn on 4/19/2018.
 */

public class OfferDbHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "offer.db";
    private static final int DATABASE_VERSION = 1;

    public OfferDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        final String SQL_CREATE_OFFER_TABLE = "CREATE TABLE " + OfferContract.OfferEntry.TABLE_NAME + " (" +
                OfferContract.OfferEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                OfferContract.OfferEntry.COLUMN_OFFER_ID + " INTEGER UNIQUE NOT NULL, " +
                OfferContract.OfferEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                OfferContract.OfferEntry.COLUMN_OUTLETNAME + " TEXT NOT NULL, " +
                OfferContract.OfferEntry.COLUMN_STARTDATE + " TEXT NOT NULL, " +
                OfferContract.OfferEntry.COLUMN_ENDDATE + " TEXT NOT NULL, " +
                OfferContract.OfferEntry.COLUMN_IMAGEURL + " TEXT NOT NULL, " +
                OfferContract.OfferEntry.COLUMN_OFFERKEY + " TEXT NOT NULL, " +
                " UNIQUE (" + OfferContract.OfferEntry._ID + ") ON CONFLICT IGNORE );";


        sqLiteDatabase.execSQL(SQL_CREATE_OFFER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OfferContract.OfferEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

//    private static final int DATABASE_VERSION = 1;
//
//    static final String DATABASE_NAME = "movie.db";
//
//
//    public MovieDbHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
//                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
//                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
//                MovieContract.MovieEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
//                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
//                MovieContract.MovieEntry.COLUMN_RATING + " INTEGER NOT NULL, " +
//                MovieContract.MovieEntry.COLUMN_DATE + " TEXT NOT NULL,"+
//                MovieContract.MovieEntry.COLUMN_REVIEWS + " TEXT NOT NULL,"+
//                MovieContract.MovieEntry.COLUMN_TRAILERS + " TEXT NOT NULL," +
//                "UNIQUE (" +MovieContract.MovieEntry._ID +") ON CONFLICT IGNORE"+
//                " );";
//        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
//
//    }
//
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
//        onCreate(sqLiteDatabase);
//
//    }