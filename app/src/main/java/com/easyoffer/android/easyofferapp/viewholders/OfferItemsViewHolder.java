package com.easyoffer.android.easyofferapp.viewholders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.easyoffer.android.easyofferapp.R;
import com.easyoffer.android.easyofferapp.dataAdapters.OfferDetailAdapter;
import com.easyoffer.android.easyofferapp.models.Items;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mauryn on 4/15/2018.
 */

public class OfferItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    @BindView(R.id.item_description)
    TextView descriptionTextView;

    @BindView(R.id.item_name)
    TextView itemNameTextView;

    @BindView(R.id.detailthumbnailURL)
    ImageView detailthumbnailView;


    @BindView(R.id.item_cost)
    TextView itemCostTextView;

    @BindView(R.id.item_discountedcost)
    TextView itemDiscountTextView;


    @BindView(R.id.share)
    ImageButton shareButton;

    OfferDetailAdapter.OfferItemOnClickHandler mofferItemOnClickHandler;

    ArrayList<Items> offeritems = new ArrayList<>();
    public OfferItemsViewHolder(View itemView, OfferDetailAdapter.OfferItemOnClickHandler offerItemOnClickHandler) {
        super(itemView);
        mofferItemOnClickHandler = offerItemOnClickHandler;
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);



    }


    public void bindToOfferItems(final Context context, ArrayList<Items> items, int position, StorageReference storageReference) {
        offeritems = items;
        final Items itemdetail = items.get(position);

        itemNameTextView.setText(itemdetail.name);
        descriptionTextView.setText(itemdetail.description);
        Double percentageDiscount = 0.0;
        Double diff = 0.0;
        if (itemdetail.offerType.equals("percentagediscount")) {
            Long discountedCost = itemdetail.finalCost;
            Long initialCost = itemdetail.initialCost;

            diff = Double.parseDouble(String.valueOf(initialCost - discountedCost));

            percentageDiscount = (diff / initialCost)*100;
            itemDiscountTextView.setText("Kshs " + itemdetail.finalCost + "  (" +Math.round(percentageDiscount)+"% off )");
            itemCostTextView.setText("Kshs. " + itemdetail.initialCost);
            itemCostTextView.setPaintFlags(itemCostTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        } else {

            itemDiscountTextView.setText("Kshs " + itemdetail.finalCost);

        }
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Great Offer");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, String.format("%s %n %s at Kshs. %s %n",itemdetail.name, itemdetail.description, itemdetail.finalCost));
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });


        // comment_body.setText(items.get(y).thumbnailURL);

        //  }
//        descriptionTextView.setText(offers.description);
//        outletnameTextView.setText(offers.outletname );


        storageReference.child("thumbnails/" + itemdetail.thumbnailURL);
        if (itemdetail.thumbnailURL != null) {

            storageReference.child("thumbnails/" + itemdetail.thumbnailURL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context).load(String.valueOf(uri)).into(detailthumbnailView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
//
        }
        detailthumbnailView.setContentDescription(itemdetail.name);
    }

    @Override
    public void onClick(View view) {

        int adapterposition = getAdapterPosition();
        Items clickedOfferItem = offeritems.get(adapterposition);
        mofferItemOnClickHandler.OnClickListener(clickedOfferItem);

    }


}
