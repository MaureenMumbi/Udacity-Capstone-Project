package com.easyoffer.android.easyofferapp.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mauryn on 4/10/2018.
 */

public class Groups {

    public String groupname;
    public HashMap<String, Boolean> members;
    public String categoryImageURL;

    public Groups() {
    }

    public Groups(String groupname, HashMap<String, Boolean> members, String categoryImageURL) {
        this.groupname = groupname;
        this.members = members;
        this.categoryImageURL = categoryImageURL;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("groupname", groupname);
        result.put("members", members);
        result.put("categoryImageURL", categoryImageURL);

        return result;
    }

}
