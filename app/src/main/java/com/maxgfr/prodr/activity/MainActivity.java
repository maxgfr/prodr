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
import com.maxgfr.prodr.model.AsyncGet;
import com.maxgfr.prodr.model.FirebaseService;
import com.maxgfr.prodr.model.Utils;

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
            firebaseService.getData(id,"users", new AsyncGet() {
                @Override
                public void onSuccess(String id, Map<String, Object> object) {
                    try {
                        System.out.println("DataLoaded");
                        String firstname = object.get("firstname").toString();
                        String lastname = object.get("lastname").toString();
                        String description = object.get("description").toString();
                        String email = object.get("email").toString();
                        String thumbnailUrl = object.get("thumbnailUrl").toString();
                        Set set = Utils.videoToSet(object.get("listUpload"));
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("firstname", firstname);
                        editor.putString("lastname", lastname);
                        editor.putString("description", description);
                        editor.putString("email", email);
                        editor.putString("thumbnailUrl", thumbnailUrl);
                        editor.putStringSet("all_video_id", set);
                        editor.apply();
                        Intent i = new Intent(ctx, SwipeActivity.class);
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
    }
}
