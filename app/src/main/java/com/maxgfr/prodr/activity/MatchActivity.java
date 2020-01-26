package com.maxgfr.prodr.activity;

import android.os.Bundle;

import com.maxgfr.prodr.R;
import com.maxgfr.prodr.adapter.MatchAdapter;
import com.maxgfr.prodr.model.MatchUnit;
import com.maxgfr.prodr.model.Profile;
import com.maxgfr.prodr.model.Utils;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MatchActivity extends AppCompatActivity {

    private ArrayList<MatchUnit> matches;
    private ArrayList<Profile> listAccept;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        listAccept = getIntent().getParcelableArrayListExtra("ACCEPT_LIST");
        if(listAccept == null) {
            listAccept = new ArrayList<>();
        }
        matches = Utils.listProfileToListMatch(listAccept);
        RecyclerView recyclerView = findViewById(R.id.matchRecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter mAdapter = new MatchAdapter(matches, this);
        recyclerView.setAdapter(mAdapter);
    }

}

