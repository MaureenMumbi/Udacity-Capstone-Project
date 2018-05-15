package com.easyoffer.android.easyofferapp.dataAdapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easyoffer.android.easyofferapp.R;
import com.easyoffer.android.easyofferapp.data.Offer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mauryn on 4/27/2018.
 */

public class FavoriteOfferAdapter extends RecyclerView.Adapter<FavoriteOfferAdapter.FavoriteOfferItemsViewHolder> {
    private static String TAG = "FavoriteOfferAdapter";
    private static ArrayList<Offer> FavoriteOfferList = new ArrayList<>();
    private static GridItemOnClickHandler mgridItemOnClickHandler;
    private Context mContext;
    private StorageReference storageReference;


    //public FavoriteOfferAdapter(FavoriteFragment favoriteFragment, Context context, String offer_key) {
    public FavoriteOfferAdapter(Context context, ArrayList<Offer> favs, GridItemOnClickHandler gridItemOnClickHandler) {
        mgridItemOnClickHandler = gridItemOnClickHandler;
        storageReference = FirebaseStorage.getInstance().getReference();

        mContext = context;
        FavoriteOfferList = favs;

    }


    @Override
    public FavoriteOfferItemsViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {


        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_offers;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachtoParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachtoParentImmediately);
        FavoriteOfferItemsViewHolder favitemofferViewHolder = new FavoriteOfferItemsViewHolder(view);


        return favitemofferViewHolder;


    }


    @Override
    public void onBindViewHolder(@NonNull FavoriteOfferItemsViewHolder holder, int position) {
        holder.favbindToOfferItems(mContext, FavoriteOfferList, position, storageReference);
    }

    @Override
    public int getItemCount() {
        return FavoriteOfferList.size();
    }


    public interface GridItemOnClickHandler {
        void OnClickListener(Offer offer);
    }

    public static class FavoriteOfferItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.outletname)
        TextView outletnameTextView;
        @BindView(R.id.description)
        TextView descriptionTextView;
        @BindView(R.id.offerthumbnail)
        ImageView thumbnailImageView;


        public FavoriteOfferItemsViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }


        public void favbindToOfferItems(final Context context, ArrayList<Offer> offers, int position, StorageReference storageReference) {

            Offer offer = offers.get(position);

            outletnameTextView.setText(offer.outletname);
            descriptionTextView.setText(offer.description);


            storageReference.child("images/" + offer.imageUrl);
            if (offer.imageUrl != null) {

                storageReference.child("images/" + offer.imageUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(context).load(String.valueOf(uri)).into(thumbnailImageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

            }
        }


        @Override
        public void onClick(View view) {
            int adapterposition = getAdapterPosition();
            Offer clickedOffer = FavoriteOfferList.get(adapterposition);
            mgridItemOnClickHandler.OnClickListener(clickedOffer);


        }
    }


}

