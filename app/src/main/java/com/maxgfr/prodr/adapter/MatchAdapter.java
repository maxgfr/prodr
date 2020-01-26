package com.maxgfr.prodr.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maxgfr.prodr.R;
import com.maxgfr.prodr.activity.OtherProfileActivity;
import com.maxgfr.prodr.model.AsyncGet;
import com.maxgfr.prodr.model.FirebaseService;
import com.maxgfr.prodr.model.MatchUnit;
import com.maxgfr.prodr.model.Utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

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
                FirebaseService firebaseService1 = FirebaseService.getInstance();
                firebaseService1.getData(mDataset.get(position).getId(),"users", new AsyncGet() {
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
                            Intent i = new Intent(mContext, OtherProfileActivity.class);
                            i.putExtra("EMAIL", email);
                            i.putExtra("FIRSTNAME", firstname);
                            i.putExtra("DESCRIPTION", description);
                            i.putExtra("LASTNAME", lastname);
                            i.putExtra("THUMBNAIL_URL", thumbnailUrl);
                            i.putExtra("SEE_ELEMENT", true);
                            i.putStringArrayListExtra("UPLOAD_LIST", uploadList);
                            mContext.startActivity(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }










}
