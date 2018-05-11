package com.easyoffer.android.easyofferapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mauryn on 4/25/2018.
 */

public class Offer implements Parcelable {

    public int offer_id;
    public String description;
    public String outletname;
    public String startdate;
    public String enddate;
    public String imageUrl;
    public String offerKey;


    public Offer(int offer_id, String description, String outletname, String startdate,String enddate, String imageUrl, String offerKey){
        this.offer_id = offer_id;
        this.description = description;
        this.outletname = outletname;
        this.startdate = startdate;
        this.enddate = enddate;
        this.imageUrl = imageUrl;
        this.offerKey = offerKey;
    }

    public int getOfferID() {
        return offer_id;
    }
    public void setOfferID(int offerid) {
        offer_id = offerid;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String offerdescription) {
        description = offerdescription;
    }

    public String getOutletname() {
        return outletname;
    }
    public void setOutletname(String offeroutletname) {
        outletname = offeroutletname;
    }

    public String getStartdate() {
        return startdate;
    }
    public void setStartdate(String offerstartdate) {
        startdate = offerstartdate;
    }


    public String getEnddate() {
        return enddate;
    }
    public void setEnddate(String offerenddate) {
        enddate = offerenddate;
    }

    public String getImageUrl(){return  imageUrl;}
    public void  setImageUrl(String offerimageUrl){imageUrl = offerimageUrl;}


    public String getOfferKey(){return  offerKey;}
    public void  setOfferKey(String offer_Key){offerKey = offer_Key;}



    protected Offer(Parcel in) {
        offer_id = in.readInt();
        description = in.readString();
        outletname = in.readString();
        startdate = in.readString();
        enddate  = in.readString();
        imageUrl = in.readString();
        offerKey = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(offer_id);
        dest.writeString(description);
        dest.writeString(outletname);
        dest.writeString(startdate);
        dest.writeString(enddate);
        dest.writeString(imageUrl);
        dest.writeString(offerKey);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Offer> CREATOR = new Creator<Offer>() {
        @Override
        public Offer createFromParcel(Parcel in) {
            return new Offer(in);
        }

        @Override
        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };
}
