package com.easyoffer.android.easyofferapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easyoffer.android.easyofferapp.R;
import com.easyoffer.android.easyofferapp.models.Offers;
import com.easyoffer.android.easyofferapp.ui.OfferDetailActivity;
import com.easyoffer.android.easyofferapp.viewholders.OfferViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.easyoffer.android.easyofferapp.ui.SearchResultsActivity.EXTRA_SEARCH_QUERY;

/**
 * A simple {@link Fragment} subclass.
 */


public class SearchResultsFragment extends Fragment {


    public SearchResultsFragment() {
        // Required empty public constructor
    }

    public Query getQuery(DatabaseReference databaseReference,String key ,String query) {
        return databaseReference.child("offers").orderByChild("outletname")
                .startAt(query)
                .endAt(query + "\uf8ff");
    }

    private final static String TAG = "SearchResultsFragment";
    @BindView(R.id.searchlistrv)
    RecyclerView mRecyclerView;
    LinearLayoutManager layoutManager;

    @BindView(R.id.searchword)TextView searchwordTextView;

    private DatabaseReference databaseReference;
    private StorageReference mStorageRef;
    private FirebaseRecyclerAdapter<Offers, OfferViewHolder> mAdapter;
    //public static final String CATEGORY_GROUP_MEMBERS ="GROUP_MEMBERS";
    String query = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
        ButterKnife.bind(this, rootView);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        mRecyclerView.setHasFixedSize(true);
        Intent intent = getActivity().getIntent();
        if (getArguments() != null ) {//|| intent != null && intent.hasExtra(EXTRA_SEARCH_QUERY)
                query = getArguments().getString(EXTRA_SEARCH_QUERY);

        }


        searchwordTextView.setText("Search query  :  "+query);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        final Query offerQuery = getQuery(databaseReference,"", query);


        FirebaseRecyclerOptions firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Offers>().
                setQuery(offerQuery, Offers.class).build();

        final HashMap<String, String> offers = new HashMap<>();
        mAdapter = new FirebaseRecyclerAdapter<Offers, OfferViewHolder>(firebaseRecyclerOptions) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();

                if(getItemCount() ==0){
                                 Snackbar
                    .make(mRecyclerView, "No items matched your search", Snackbar.LENGTH_INDEFINITE).
                    show();
                    return;

                }
                else{

                    //show results
                }
            }



            @Override
            protected void onBindViewHolder(@NonNull OfferViewHolder holder, int position, @NonNull final Offers model) {


                final DatabaseReference offerRef = getRef(position);

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
                int layoutIdForListItem = R.layout.list_search_results;
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
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }


}


