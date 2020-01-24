package com.maxgfr.prodr.model;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class FacebookApi {

    private static FacebookApi single_instance = null;

    private FacebookApi() {
    }

    public static FacebookApi getInstance() {
        if (single_instance == null)
            single_instance = new FacebookApi();

        return single_instance;
    }

    public void getInfo(AccessToken accessToken, final AsyncFbApi myInterface) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            System.out.println(response.getRawResponse());
                            myInterface.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
