package com.easyoffer.android.easyofferapp.fragments;


/**
 * A simple {@link Fragment} subclass.
 */
//public class CategoriesFragment extends Fragment {
//
////
////    public CategoriesFragment() {
////        // Required empty public constructor
////    }
////
////
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////        // Inflate the layout for this fragment
////        return inflater.inflate(R.layout.fragment_categories, container, false);
////    }
//
//}

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;
import static com.easyoffer.android.easyofferapp.fragments.OfferCategoriesFragment.CATEGORY_GROUP_MEMBERS;
import static com.easyoffer.android.easyofferapp.ui.CategoryDetailActivity.EXTRA_CATEGORY_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment {

    private DatabaseReference mdatabaseReference;
    private StorageReference mStorageRef;
    private ValueEventListener mOfferListener;
    private ArrayList<String> category_members;
    @BindView(R.id.categoryofferlistrv)
    RecyclerView mRecyclerView;


    private String category_key;
    private FirebaseRecyclerAdapter<Offers, OfferViewHolder> mAdapter;


    private Query categoryquery;
    private StaggeredGridLayoutManager layoutManager;

    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        ButterKnife.bind(this, rootView);
        if (savedInstanceState != null) {
            category_key = savedInstanceState.getString(EXTRA_CATEGORY_KEY);
            category_members = savedInstanceState.getStringArrayList(CATEGORY_GROUP_MEMBERS);


        } else {
            category_key = getArguments().getString(EXTRA_CATEGORY_KEY); // TO BE LOOKED AT FRM PREVIOUS LESSONS
            category_members = getArguments().getStringArrayList(CATEGORY_GROUP_MEMBERS);

        }
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mdatabaseReference = FirebaseDatabase.getInstance().getReference();


        categoryquery = FirebaseDatabase.getInstance().getReference().child("offers").orderByChild("categories/" + category_key).equalTo(true);
        mRecyclerView.setHasFixedSize(true);

        return rootView;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);


        FirebaseRecyclerOptions firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Offers>().
                setQuery(categoryquery, Offers.class).build();

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
//                        Log.d(TAG, "iTEM " + offerKey + "CLICKED ON MAIN ACTIVITY");
//                        Intent intent = new Intent(getActivity(), OfferDetailActivity.class);
//                        intent.putExtra(OfferDetailActivity.EXTRA_OFFER_KEY, offerKey);
//                        startActivity(intent);

                        Log.d(TAG, "iTEM " + offerKey + "------------" + model.outletname + "--------------" + model.description + "CLICKED ON CATEGORY ACTIVITY");

                        offers.put("id", String.valueOf(model.id));
                        offers.put("outletname", model.outletname);
                        offers.put("description", model.description);
                        offers.put("startdate", model.startdate);
                        offers.put("enddate", model.enddate);
                        offers.put("imageUrl", model.imageURL);
                        offers.put("offerKey", offerKey);

                        Intent intent = new Intent(getActivity(), OfferDetailActivity.class);
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

        // Remove post value event listener
        if (mOfferListener != null) {
            mdatabaseReference.removeEventListener(mOfferListener);
        }

        // Clean up comments listener
        // offerDetailAdapter.cleanupListener();
    }


}



