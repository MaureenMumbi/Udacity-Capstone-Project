package com.easyoffer.android.easyofferapp.data;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.easyoffer.android.easyofferapp.R;
import com.easyoffer.android.easyofferapp.ui.FavoritesActivity;
import com.easyoffer.android.easyofferapp.ui.OfferDetailActivity;

import java.util.ArrayList;

import static com.easyoffer.android.easyofferapp.data.OfferListService.FavOfferList;

/**
 * Created by Mauryn on 5/4/2018.
 */


public class FavoriteOfferWidget extends AppWidgetProvider {

    static public String FAVOURITE_OFFER_LIST = "FAVOURITE_OFFER_LIST";


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                ArrayList<Offer> favlist, int appWidgetId) {

//        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
//        options.
        RemoteViews rv;
        rv = getFavoriteOfferRemoteView(context, favlist, appWidgetId);


        appWidgetManager.updateAppWidget(appWidgetId, rv);
//
//        Intent intent = new Intent(context, FavoritesActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);
//        // Update image
//        views.setImageViewResource(R.id.widget_plant_image, imgRes);
//        // Widgets allow click handlers to only launch pending intents
//        views.setOnClickPendingIntent(R.id.favlist_view, pendingIntent);
//
//        appWidgetManager.updateAppWidget(appWidgetId, views);


//        Log.e("Fav List Size", "in Update widget " + favlist.size());
//
//        // Create an Intent to launch MainActivity when clicked
//        Intent intent = new Intent(context, FavoritesActivity.class);
//        intent.putExtra(FAVOURITE_OFFER_LIST, favlist);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view_item);
//        // Update image
//        views.setTextViewText(R.id.favlistview_item, favlist.get(0).outletname);
//        // Widgets allow click handlers to only launch pending intents
//        views.setOnClickPendingIntent(R.id.favlistview_item, pendingIntent);

        // Add the wateringservice click handler
//        Intent wateringIntent = new Intent(context, OfferListService.class);
//        wateringIntent.setAction(OfferListService.ACTION_LIST_FAVORITES);
//        PendingIntent wateringPendingIntent = PendingIntent.getService(context, 0, wateringIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        views.setOnClickPendingIntent(R.id.widget_water_button, wateringPendingIntent);
        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);


    }

    public static void updateOfferWidgets(Context context, AppWidgetManager appWidgetManager,
                                          ArrayList<Offer> offerList, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, offerList, appWidgetId);
        }
    }

    private static RemoteViews getFavoriteOfferRemoteView(Context context, ArrayList<Offer> offerList, int appWidgetId) {
        // Set the click handler to open the DetailActivity if a recipe has been clicked  plant ID,
        // or the MainActivity if no recipe has been clicked
        Intent intent;

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);
        if (offerList == null || offerList.size() == 0) {
            Intent configIntent = new Intent(context, FavoritesActivity.class);

            PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);

            views.setOnClickPendingIntent(R.id.favlist_view, configPendingIntent);


            // should open FavoriteActivity home
        } else { // Set on click to open the corresponding detail activity

            // Set the ListWidgetService intent to act as the adapter for the GridView
            intent = new Intent(context, ListWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(
                    intent.toUri(Intent.URI_INTENT_SCHEME)));
            views.setRemoteAdapter(appWidgetId, R.id.favlist_view, intent);
            views.setEmptyView(R.id.favlist_view, R.id.empty_view);


            // Intent appIntent = new Intent(context, FavoritesActivity.class);
            //  appIntent.putExtra(FAVOURITE_OFFER_LIST, offerList);
            //PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0);
            //   views.setOnClickPendingIntent(R.id.favlist_view, appPendingIntent);
            Intent titleIntent = new Intent(context, FavoritesActivity.class);
            PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
            views.setOnClickPendingIntent(R.id.widgetTitleLabel, titlePendingIntent);


            Intent clickIntentTemplate = new Intent(context, OfferDetailActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.favlist_view, clickPendingIntentTemplate);


        }

        return views;

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Start the intent service update widget action, the service takes care of updating the widgets UI

        OfferListService.startActionUpdateFavoritesWidgets(context);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        OfferListService.startActionShowFavorites(context, FavOfferList);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Perform any action when an AppWidget for this provider is instantiated
    }

    @Override
    public void onDisabled(Context context) {
        // Perform any action when the last AppWidget instance for this provider is deleted
    }


}


