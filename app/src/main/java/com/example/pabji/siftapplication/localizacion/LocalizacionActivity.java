package com.example.pabji.siftapplication.localizacion;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.pabji.siftapplication.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by pabji on 09/05/2016.
 */
public class LocalizacionActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LocalizacionActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 1;

    private TextView tvLocalizacion;
    private TextView tvRango;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //tvLocalizacion = (TextView) findViewById(R.id.tv_localizacion);
        //tvRango = (TextView) findViewById(R.id.tv_rango);

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

        Location lastLocation = null;
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
            Double locationLatitudeCasaConchas = 40.9628577;
//            Double locationLatitudeCasaConchas = 40.9754476;
//            Double locationLongitudeCasaConchas = -5.6682167;
            Double locationLongitudeCasaConchas = -5.6660739;
            tvLocalizacion.setText(lastLocation.getLatitude() + " , " + lastLocation.getLongitude());
            if (lastLocation.getLatitude() < locationLatitudeCasaConchas + 0.00001 && lastLocation.getLatitude() > locationLatitudeCasaConchas - 0.00001 &&
                    lastLocation.getLongitude() < locationLongitudeCasaConchas + 0.00001 && lastLocation.getLongitude() > locationLongitudeCasaConchas - 0.00001) {
                tvRango.setText("Estas en el rango guapeton");
            }else{
                tvRango.setText("No estas en el rango guapeton");
            }

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
                Double locationLatitudeCasaConchas = 40.9628577;
//                Double locationLatitudeCasaConchas = 40.9754476;
//                Double locationLongitudeCasaConchas = -5.6682167;
                Double locationLongitudeCasaConchas = -5.6660739;
                tvLocalizacion.setText(lastLocation.getLatitude() + " , " + lastLocation.getLongitude());

                if (lastLocation.getLatitude() < locationLatitudeCasaConchas + 0.00001 && lastLocation.getLatitude() > locationLatitudeCasaConchas - 0.00001 &&
                        lastLocation.getLongitude() < locationLongitudeCasaConchas + 0.00001 && lastLocation.getLongitude() > locationLongitudeCasaConchas - 0.00001) {
                    tvRango.setText("Estas en el rango guapeton");
                }else{
                    tvRango.setText("No estas en el rango guapeton");

                }
            }
        } else {
            finish();
        }
    }


    public void pablo263(){

    }
}

