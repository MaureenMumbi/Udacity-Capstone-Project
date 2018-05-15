package com.easyoffer.android.easyofferapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easyoffer.android.easyofferapp.R;
import com.easyoffer.android.easyofferapp.data.FavoritesHelper;
import com.easyoffer.android.easyofferapp.data.Offer;
import com.easyoffer.android.easyofferapp.data.OfferListService;
import com.easyoffer.android.easyofferapp.dataAdapters.FavoriteOfferAdapter;
import com.easyoffer.android.easyofferapp.ui.OfferDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.easyoffer.android.easyofferapp.data.FavoriteOfferWidget.FAVOURITE_OFFER_LIST;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements FavoriteOfferAdapter.GridItemOnClickHandler {


    @BindView(R.id.favofferlistrv)
    RecyclerView favRecyclerView;
    FavoriteOfferAdapter favoriteOfferAdapter;
    ArrayList<Offer> favList = new ArrayList();
    private ArrayList<String> favorited_offers;
    private StaggeredGridLayoutManager layoutManager;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, rootView);
        favRecyclerView.setHasFixedSize(true);
        Intent intent = getActivity().getIntent();
        if (savedInstanceState != null || intent != null && intent.hasExtra(FAVOURITE_OFFER_LIST)) {
            if (getArguments() != null) {

                favList = intent.getParcelableArrayListExtra(FAVOURITE_OFFER_LIST);


            } else {
                favList = getArguments().getParcelableArrayList(FAVOURITE_OFFER_LIST);

            }
        } else {
            favList = FavoritesHelper.getFavorites(getContext());
        }

        if (favList.size() == 0) {

            Snackbar
                    .make(favRecyclerView, "You have no favorite offers ", Snackbar.LENGTH_INDEFINITE).
                    show();
        }

        OfferListService.startActionShowFavorites(getContext(), favList);

        return rootView;


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        favRecyclerView.setLayoutManager(layoutManager);

        // ArrayList<Offer> favs = FavoritesHelper.getFavorites(getContext());

        favoriteOfferAdapter = new FavoriteOfferAdapter(getContext(), favList, this);

        favRecyclerView.setAdapter(favoriteOfferAdapter);
    }


    @Override
    public void OnClickListener(Offer offer) {
        final HashMap<String, String> offers = new HashMap<>();

        offers.put("id", String.valueOf(offer.getOfferID()));
        offers.put("outletname", offer.getOutletname());
        offers.put("description", offer.getDescription());
        offers.put("startdate", offer.getStartdate());
        offers.put("enddate", offer.getEnddate());
        offers.put("imageUrl", offer.getImageUrl());
        offers.put("offerKey", offer.getOfferKey());
        Intent intent = new Intent(getContext(), OfferDetailActivity.class)
                .putExtra("offer_details", offers);
        startActivity(intent);

    }
}
