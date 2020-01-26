package com.maxgfr.prodr.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.maxgfr.prodr.R;
import com.maxgfr.prodr.model.AsyncCollectionGet;
import com.maxgfr.prodr.model.AsyncGet;
import com.maxgfr.prodr.model.FirebaseService;
import com.maxgfr.prodr.model.Profile;
import com.maxgfr.prodr.model.Utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress);
        Sprite doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        boolean inscription_done = pref.getBoolean("inscription_done", false);
        FirebaseService firebaseService = FirebaseService.getInstance();
        String id = pref.getString("id", "");
        Context ctx = this;
        if(!inscription_done) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            firebaseService.getCollection("users", new AsyncCollectionGet() {
                public void onSuccess(Map<String, Map<String, Object>> res) {
                    try {
                        System.out.println("CollectionLoaded");
                        ArrayList<Profile> allListProfile = new ArrayList<>();

                        for (Map.Entry<String, Map<String, Object>> entry : res.entrySet()) {
                            Map<String, Object> object = entry.getValue();
                            Profile profile = new Profile(
                                    object.get("id"),
                                    object.get("firstname"),
                                    object.get("thumbnailUrl")
                                );
                            allListProfile.add(profile);
                        }
                        //System.out.println(allListProfile);
                        firebaseService.getData(id,"users", new AsyncGet() {
                            @Override
                            public void onSuccess(String id, Map<String, Object> object) {
                                try {
                                    System.out.println("DataLoaded");
                                    String firstname = object.get("firstname").toString();
                                    String lastname = object.get("lastname").toString();
                                    String description = object.get("description").toString();
                                    String email = object.get("email").toString();
                                    String thumbnailUrl = "";
                                    try {
                                        thumbnailUrl = object.get("thumbnailUrl").toString();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Set set = Utils.videoToSet(object.get("listUpload"));
                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("firstname", firstname);
                                    editor.putString("lastname", lastname);
                                    editor.putString("description", description);
                                    editor.putString("email", email);
                                    editor.putString("thumbnailUrl", thumbnailUrl);
                                    if(object.get("listAccept") != null) {
                                        editor.putString("listAccept", object.get("listAccept").toString());
                                    }
                                    if(object.get("listRefuse") != null) {
                                        editor.putString("listRefuse", object.get("listRefuse").toString());
                                    }
                                    editor.putStringSet("all_video_id", set);
                                    editor.apply();
                                    ArrayList<Profile> listAccept = Utils.profileListToParcelable(object.get("listAccept"));
                                    ArrayList<Profile> listRefuse = Utils.profileListToParcelable(object.get("listRefuse"));
                                    Intent i = new Intent(ctx, SwipeActivity.class);
                                    i.putParcelableArrayListExtra("ACCEPT_LIST", listAccept);
                                    i.putParcelableArrayListExtra("REFUSE_LIST", listRefuse);
                                    i.putParcelableArrayListExtra("ALL_PROFILE", allListProfile);
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
    }
}
