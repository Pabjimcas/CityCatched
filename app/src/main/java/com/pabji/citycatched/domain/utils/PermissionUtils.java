package com.pabji.citycatched.domain.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Pablo Jiménez Casado on 07/08/2016.
 */

@Singleton
public class PermissionUtils {


    @Inject
    public PermissionUtils() {
    }

    /*
    PERMISSION GRANT CHECK METHODS
     */
    public boolean isLocationPermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isWriteStoragePermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isAccountsPermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(final PermissionListener listener, final String permission) {

        Dexter.checkPermission(new PermissionListener() {
            @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                listener.onPermissionGranted(response);
            }

            @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                listener.onPermissionDenied(response);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
                listener.onPermissionRationaleShouldBeShown(permission, token);
            }
        }, permission);
    }

    public void requestLocationPermission(final PermissionListener listener) {
        requestPermission(listener, Manifest.permission.ACCESS_FINE_LOCATION);
    }


    public void requestWriteStoragePermission(final PermissionListener listener) {
        requestPermission(listener, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void requestReadStoragePermission(final PermissionListener listener){
        requestPermission(listener,Manifest.permission.READ_EXTERNAL_STORAGE);
    }

}
