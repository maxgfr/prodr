package com.maxgfr.prodr.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.maxgfr.prodr.R;
import com.maxgfr.prodr.model.AsyncGet;
import com.maxgfr.prodr.model.FirebaseService;
import com.maxgfr.prodr.model.Utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class SwipeActivity extends Activity {

    private SwipeFlingAdapterView flingContainer;
    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        FirebaseService firebaseService = FirebaseService.getInstance();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        String id = pref.getString("id", "");
        firebaseService.getData(id, "users", new AsyncGet() {
            @Override
            public void onSuccess(String id, Map<String, Object> object) {
                System.out.println("On create SwipeActivity");
                String firstname = object.get("firstname").toString();
                String lastname = object.get("lastname").toString();
                String description = object.get("description").toString();
                String email = object.get("email").toString();
                String thumbnailUrl = object.get("thumbnailUrl").toString();
                Set set = Utils.videoToSet(object.get("listUpload"));
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("inscription_done", true);
                editor.putString("firstname", firstname);
                editor.putString("lastname", lastname);
                editor.putString("description", description);
                editor.putString("email", email);
                editor.putString("thumbnailUrl", thumbnailUrl);
                editor.putStringSet("all_video_id", set);
                editor.apply();
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
        flingContainer = findViewById(R.id.frame);

        al = new ArrayList<>();
        al.add("php");
        al.add("c");
        al.add("python");
        al.add("java");
        al.add("html");
        al.add("c++");
        al.add("css");
        al.add("javascript");

        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.helloText, al );


        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                makeToast(SwipeActivity.this, "Left!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                makeToast(SwipeActivity.this, "Right!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                //arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(SwipeActivity.this, "Clicked!");
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
        Intent intent = new Intent(this, ProfilActivity.class);
        startActivity(intent);
    }

    public void onMatch(View view) {
        Intent intent = new Intent(this, MatchActivity.class);
        startActivity(intent);
    }
}
