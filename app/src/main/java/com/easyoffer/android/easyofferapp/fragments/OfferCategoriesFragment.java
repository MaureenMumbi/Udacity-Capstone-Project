package com.easyoffer.android.easyofferapp.fragments;


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
import com.easyoffer.android.easyofferapp.models.Groups;
import com.easyoffer.android.easyofferapp.ui.CategoryDetailActivity;
import com.easyoffer.android.easyofferapp.viewholders.GroupsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfferCategoriesFragment extends Fragment {


    public static final String CATEGORY_GROUP_MEMBERS = "GROUP_MEMBERS";
    private final static String TAG = "OfferCategoriesFragment";
    @BindView(R.id.offerlistrv)
    RecyclerView mRecyclerView;
    StaggeredGridLayoutManager layoutManager;
    private DatabaseReference databaseReference;
    private StorageReference mStorageRef;
    private FirebaseRecyclerAdapter<Groups, GroupsViewHolder> mAdapter;

    public OfferCategoriesFragment() {
        // Required empty public constructor
    }

    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("offers").child("groups");
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
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        final Query groupQuery = getQuery(databaseReference);


        FirebaseRecyclerOptions firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Groups>().
                setQuery(groupQuery, Groups.class).build();


        mAdapter = new FirebaseRecyclerAdapter<Groups, GroupsViewHolder>(firebaseRecyclerOptions) {

            @Override
            protected void onBindViewHolder(@NonNull GroupsViewHolder holder, int position, @NonNull final Groups model) {
                final DatabaseReference groupRef = getRef(position);
                System.out.println("GROUP KEY %%%%%%%%%" + groupRef.getKey());
                final String categoryKey = groupRef.getKey();

                final ArrayList<String> alloffersincategory = new ArrayList<>();

                for (String key : model.members.keySet()) {
                    System.out.println(key);
                    alloffersincategory.add(key);
                }


                holder.bindtoGroups(model, getContext(), mStorageRef);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity

                        Log.d(TAG, "iTEM " + categoryKey + "CLICKED FROM MAIN ACTIVITY");

                        Intent intent = new Intent(getActivity(), CategoryDetailActivity.class);
                        intent.putExtra(CategoryDetailActivity.EXTRA_CATEGORY_KEY, categoryKey);

                        intent.putExtra(CATEGORY_GROUP_MEMBERS, alloffersincategory);
                        startActivity(intent);
                    }
                });


            }


            @Override
            public GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Context context = parent.getContext();
                int layoutIdForListItem = R.layout.list_offer_categories;
                LayoutInflater inflater = LayoutInflater.from(context);
                boolean shouldAttachtoParentImmediately = false;
                View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachtoParentImmediately);
                GroupsViewHolder groupsViewHolder = new GroupsViewHolder(view);
                return groupsViewHolder;

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

