package com.easyoffer.android.easyofferapp.fragments;


import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.easyoffer.android.easyofferapp.R;
import com.easyoffer.android.easyofferapp.ui.ThreeTwoImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfferItemDetailFragment extends Fragment {


    public OfferItemDetailFragment() {
        // Required empty public constructor
    }

    private StorageReference mStorageRef;

    //    @BindView(R.id.detailthumbnailURL)
//    ImageView thumbnailImageView;
    @BindView(R.id.detailthumbnailURL)
    ThreeTwoImageView thumbnailImageView;

    @BindView(R.id.item_name)
    TextView nameTextView;
    @BindView(R.id.item_description)
    TextView descriptionTextView;
    @BindView(R.id.item_cost)
    TextView costTextView;
    @BindView(R.id.item_discountedcost)
    TextView discountedCostTextView;
    @BindView(R.id.empty_item)
    TextView emptyTextView;
    @BindView(R.id.share)
    ImageButton shareButton;
    @BindView(R.id.adView)
    AdView getmAdView;

    private String EXTRA_CLICKED_ITEM = "clicked_item";
    private HashMap<String, String> clickeditem = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_offer_item_detail, container, false);
        ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            clickeditem = (HashMap<String, String>) savedInstanceState.getSerializable(EXTRA_CLICKED_ITEM);


        } else {
            clickeditem = (HashMap<String, String>) getArguments().getSerializable(EXTRA_CLICKED_ITEM);

        }

        MobileAds.initialize(getActivity(), String.valueOf(R.string.SAMPLE_AD_MOB_ID));
        AdView adView = new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        AdRequest adRequest = new AdRequest.Builder().build();
        getmAdView.loadAd(adRequest);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (clickeditem != null) {
            nameTextView.setText(clickeditem.get("name"));
            descriptionTextView.setText(clickeditem.get("description"));

            mStorageRef = FirebaseStorage.getInstance().getReference();
            mStorageRef.child("thumbnails/" + clickeditem.get("thumbnailURL"));
            if (clickeditem.get("thumbnailURL") != null) {

                mStorageRef.child("thumbnails/" + clickeditem.get("thumbnailURL")).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Picasso.with(getContext()).load(String.valueOf(uri)).into(thumbnailImageView);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
//
            }


            Double percentageDiscount = 0.0;
            Double diff = 0.0;
            if (clickeditem.get("offerType").equals("percentagediscount")) {
                Long discountedCost = Long.parseLong(clickeditem.get("initialCost"));
                Long initialCost = Long.parseLong(clickeditem.get("finalCost"));

                diff = Double.parseDouble(String.valueOf(initialCost - discountedCost));

                percentageDiscount = (diff / initialCost) * 100;
                discountedCostTextView.setText("Kshs " + clickeditem.get("finalCost") + "  (" + Math.round(percentageDiscount) + "% off )");
                costTextView.setText("Kshs. " + clickeditem.get("initialCost"));
                costTextView.setPaintFlags(costTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            } else {

                discountedCostTextView.setText("Kshs " + clickeditem.get("finalCost"));

            }

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Great Offer");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, String.format("%s %n %s at Kshs. %s %n", clickeditem.get("name"), clickeditem.get("description"), clickeditem.get("finalCost")));
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            });

        } else {

            emptyTextView.setText("No item has been clicked ");

        }
    }


    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);

        currentState.putSerializable(EXTRA_CLICKED_ITEM, clickeditem);

    }

}
