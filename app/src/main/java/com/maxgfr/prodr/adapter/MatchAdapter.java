package com.maxgfr.prodr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.maxgfr.prodr.R;
import com.maxgfr.prodr.model.MatchUnit;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MatchAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private ArrayList<MatchUnit> mDataset;
    private Context mContext;


    public MatchAdapter(ArrayList<MatchUnit> matchs, Context context) {
        this.mDataset = matchs;
        this.mContext = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.textView.setText(mDataset.get(position).getMatchName());
        try {
            Glide.with(mContext)
                    .asBitmap()
                    .load(mDataset.get(position).getMatchImage())
                    .into(holder.imageView);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
