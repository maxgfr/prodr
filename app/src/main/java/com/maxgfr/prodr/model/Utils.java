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
}
