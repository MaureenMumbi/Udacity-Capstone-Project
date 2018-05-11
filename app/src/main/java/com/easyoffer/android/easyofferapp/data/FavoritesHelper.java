package com.easyoffer.android.easyofferapp.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mauryn on 4/25/2018.
 */

public class FavoritesHelper {


    public void addToFavorites(ContentResolver resolver,HashMap<String, String> offer)  {

        Uri uri = OfferContract.OfferEntry.CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.clear();
        contentValues.put(OfferContract.OfferEntry.COLUMN_OFFER_ID, offer.get("id"));
        contentValues.put(OfferContract.OfferEntry.COLUMN_DESCRIPTION, offer.get("description"));
        contentValues.put(OfferContract.OfferEntry.COLUMN_OUTLETNAME, offer.get("outletname"));
        contentValues.put(OfferContract.OfferEntry.COLUMN_STARTDATE, offer.get("startdate"));
        contentValues.put(OfferContract.OfferEntry.COLUMN_ENDDATE, offer.get("enddate"));
        contentValues.put(OfferContract.OfferEntry.COLUMN_IMAGEURL, offer.get("imageUrl"));
        contentValues.put(OfferContract.OfferEntry.COLUMN_OFFERKEY, offer.get("offerKey"));


        resolver.insert(uri, contentValues);
    }


    public void deleteFromFavorites(ContentResolver resolver,long offerid) {

        Uri uri = OfferContract.OfferEntry.CONTENT_URI;

        long noDeleted = resolver.delete(uri,
                OfferContract.OfferEntry.COLUMN_OFFER_ID + " = ? ",
                new String[]{ offerid + "" });

    }


    public boolean queryFavorites(ContentResolver resolver,long offerid) {

        Uri uri = OfferContract.OfferEntry.buildOfferUri(offerid);
        Cursor cursor = null;

        try {
            cursor = resolver.query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                return true;
            }

        } finally {

            if(cursor != null)
                cursor.close();

        }

        return false;
    }

    public static ArrayList<Offer> getFavorites(Context context){
        ArrayList<Offer> offerData = new ArrayList<>();
        Uri uri = OfferContract.OfferEntry.CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;

        try {

            cursor = resolver.query(uri, null, null, null, null);

            // clear movies
            offerData.clear();

            if (cursor.moveToFirst()){
                do {

                    //int offer_id, String description, String outletname, String startdate,String enddate, String imageUrl, String offerKey
                    Offer offer = new Offer(cursor.getInt(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5),
                            cursor.getString(6),cursor.getString(7));

                    offerData.add(offer);

                } while (cursor.moveToNext());
            }

        } finally {

            if(cursor != null)
                cursor.close();

        }
        return offerData;
    }


}
