package com.easyoffer.android.easyofferapp.fragments;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easyoffer.android.easyofferapp.R;
import com.easyoffer.android.easyofferapp.data.FavoritesHelper;
import com.easyoffer.android.easyofferapp.data.OfferContract;
import com.easyoffer.android.easyofferapp.dataAdapters.OfferDetailAdapter;
import com.easyoffer.android.easyofferapp.models.Items;
import com.easyoffer.android.easyofferapp.models.Offers;
import com.easyoffer.android.easyofferapp.ui.OfferDetailActivity;
import com.easyoffer.android.easyofferapp.ui.OfferItemDetailActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfferDetailFragment extends Fragment implements OfferDetailAdapter.OfferItemOnClickHandler {

    private DatabaseReference offerReference;
    private DatabaseReference offeritemsReference;
    private StorageReference mStorageRef;
    private ValueEventListener mOfferListener;
    @BindView(R.id.offer_child_detail)
    RecyclerView mRecyclerView;
    //    @BindView(R.id.offeroutletname)TextView mOutletnameTextView;
    @BindView(R.id.offerdescription)
    TextView mdescriptionView;
    @BindView(R.id.offerimage)
    ImageView mImageView;
    @BindView(R.id.offervaliddates)
    TextView mValidDatesTextView;

    @BindView(R.id.add_to_fav_view)
    ImageButton favoriteButton;
    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    private HashMap<String, String> offer = new HashMap<>();
    //   private String offer_key;

    private OfferDetailAdapter offerDetailAdapter;

    public OfferDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_offer_detail, container, false);


        Intent intent = getActivity().getIntent();

        ButterKnife.bind(this, rootView);
        if (savedInstanceState != null || intent != null && intent.hasExtra("offer_details")) {
            if (getArguments() != null) {
                offer = (HashMap<String, String>) intent.getSerializableExtra("offer_details");

            } else {
                offer = (HashMap<String, String>) getArguments().getSerializable("offer_details");

            }
        }


        mStorageRef = FirebaseStorage.getInstance().getReference();

        if (offer.get("offerKey") != null) {
            offerReference = FirebaseDatabase.getInstance().getReference()
                    .child("offers").child(offer.get("offerKey"));
        }
        offeritemsReference = FirebaseDatabase.getInstance().getReference()
                .child("items");

        //.child("offer").equalTo(offer_key)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(),
                layoutManager.getOrientation()
        );
        mRecyclerView.addItemDecoration(mDividerItemDecoration);


//         offerDetailAdapter = new OfferDetailAdapter((OfferDetailActivity)getActivity());
//          mRecyclerView.setAdapter(offerDetailAdapter);


        offerDetailAdapter = new OfferDetailAdapter(getActivity(), offer.get("offerKey"), this);
        mRecyclerView.setAdapter(offerDetailAdapter);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addFabButtons();

        return rootView;

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            // Respond to the action bar's Up/Home button
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(getActivity());
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    private boolean queryFavorites(ContentResolver r, Long offerid) {

        Uri uri = OfferContract.OfferEntry.buildOfferUri(offerid);
        ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor = null;

        try {

            cursor = resolver.query(uri, null, null, null, null);
            if (cursor.moveToFirst())
                return true;

        } finally {

            if (cursor != null)
                cursor.close();

        }

        return false;
    }


    private void addFabButtons() {
        final ContentResolver resolver = getActivity().getContentResolver();
        final FavoritesHelper fv = new FavoritesHelper();

toggleFavoritesButton(fv, resolver, Long.parseLong(offer.get("id")));
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //offer

                boolean inFavorites = queryFavorites(resolver, Long.parseLong(offer.get("id")));
                if (inFavorites) {
                    fv.deleteFromFavorites(resolver, Long.parseLong(offer.get("id")));
                } else {
                    fv.addToFavorites(resolver, offer);
                }
                toggleFavoritesButton(fv, resolver, Long.parseLong(offer.get("id")));
            }
        });

    }


    /*
    * toggles active state for the favorited star based on whether it is found in the database
    * */
    private void toggleFavoritesButton(FavoritesHelper fv, ContentResolver resolver, long offerid) {

        boolean inFavorites = queryFavorites(resolver, offerid);
        if (inFavorites) {
            favoriteButton.setImageResource(R.drawable.ic_favorite_black_48dp);
        } else {
            favoriteButton.setImageResource(R.drawable.ic_favorite_border_black_48dp);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener offerListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Offers offers = dataSnapshot.getValue(Offers.class);
                // [START_EXCLUDE]
                //  mOutletnameTextView.setText(offers.outletname);
                mdescriptionView.setText(offers.description);
                mValidDatesTextView.setText("Offer Dates: " + offers.startdate + " to " + offers.enddate);
                collapsingToolbar.setTitle(offers.outletname);

                mStorageRef.child("images/" + offers.imageURL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Picasso.with((OfferDetailActivity) getActivity()).load(String.valueOf(uri)).into(mImageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText((OfferDetailActivity) getActivity(), "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        offerReference.addValueEventListener(offerListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mOfferListener = offerListener;

        // Fetch offer details
//        offerDetailAdapter = new OfferDetailAdapter(getActivity(), offer.get("offerKey"), this);
//        mRecyclerView.setAdapter(offerDetailAdapter);


    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mOfferListener != null) {
            offerReference.removeEventListener(mOfferListener);
        }


    }


    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);

        currentState.putSerializable("offer_details", offer);

    }


    @Override
    public void OnClickListener(Items items) {
        final HashMap<String, String> offeritem = new HashMap<>();

        offeritem.put("itemid", String.valueOf(items.itemid));
        offeritem.put("thumbnailURL", items.thumbnailURL);
        offeritem.put("description", items.description);
        offeritem.put("initialCost", String.valueOf(items.initialCost));
        offeritem.put("finalCost", String.valueOf(items.finalCost));
        offeritem.put("name", items.name);
        offeritem.put("offerType", items.offerType);
        offeritem.put("offerKey", items.offer);

        Intent intent = new Intent(getContext(), OfferItemDetailActivity.class);
        intent.putExtra("clicked_item", offeritem);

        startActivity(intent);


    }
}


