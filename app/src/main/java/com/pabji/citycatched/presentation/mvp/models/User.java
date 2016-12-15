package com.pabji.citycatched.presentation.mvp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by usuario on 3/08/16.
 */
public class User implements Parcelable{
    public String name = "";
    public String uid = "";
    public String urlImage = "";

    public ArrayList<String> userProviders = new ArrayList<>();

    @Exclude
    public ArrayList<Building> visited = new ArrayList<>();

    public User(){}


    protected User(Parcel in) {
        name = in.readString();
        uid = in.readString();
        urlImage = in.readString();
        userProviders = in.createStringArrayList();
        visited = in.createTypedArrayList(Building.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(uid);
        dest.writeString(urlImage);
        dest.writeStringList(userProviders);
        dest.writeTypedList(visited);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("date", urlImage);

        return result;
    }
}
