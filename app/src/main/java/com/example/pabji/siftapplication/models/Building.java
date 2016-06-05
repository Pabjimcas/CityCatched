package com.example.pabji.siftapplication.models;

/**
 * Created by Daniel on 12/5/16.
 */
public class Building {

    String description;
    String name;
    Double latitude;
    Double longitude;
    String url_image;
    String intro;
    String id;

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String url_description) {
        this.description = url_description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public Building(String description, String name, Double latitude, Double longitude, String url_image, String id, String intro) {
        this.description = description;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.url_image = url_image;
        this.id = id;
        this.intro = intro;
    }
}
