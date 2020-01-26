package com.maxgfr.prodr.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Profile implements Parcelable {

    private String id;
    private String name;
    private String thumbnailId;

    public Profile(String id, String name, String thumbnailId) {
        this.id = id;
        this.name = name;
        this.thumbnailId = thumbnailId;
    }

    public String getName() {
        return this.name;
    }

    public String getThumbnailId() {
        return this.thumbnailId;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Profile)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Profile c = (Profile) o;

        return c.getId().equals(this.id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(thumbnailId);
    }

    protected Profile(Parcel in) {
        id = in.readString();
        name = in.readString();
        thumbnailId = in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

}
