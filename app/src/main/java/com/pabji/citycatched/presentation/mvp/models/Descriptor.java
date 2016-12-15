package com.pabji.citycatched.presentation.mvp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pablo Jiménez Casado on 06/08/2016.
 */

public class Descriptor implements Parcelable {
    public String content = "";
    public String buildingId = "";
    public MyLocation myLocation = new MyLocation();

    public Descriptor(){}


    protected Descriptor(Parcel in) {
        content = in.readString();
        buildingId = in.readString();
        myLocation = in.readParcelable(MyLocation.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(buildingId);
        dest.writeParcelable(myLocation, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Descriptor> CREATOR = new Creator<Descriptor>() {
        @Override
        public Descriptor createFromParcel(Parcel in) {
            return new Descriptor(in);
        }

        @Override
        public Descriptor[] newArray(int size) {
            return new Descriptor[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public MyLocation getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(MyLocation myLocation) {
        this.myLocation = myLocation;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("buildingId", buildingId);
        result.put("myLocation",myLocation.toMap());
        return result;
    }
}
