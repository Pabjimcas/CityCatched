package com.pabji.citycatched.presentation.mvp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by usuario on 2/08/16.
 */
public class Building implements Parcelable {

    public String id = "";
    public String name = "";
    public String mainImage = "";
    public String urlDescription = "";
    public String shortDescription = "";

    @Exclude
    public MyLocation myLocation = new MyLocation();

    @Exclude
    public float distance = 0;

    @Exclude
    public boolean visited = false;

    @Exclude
    public Set<String> features = new HashSet<>();

    @Exclude
    public List<String> pictures = new ArrayList<>();

    @Exclude
    public ArrayList<String> descriptors = new ArrayList<>();

    @Exclude
    public List<String> nearBuildings = new ArrayList<>();

    @Exclude
    public ArrayList<Comment> comments = new ArrayList<>();

    public Building(String id,String mainImage){
        this.id = id;
        this.mainImage = mainImage;
    }

    public Building(){}

    public Building(MyLocation myLocation, RecognizedObject recognizedObject) {
        this.myLocation = myLocation;
        this.descriptors.add(recognizedObject.contentDescriptorDetected);
    }


    protected Building(Parcel in) {
        id = in.readString();
        name = in.readString();
        mainImage = in.readString();
        urlDescription = in.readString();
        shortDescription = in.readString();
        myLocation = in.readParcelable(MyLocation.class.getClassLoader());
        distance = in.readFloat();
        visited = in.readByte() != 0;
        pictures = in.createStringArrayList();
        descriptors = in.createStringArrayList();
        nearBuildings = in.createStringArrayList();
        comments = in.createTypedArrayList(Comment.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(mainImage);
        dest.writeString(urlDescription);
        dest.writeString(shortDescription);
        dest.writeParcelable(myLocation, flags);
        dest.writeFloat(distance);
        dest.writeByte((byte) (visited ? 1 : 0));
        dest.writeStringList(pictures);
        dest.writeStringList(descriptors);
        dest.writeStringList(nearBuildings);
        dest.writeTypedList(comments);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Building> CREATOR = new Creator<Building>() {
        @Override
        public Building createFromParcel(Parcel in) {
            return new Building(in);
        }

        @Override
        public Building[] newArray(int size) {
            return new Building[size];
        }
    };

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        visited = visited;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Set<String> getFeatures() {
        return features;
    }

    public void setFeatures(Set<String> features) {
        this.features = features;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getUrlDescription() {
        return urlDescription;
    }

    public void setUrlDescription(String urlDescription) {
        this.urlDescription = urlDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public MyLocation getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(MyLocation myLocation) {
        this.myLocation = myLocation;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public ArrayList<String> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(ArrayList<String> descriptors) {
        this.descriptors = descriptors;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public List<String> getNearBuildings() {
        return nearBuildings;
    }

    public void setNearBuildings(List<String> nearBuildings) {
        this.nearBuildings = nearBuildings;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("mainImage", mainImage);
        result.put("wikiDescription", urlDescription);
        result.put("myLocation",myLocation.toMap());

        return result;
    }

}
