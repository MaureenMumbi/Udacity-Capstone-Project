package com.easyoffer.android.easyofferapp.viewholders;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.easyoffer.android.easyofferapp.R;
import com.easyoffer.android.easyofferapp.models.Offers;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mauryn on 4/9/2018.
 */

public class OfferViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.outletname)
    TextView outletnameTextView;
    @BindView(R.id.description)
    TextView descriptionTextView;
    @BindView(R.id.offerthumbnail)
    ImageView thumbnailImageView;


    TextView getDescriptionTextView;

    public OfferViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        //   getDescriptionTextView = itemView.findViewById(R.id.description);
    }

    public void bindToOffer(Offers offers, final Context context, StorageReference storageReference) {
        descriptionTextView.setText(offers.description);
        outletnameTextView.setText(offers.outletname);


        storageReference.child("images/" + offers.imageURL);
        if (offers.imageURL != null) {

            storageReference.child("images/" + offers.imageURL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
        thumbnailImageView.setContentDescription(offers.outletname);
    }
}
