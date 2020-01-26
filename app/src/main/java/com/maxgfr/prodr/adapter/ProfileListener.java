package com.maxgfr.prodr.adapter;

import com.maxgfr.prodr.model.Profile;

import androidx.annotation.NonNull;

public interface ProfileListener {

    void onProfileRemoved(@NonNull final Profile profile);

    void onEmptyList();
}
