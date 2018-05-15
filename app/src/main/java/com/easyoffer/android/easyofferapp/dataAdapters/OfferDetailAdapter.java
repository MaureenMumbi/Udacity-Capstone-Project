package com.easyoffer.android.easyofferapp.dataAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easyoffer.android.easyofferapp.R;
import com.easyoffer.android.easyofferapp.models.Items;
import com.easyoffer.android.easyofferapp.viewholders.OfferItemsViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


/**
 * Created by Mauryn on 4/11/2018.
 */


public class OfferDetailAdapter extends RecyclerView.Adapter<OfferItemsViewHolder> {
    private static String TAG = "OfferDetailAdapter";
    private static OfferItemOnClickHandler mofferItemOnClickHandler;
    private Context mContext;
    private DatabaseReference mDatabaseReference;
    private StorageReference storageReference;
    private Query query;
    private ValueEventListener mItemOfferListener;
    private ArrayList<Items> ItemsOfferList = new ArrayList<>();


    public OfferDetailAdapter(Context context, String offer_key, OfferItemOnClickHandler offerItemOnclickHandler) {


        mofferItemOnClickHandler = offerItemOnclickHandler;
        mContext = context;
        DatabaseReference offeritemsReference = FirebaseDatabase.getInstance().getReference()
                .child("offers").child("items");

        storageReference = FirebaseStorage.getInstance().getReference();

        query = offeritemsReference.orderByChild("offer").equalTo(offer_key);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Items items = dataSnapshot.getValue(Items.class);

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Items offer = ds.getValue(Items.class);
                    ItemsOfferList.add(offer);

                }

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e(TAG, "Error occurred: " + firebaseError.getMessage());
            }
        });


    }

    @Override
    public OfferItemsViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {


        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.offer_item_details;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachtoParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachtoParentImmediately);
        OfferItemsViewHolder itemofferViewHolder = new OfferItemsViewHolder(view, context, storageReference, mofferItemOnClickHandler);


        return itemofferViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull OfferItemsViewHolder holder, final int position) {
        holder.bindToOfferItems(ItemsOfferList, position);


    }

    @Override
    public int getItemCount() {
        return ItemsOfferList.size();
    }

    //
    public interface OfferItemOnClickHandler {
        void OnClickListener(Items items);
    }


}
