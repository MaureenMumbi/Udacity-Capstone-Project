package com.easyoffer.android.easyofferapp.viewholders;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.easyoffer.android.easyofferapp.R;
import com.easyoffer.android.easyofferapp.models.Groups;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mauryn on 4/11/2018.
 */
public class GroupsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.groupname)
    TextView groupnameTextView;
    @BindView(R.id.categorythumbnail)
    ImageView categoryImageView;


    public GroupsViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);


    }

    public void bindtoGroups(final Groups groups, final Context context, StorageReference storageReference) {

        System.out.println(groups.groupname + ": ");
        groupnameTextView.setText(groups.groupname);

        storageReference.child("categories/" + groups.categoryImageURL);
        if (groups.categoryImageURL != null) {

            storageReference.child("categories/" + groups.categoryImageURL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context).load(String.valueOf(uri)).into(categoryImageView);
                    categoryImageView.setContentDescription(groups.groupname);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

        }
    }
}
