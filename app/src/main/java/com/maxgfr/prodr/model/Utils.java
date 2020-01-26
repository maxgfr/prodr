package com.maxgfr.prodr.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {

    public static Set<String> videoToSet(Object o) {
        List<Object> group = (List<Object>) o;
        ArrayList<String> list_url = new ArrayList<>();
        for (int i = 0; i < group.size(); i++) {
            Object value = group.get(i);
            JSONObject obj = null;
            try {
                obj = new JSONObject(convertStandardJSONString(value.toString()));
                //System.out.println(obj);
                list_url.add(obj.getString("videoId"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Set<String> set = new HashSet<>(list_url);
        //System.out.println(set);
        return set;
    }

    private static String convertStandardJSONString(String data_json){
        data_json = data_json.replace("=", "=\"");
        data_json = data_json.replace(",", "\",");
        data_json = data_json.replace("}", "\"}");
        return data_json;
    }

    public static ArrayList<Profile> profileListToParcelable(Object o) {
        if(o == null) {
            return new ArrayList<>();
        }
        List<Object> group = (List<Object>) o;
        ArrayList<Profile> list_profile = new ArrayList<>();
        for (int i = 0; i < group.size(); i++) {
            Object value = group.get(i);
            JSONObject obj = null;
            try {
                obj = new JSONObject(convertStandardJSONString(value.toString()));
                Profile p = new Profile(obj.getString("id"), obj.getString("name"), obj.getString("thumbnailId"));
                list_profile.add(p);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list_profile;
    }

    public static ArrayList<MatchUnit> listProfileToListMatch(ArrayList<Profile> listAccept) {
        ArrayList<MatchUnit> matchUnits = new ArrayList<>();
        for(Profile p : listAccept) {
            MatchUnit m = new MatchUnit(p.getName(), p.getThumbnailId());
            matchUnits.add(m);
        }
        return matchUnits;
    }
}
