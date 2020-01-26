package com.maxgfr.prodr.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxgfr.prodr.R;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder{

    public ImageView imageView;
    public TextView textView;
    public RelativeLayout layout;

    public MyViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.matchImageuh);
        textView = itemView.findViewById(R.id.matchNameuh);
        layout = itemView.findViewById(R.id.parentLayoutMatch);
    }
}
