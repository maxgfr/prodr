package com.maxgfr.prodr.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maxgfr.prodr.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MatchActivity extends AppCompatActivity {

    private List<MatchUnit> matches = new ArrayList<>();
    private RecyclerView matchRecyclerView;
    private MatchAdapter matchAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        readMatches();
        matchRecyclerView = (RecyclerView)findViewById(R.id.matchRecyclerView);
        matchAdapter = new MatchAdapter(this,matches);
        matchRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        matchRecyclerView.setAdapter(matchAdapter);

    }


    public void readMatches() {
        InputStream is = getResources().openRawResource(R.raw.matches);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "" ;
        try {
            while (((line = reader.readLine()) != null)) {
                Log.d("MatchActivity",line);
                MatchUnit match = new MatchUnit();
                match.setMatch(line);
                matches.add(match);

                Log.d("MatchActivity", "Just added " + line);
            }
        }
        catch (IOException e) {
            Log.wtf("MatchActivity", "Error reading data file on line" + line,e);
            e.printStackTrace();
        }

    }


}

