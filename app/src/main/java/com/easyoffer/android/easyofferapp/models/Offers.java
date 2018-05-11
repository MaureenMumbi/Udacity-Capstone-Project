package com.easyoffer.android.easyofferapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mauryn on 4/4/2018.
 */

public class Offers implements Parcelable {

    public Long id;
    public String outletname;
    public Boolean active;
    public String description;
    public String startdate;
    public String enddate;
    public String group;
    public Boolean featured;
    public String imageURL;
    public String offerKey;
//    public Map<String, String> items = new HashMap<>();
//    public ArrayList<String> offers  = new ArrayList<>();

    public Offers() {
    }

    public Offers(Long id, String outletname, Boolean active, String description, String startdate, String enddate, String group,
                  boolean featured, String imageURL, String offerKey) {
        this.id = id;
        this.outletname = outletname;
        this.active = active;
        this.description = description;
        this.startdate = startdate;
        this.enddate = enddate;
        this.group = group;
        this.featured = featured;
        this.imageURL = imageURL;
        this.offerKey = offerKey;
    }


    protected Offers(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        outletname = in.readString();
        byte tmpActive = in.readByte();
        active = tmpActive == 0 ? null : tmpActive == 1;
        description = in.readString();
        startdate = in.readString();
        enddate = in.readString();
        group = in.readString();
        byte tmpFeatured = in.readByte();
        featured = tmpFeatured == 0 ? null : tmpFeatured == 1;
        imageURL = in.readString();
        offerKey = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(outletname);
        dest.writeByte((byte) (active == null ? 0 : active ? 1 : 2));
        dest.writeString(description);
        dest.writeString(startdate);
        dest.writeString(enddate);
        dest.writeString(group);
        dest.writeByte((byte) (featured == null ? 0 : featured ? 1 : 2));
        dest.writeString(imageURL);
        dest.writeString(offerKey);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Offers> CREATOR = new Creator<Offers>() {
        @Override
        public Offers createFromParcel(Parcel in) {
            return new Offers(in);
        }

        @Override
        public Offers[] newArray(int size) {
            return new Offers[size];
        }
    };

    // [START offer_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("outletname", outletname);
        result.put("active", active);
        result.put("description", description);
        result.put("startdate", startdate);
        result.put("enddate", enddate);
        result.put("group", group);
        result.put("featured", featured);
        result.put("imageURL", imageURL);
        result.put("offerKey", offerKey);

        return result;
    }


    // [END post_to_map]

}
