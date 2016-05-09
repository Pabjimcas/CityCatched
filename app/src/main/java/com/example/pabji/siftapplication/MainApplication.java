package com.example.pabji.siftapplication;

import android.app.Application;

import com.firebase.client.Firebase;


/**
 * Created by pabji on 01/05/2016.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
