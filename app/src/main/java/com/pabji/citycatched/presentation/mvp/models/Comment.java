package com.pabji.citycatched.presentation.mvp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.pabji.citycatched.presentation.utils.StringFormat;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by usuario on 3/08/16.
 */
public class Comment implements Parcelable {
    public String userId;
    public String description;
    public String username;
    public String userImage;
    public Long date;
    public Float mark;

    public Comment(){}

    protected Comment(Parcel in) {
        userId = in.readString();
        description = in.readString();
        username = in.readString();
        userImage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(description);
        dest.writeString(username);
        dest.writeString(userImage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    @Exclude
    public String getDateFormated(){
        return StringFormat.timemilisToDate(date);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("description", description);
        result.put("date", date);
        result.put("mark", mark);
        result.put("username",username);
        result.put("userImage",userImage);
        result.put("userId",userId);
        return result;
    }
}
