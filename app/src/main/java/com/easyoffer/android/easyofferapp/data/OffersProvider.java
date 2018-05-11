package com.easyoffer.android.easyofferapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Mauryn on 4/19/2018.
 */

public class OffersProvider extends ContentProvider {
    private static UriMatcher sUriMatcher = buildUriMatcher();
    private OfferDbHelper mOpenHelper;
    public static final int OFFER = 100;
    public static final int OFFER_ID = 101;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = OfferContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, OfferContract.PATH_OFFER, OFFER);
        matcher.addURI(authority, OfferContract.PATH_OFFER + "/#", OFFER_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new OfferDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        final int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        switch (match) {
            case OFFER: {
                builder.setTables(OfferContract.OfferEntry.TABLE_NAME);
                break;

            }
            case OFFER_ID: {
                builder.setTables(OfferContract.OfferEntry.TABLE_NAME);
                builder.appendWhere(OfferContract.OfferEntry.COLUMN_OFFER_ID + " = " + uri.getLastPathSegment());
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }

        retCursor = builder.query(db,projection,selection,selectionArgs,null,null,null);
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (sUriMatcher.match(uri)) {
            case OFFER:
                return OfferContract.OfferEntry.CONTENT_TYPE;
            case OFFER_ID:
                return OfferContract.OfferEntry.CONTENT_TYPE_ITEM;
            default:
                return null;
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case OFFER: {
                long _id = db.insert(OfferContract.OfferEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = OfferContract.OfferEntry.buildOfferUri(_id);
                }
                else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) {
            selection = "1";
        }
        switch (sUriMatcher.match(uri)) {
            case OFFER:
                rowsDeleted = db.delete(
                        OfferContract.OfferEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numRowsUpdated;

        switch (sUriMatcher.match(uri)) {
            case OFFER:
                numRowsUpdated = db.update(OfferContract.OfferEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsUpdated;
    }
}


