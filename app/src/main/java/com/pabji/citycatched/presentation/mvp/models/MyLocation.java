package com.pabji.citycatched.presentation.mvp.models;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by usuario on 2/08/16.
 */
public class MyLocation implements Parcelable {

    public Double latitude;
    public Double longitude;

    public MyLocation(Location location){
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    public MyLocation() {}

    public MyLocation(GeoLocation location) {
        this.latitude = location.latitude;
        this.longitude = location.longitude;
    }

    protected MyLocation(Parcel in) {
    }

    public MyLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyLocation> CREATOR = new Creator<MyLocation>() {
        @Override
        public MyLocation createFromParcel(Parcel in) {
            return new MyLocation(in);
        }

        @Override
        public MyLocation[] newArray(int size) {
            return new MyLocation[size];
        }
    };

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("latitude", latitude);
        result.put("longitude", longitude);

        return result;
    }

    @Exclude
    public String getAddress(Context context){
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude,longitude, 1);
            if (addresses.size() > 0)
                return addresses.get(0).getLocality() + "-" + addresses.get(0).getThoroughfare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Exclude
    public String getPostalCode(Context context){
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude,longitude, 1);
            if (addresses.size() > 0)
                return addresses.get(0).getPostalCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Exclude
    public String getFullAddress(Context context){
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude,longitude, 1);
            if (addresses.size() > 0){
                int maxIndex = addresses.get(0).getMaxAddressLineIndex();
                String address = "";
                for(int i = 0; i < maxIndex; i++){
                    address += addresses.get(0).getAddressLine(i);
                    if(i != maxIndex -1){
                        address += ", ";
                    }
                }
                return address;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Exclude
    public String getCity(Context context){
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude,longitude, 1);
            if (addresses.size() > 0)
                return addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Exclude
    public String getCountry(Context context){
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude,longitude, 1);
            if (addresses.size() > 0)
                return addresses.get(0).getCountryName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Exclude
    public float getDistanceTo(MyLocation location){
        float distance[] = new float[1];
        if(location != null) {
            Location.distanceBetween(latitude, longitude, location.getLatitude(), location.getLongitude(), distance);
        }
        return distance[0];
    }

    @Exclude
    public Double getLatitude() {
        return latitude;
    }

    @Exclude
    public LatLng getLatLng(){
        return new LatLng(latitude,longitude);
    }

    @Exclude
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Exclude
    public Double getLongitude() {
        return longitude;
    }

    @Exclude
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
