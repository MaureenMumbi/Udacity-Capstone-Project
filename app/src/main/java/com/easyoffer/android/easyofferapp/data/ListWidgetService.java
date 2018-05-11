package com.easyoffer.android.easyofferapp.data;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.easyoffer.android.easyofferapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.easyoffer.android.easyofferapp.data.OfferListService.FavOfferList;
import static com.easyoffer.android.easyofferapp.data.OfferListService.favoriteOfferList;


public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;

    ArrayList<String> remoteViewfavoriteList;

    public ListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;

    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        remoteViewfavoriteList = favoriteOfferList;


    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return remoteViewfavoriteList.size();

    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_view_item);


        views.setTextViewText(R.id.favlistview_item, remoteViewfavoriteList.get(position));


        Intent fillInIntent = new Intent();
        HashMap<String, String> offer = new HashMap<>();
        offer.put("id", String.valueOf(FavOfferList.get(position).offer_id));
        offer.put("outletname", FavOfferList.get(position).outletname);
        offer.put("description", FavOfferList.get(position).description);
        offer.put("startdate", FavOfferList.get(position).startdate);
        offer.put("enddate", FavOfferList.get(position).enddate);
        offer.put("imageUrl", FavOfferList.get(position).imageUrl);
        offer.put("offerKey", FavOfferList.get(position).offerKey);
        fillInIntent.putExtra("offer_details", offer);
        views.setOnClickFillInIntent(R.id.favlistview_item, fillInIntent);


        return views;


    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

