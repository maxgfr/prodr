package com.maxgfr.prodr.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maxgfr.prodr.R;

import java.util.List;


public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MyViewHolder>{
    List<MatchUnit> matches;
    private Context mContext;

    public MatchAdapter(Context context, List<MatchUnit> matches) {
        this.matches = matches;
        this.mContext = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.match_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.display(matches.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProfilActivity.class);
            //    intent.putExtra("match", matches.get(position));

                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView matchName ;
        LinearLayout parentLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            matchName = (TextView)itemView.findViewById(R.id.matchName);
        }

        public void display(MatchUnit unit) {
            matchName.setText("\n"+unit.getMatch());
            parentLayout = itemView.findViewById(R.id.parent_layout_match);
        }

    }

}
