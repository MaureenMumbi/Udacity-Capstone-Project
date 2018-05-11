package com.easyoffer.android.easyofferapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easyoffer.android.easyofferapp.R;
import com.easyoffer.android.easyofferapp.models.Offers;
import com.easyoffer.android.easyofferapp.ui.OfferDetailActivity;
import com.easyoffer.android.easyofferapp.viewholders.OfferViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class MainActivityFragment extends Fragment {
    private final static String TAG = "MainActivityFragment";
    @BindView(R.id.offerlistrv)
    RecyclerView mRecyclerView;
    StaggeredGridLayoutManager layoutManager;

    private DatabaseReference databaseReference;
    private StorageReference mStorageRef;
    private FirebaseRecyclerAdapter<Offers, OfferViewHolder> mAdapter;
    private Query offerQuery;
    private FirebaseAnalytics mFirebaseAnalytics;

    public MainActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);
        ButterKnife.bind(this, rootView);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        mRecyclerView.setHasFixedSize(true);

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);

        if (checkIfLandscape() || tabletSize) {
            layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        } else {
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }


        mRecyclerView.setLayoutManager(layoutManager);
        final Query offerQuery = getQuery(databaseReference);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        FirebaseRecyclerOptions firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Offers>().
                setQuery(offerQuery, Offers.class).build();
        final HashMap<String, String> offers = new HashMap<>();

        mAdapter = new FirebaseRecyclerAdapter<Offers, OfferViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull OfferViewHolder holder, int position, @NonNull final Offers model) {
                DatabaseReference offerRef = getRef(position);
                final String offerKey = offerRef.getKey();

                holder.bindToOffer(model, getContext(), mStorageRef);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity

                        offers.put("id", String.valueOf(model.id));
                        offers.put("outletname", model.outletname);
                        offers.put("description", model.description);
                        offers.put("startdate", model.startdate);
                        offers.put("enddate", model.enddate);
                        offers.put("imageUrl", model.imageURL);
                        offers.put("offerKey", offerKey);
                        LogAnalytics(model);


                        Log.d(TAG, "iTEM " + offerKey + "------------" + model.outletname + "--------------" + model.description + "CLICKED ON MAIN ACTIVITY");
                        Intent intent = new Intent(getActivity(), OfferDetailActivity.class);
                        // intent.putExtra(OfferDetailActivity.EXTRA_OFFER_KEY, offerKey);

                        intent.putExtra("offer_details", offers);

                        startActivity(intent);
                    }
                });


            }


            @Override
            public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Context context = parent.getContext();
                int layoutIdForListItem = R.layout.list_offers;
                LayoutInflater inflater = LayoutInflater.from(context);
                boolean shouldAttachtoParentImmediately = false;
                View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachtoParentImmediately);
                OfferViewHolder offerViewHolder = new OfferViewHolder(view);
                return offerViewHolder;

            }


        };


        mRecyclerView.setAdapter(mAdapter);
    }


    private void LogAnalytics(Offers offer) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(offer.id));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, offer.outletname);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        mFirebaseAnalytics.setCurrentScreen(getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }

    }

    public abstract Query getQuery(DatabaseReference databaseReference);

    public boolean checkIfLandscape() {
        return getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
