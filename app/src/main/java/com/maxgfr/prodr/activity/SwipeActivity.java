package com.maxgfr.prodr.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.maxgfr.prodr.R;
import com.maxgfr.prodr.adapter.ProfileAdapter;
import com.maxgfr.prodr.model.AsyncGet;
import com.maxgfr.prodr.model.AsyncModify;
import com.maxgfr.prodr.model.FirebaseService;
import com.maxgfr.prodr.model.Profile;
import com.maxgfr.prodr.model.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SwipeActivity extends Activity {

    private SwipeFlingAdapterView flingContainer;
    private ProfileAdapter profileAdapter;
    private ArrayList<Profile> listAccept;
    private ArrayList<Profile> listRefuse;
    private ArrayList<Profile> allProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        FirebaseService firebaseService = FirebaseService.getInstance();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        String id = pref.getString("id", "");
        listAccept = getIntent().getParcelableArrayListExtra("ACCEPT_LIST");
        if(listAccept == null) {
            listAccept = new ArrayList<>();
        }
        listRefuse = getIntent().getParcelableArrayListExtra("REFUSE_LIST");
        if(listRefuse == null) {
            listRefuse = new ArrayList<>();
        }
        allProfile = getIntent().getParcelableArrayListExtra("ALL_PROFILE");
        if(allProfile == null) {
            allProfile = new ArrayList<>();
        }
        flingContainer = findViewById(R.id.frame);
        /*
        System.out.println("LIST ACCEEEEEEPT");
        System.out.println(listAccept);
        System.out.println("LIST REFUUUUUUUSE");
        System.out.println(listRefuse);
         */
        ArrayList<Profile> newList = new ArrayList<>(allProfile);
        for(Profile p : newList) {
            if(listRefuse.contains(p) || listAccept.contains(p)) {
                allProfile.remove(p);
            }
        }

        profileAdapter = new ProfileAdapter(this, R.layout.view_layout, allProfile);


        flingContainer.setAdapter(profileAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                profileAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                makeToast(SwipeActivity.this, "Rejected");
                System.out.println(dataObject);
                String id = pref.getString("id", "");
                listRefuse.add((Profile) dataObject);
                allProfile.remove(dataObject);
                Map<String, Object> data = new HashMap<>();
                data.put("listRefuse", listRefuse);
                final FirebaseService firebaseService = FirebaseService.getInstance();
                firebaseService.modifyData(id, "users", data, new AsyncModify() {
                    @Override
                    public void onSuccess(String msg) {
                        System.out.println("Data added");
                        Toast.makeText(getApplicationContext(), "Data added to database", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String msg) {
                        System.out.println(msg);
                    }
                });
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                makeToast(SwipeActivity.this, "Accepted");
                System.out.println(dataObject);
                String id = pref.getString("id", "");
                listAccept.add((Profile) dataObject);
                allProfile.remove(dataObject);
                Map<String, Object> data = new HashMap<>();
                data.put("listAccept", listAccept);
                final FirebaseService firebaseService = FirebaseService.getInstance();
                firebaseService.modifyData(id, "users", data, new AsyncModify() {
                    @Override
                    public void onSuccess(String msg) {
                        System.out.println("Data added");
                        Toast.makeText(getApplicationContext(), "Data added to database", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String msg) {
                        System.out.println(msg);
                    }
                });
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                //arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
            }

            @Override
            public void onScroll(float v) {

            }

        });

        Context ctx = this;

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(SwipeActivity.this, "Clicked!");
                FirebaseService firebaseService1 = FirebaseService.getInstance();
                Profile obj =  (Profile) dataObject;
                firebaseService1.getData(obj.getId(),"users", new AsyncGet() {
                    @Override
                    public void onSuccess(String id, Map<String, Object> object) {
                        try {
                            String firstname = object.get("firstname").toString();
                            String lastname = object.get("lastname").toString();
                            String description = object.get("description").toString();
                            String email = object.get("email").toString();
                            String thumbnailUrl = null;
                            try {
                                thumbnailUrl = object.get("thumbnailUrl").toString();
                            } catch(Exception e) {
                                thumbnailUrl = "";
                            }
                            Set<String> set = Utils.videoToSet(object.get("listUpload"));
                            ArrayList<String> uploadList = new ArrayList<>();
                            uploadList.addAll(set);
                            Intent i = new Intent(ctx, OtherProfileActivity.class);
                            i.putExtra("EMAIL", email);
                            i.putExtra("FIRSTNAME", firstname);
                            i.putExtra("DESCRIPTION", description);
                            i.putExtra("LASTNAME", lastname);
                            i.putExtra("THUMBNAIL_URL", thumbnailUrl);
                            i.putExtra("SEE_ELEMENT", false);
                            i.putStringArrayListExtra("UPLOAD_LIST", uploadList);
                            ctx.startActivity(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    public void onLeft(View view) {
        flingContainer.getTopCardListener().selectLeft();
    }

    public void onRight(View view) {
        flingContainer.getTopCardListener().selectRight();
    }

    public void onProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void onMatch(View view) {
        Intent i = new Intent(this, MatchActivity.class);
        i.putParcelableArrayListExtra("ACCEPT_LIST", listAccept);
        startActivity(i);
    }
}
