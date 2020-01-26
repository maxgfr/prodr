package com.maxgfr.prodr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxgfr.prodr.R;
import com.maxgfr.prodr.model.Profile;

import java.util.ArrayList;

import androidx.annotation.NonNull;


/**
 * Created by renarosantos on 21/02/17.
 */
public class ProfileAdapter extends ArrayAdapter<Profile> {

    private final Context mContext;
    private ArrayList<Profile> mProfiles;
    private ProfileListener mListener;

    public ProfileAdapter(final Context context, final int resource, final ArrayList<Profile> objects) {
        super(context, resource, objects);
        mContext = context;
        mProfiles = objects;
    }

    @Override
    public int getCount() {
        return mProfiles.size();
    }

    public void setListener(final ProfileListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View root = null;
        if (mProfiles != null && mProfiles.size() > position) {
            Profile profile = mProfiles.get(position);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            if (convertView == null) {
                root = inflater.inflate(R.layout.view_layout, parent, false);
            } else {
                root = convertView;
            }
            TextView nameTextView = (TextView) root.findViewById(R.id.image_name);
            ImageView imageView = (ImageView) root.findViewById(R.id.image_art);
            nameTextView.setText(profile.getName());
            try {
                Glide.with(mContext)
                        .asBitmap()
                        .centerCrop()
                        .load(profile.getThumbnailId())
                        .into(imageView);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return root != null ? root : convertView;
    }

    public void removeTop() {
        if(mProfiles.size() == 0){
            return;
        }
        Profile removedProfile = mProfiles.get(0);
        if (mProfiles.size() > 1) {
            mProfiles.remove(removedProfile);
        } else {
            mProfiles = new ArrayList<>();
        }
        if (mListener != null) {
            if (mProfiles.size() > 0) {
                mListener.onProfileRemoved(removedProfile);
            } else {
                mListener.onEmptyList();
            }
        }
        notifyDataSetChanged();
    }


}