package com.maxgfr.prodr.model;

import com.facebook.GraphResponse;

import org.json.JSONException;

public interface AsyncFbApi {
    void onSuccess(GraphResponse response) throws JSONException;
    void onFailure(String msg);
}
