package com.easyoffer.android.easyofferapp.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mauryn on 4/10/2018.
 */

public class Items {

    public String description;
    public Long finalCost;
    public Long initialCost;
    public Long itemid;
    public String name;
    public String offerType;
    public String thumbnailURL;
    public String offer;

    public Items() {
    }

    public Items(String description, Long finalCost, Long initialCost, Long itemid, String name, String offerType, String thumbnailURL, String offer) {
        this.description = description;
        this.finalCost = finalCost;
        this.initialCost = initialCost;
        this.offerType = offerType;
        this.thumbnailURL = thumbnailURL;
        this.offer = offer;
        this.itemid = itemid;
        this.name = name;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("description", description);
        result.put("name", name);
        result.put("finalCost", finalCost);
        result.put("initialCost", initialCost);
        result.put("itemid", itemid);
        result.put("offerType", offerType);
        result.put("thumbnailURL", thumbnailURL);
        result.put("offer", offer);

        return result;
    }

}
