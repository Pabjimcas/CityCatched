package com.example.pabji.siftapplication.localizacion;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.pabji.siftapplication.R;
import com.example.pabji.siftapplication.models.Building;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class LocalizacionActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LocalizacionActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 1;

    private TextView tvLocalizacion;
    private TextView tvRango;
    private GoogleApiClient mGoogleApiClient;
    private Firebase firebase;
    private List builds = new ArrayList();
    private Location lastLocation = null;
    private boolean dentro = false;
    private boolean fuera = false;
    private double la;
    private double lo;
    private ArrayList aux = new ArrayList<>();
    private LocationRequest locationRequest;
    private boolean inside;
    public List nearBuilding = new ArrayList();
    private String n = "";
    private boolean outside;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        setContentView(R.layout.activity_main2);

        tvLocalizacion = (TextView) findViewById(R.id.tv_localizacion);
        tvRango = (TextView) findViewById(R.id.tv_rango);
        getLocationFirebase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                Log.v(TAG, "Permission is granted");
                lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        if (lastLocation != null) {
            tvLocalizacion.setText(lastLocation.getLatitude() + " , " + lastLocation.getLongitude());
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            Log.v(TAG, "Permission: " + permissions[1] + "was " + grantResults[1]);
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (lastLocation != null) {
                tvLocalizacion.setText(lastLocation.getLatitude() + " , " + lastLocation.getLongitude());
            }
        } else {
            finish();
        }
    }

    public void getLocationFirebase() {
        firebase = new Firebase("https://city-catched.firebaseio.com/buildings");
        firebase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    System.out.println(postSnapshot.getKey());
                    String descripcion = postSnapshot.child("url_description").getValue(String.class);
                    Double latitude = postSnapshot.child("latitude").getValue(Double.class);
                    Double longitude = postSnapshot.child("longitude").getValue(Double.class);
                    String name = postSnapshot.child("name").getValue(String.class);
                    String url_image = postSnapshot.child("url_image").getValue(String.class);
                    Building build = new Building(descripcion, name, latitude, longitude, url_image);
                    builds.add(build);

                    if (lastLocation.getLatitude() < latitude + 0.0015 && lastLocation.getLatitude() > latitude - 0.0015 &&
                            lastLocation.getLongitude() < longitude + 0.0015 && lastLocation.getLongitude() > longitude - 0.0015) {
                        inside = true;
                        nearBuilding.add(build);
                        n = n + " " + build.getName();

                    } else {
                        outside = true;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + lastLocation.getLatitude() + "," + lastLocation.getLongitude() + "?q=search/monumentos+de+salamanca"));
                        startActivity(intent);
                    }
                }

                if (nearBuilding.size() > 0) {
                    tvRango.setText("Estas en el rango de" + n);
                    inside = false;

                } else {
                    tvRango.setText("nooo Estas en el rango");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}




