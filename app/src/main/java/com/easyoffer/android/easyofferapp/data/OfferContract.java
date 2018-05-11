package com.easyoffer.android.easyofferapp.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mauryn on 4/19/2018.
 */

public class OfferContract {
    public static final String CONTENT_AUTHORITY = "com.easyoffer.android.easyofferapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_OFFER = "offer";

    public static final class OfferEntry implements BaseColumns {


        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_OFFER;
        public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_OFFER;
        public static final String TABLE_NAME = "offer";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_OFFER).build();
        public static final String COLUMN_OFFER_ID = "offer_id";
        public static final String COLUMN_DESCRIPTION = "offer_description";
        public static final String COLUMN_STARTDATE = "offer_startdate";
        public static final String COLUMN_ENDDATE = "offer_enddate";
        public static final String COLUMN_IMAGEURL = "offer_imageurl";
        public static final String COLUMN_OUTLETNAME = "offer_outletname";
        public static final String COLUMN_OFFERKEY = "offer_key";

        public static Uri buildOfferUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
