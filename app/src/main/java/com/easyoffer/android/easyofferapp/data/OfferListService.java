package com.easyoffer.android.easyofferapp.data;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.easyoffer.android.easyofferapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.easyoffer.android.easyofferapp.data.FavoriteOfferWidget.FAVOURITE_OFFER_LIST;

/**
 * Created by Mauryn on 5/4/2018.
 */

public class OfferListService extends IntentService {

    public static final String ACTION_LIST_FAVORITES = "com.easyoffer.android.easyofferapp.action.list_favorites";
    public static final String ACTION_UPDATE_FAVORITE_WIDGETS = "com.easyoffer.android.easyofferapp.action.update_listfav_widgets";
    public static HashMap<String, String> favoriteOfferHashMap = new HashMap<>();
    public static ArrayList<String> favoriteOfferList = new ArrayList<>();
    /**
     * Handle action UpdateFavsWidgets in the provided background thread
     */
    static ArrayList<Offer> FavOfferList = new ArrayList<>();

    public OfferListService() {
        super("OfferListService");
    }

    public static void startActionShowFavorites(Context context, ArrayList<Offer> offerList) {


        Intent intent = new Intent(context, OfferListService.class);
        intent.setAction(ACTION_LIST_FAVORITES);
        intent.putExtra(FAVOURITE_OFFER_LIST, offerList);

        context.startService(intent);

    }

    /**
     * Starts this service to perform UpdateFavsWidgets action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateFavoritesWidgets(Context context) {
        Intent intent = new Intent(context, OfferListService.class);
        intent.setAction(ACTION_UPDATE_FAVORITE_WIDGETS);
        context.startService(intent);
    }

    /**
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            final String action = intent.getAction();
            if (ACTION_UPDATE_FAVORITE_WIDGETS.equals(action)) {
                handleActionUpdateFavoritesWidgets(intent);

            } else if (ACTION_LIST_FAVORITES.equals(action)) {
                handleActionUpdateFavoritesWidgets(intent);

            }
        }
    }

    private void handleActionUpdateFavoritesWidgets(Intent intent) {
        favoriteOfferList = new ArrayList<>();

        // int imgRes = R.drawable.ic_view_list_black_48dp;
        FavOfferList = FavoritesHelper.getFavorites(getApplicationContext());
        String outletname = "";
        String description = "";
        String offerdates = "";

        for (int i = 0; i < FavOfferList.size(); i++) {
            outletname = FavOfferList.get(i).outletname;
            description = FavOfferList.get(i).description;
            offerdates = "Valid From " + FavOfferList.get(i).startdate + " To " + FavOfferList.get(i).enddate;
            favoriteOfferList.add(String.format(" %s %n %s %n %s %n", outletname, description, offerdates));

        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, FavoriteOfferWidget.class));
        //Trigger data update to handle the list widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.favlist_view);
        //Now update all widgets
        FavoriteOfferWidget.updateOfferWidgets(this, appWidgetManager, FavOfferList, appWidgetIds);

    }

}
