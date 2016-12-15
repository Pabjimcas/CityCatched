package com.pabji.citycatched.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pabji.citycatched.R;
import com.pabji.citycatched.domain.components.BuildingMapComponent;
import com.pabji.citycatched.domain.components.DaggerBuildingMapComponent;
import com.pabji.citycatched.domain.modules.BuildingMapModule;
import com.pabji.citycatched.domain.scopes.HasComponent;
import com.pabji.citycatched.presentation.activities.base.BaseMVPActivity;
import com.pabji.citycatched.presentation.mvp.models.MyLocation;
import com.pabji.citycatched.presentation.mvp.presenters.BuildingMapPresenter;
import com.pabji.citycatched.presentation.mvp.views.BuildingMapView;
import com.pabji.citycatched.presentation.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BuildingMapActivity extends BaseMVPActivity<BuildingMapPresenter,BuildingMapView> implements BuildingMapView,HasComponent<BuildingMapComponent>, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private BuildingMapComponent component;

    private Unbinder unbind;

    SupportMapFragment mapFragment;

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap map;

    List<Marker> rangeList = new ArrayList<>();
    private String buildingImage;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, BuildingMapActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.initializeInjector();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_map);
        unbind = ButterKnife.bind(this);

        buildingImage = getIntent().getStringExtra("buildingImage");

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mGoogleApiClient != null && !mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()){
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @NonNull
    @Override
    public BuildingMapPresenter createPresenter() {
        return component.presenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind.unbind();
    }

    @Override
    public BuildingMapComponent getComponent() {
        return component;
    }

    private void initializeInjector() {
        this.component = DaggerBuildingMapComponent.builder()
                .cityCatchedApplicationComponent(getInjector())
                .buildingMapModule(new BuildingMapModule(this))
                .build();
        this.component.inject(this);
    }

    @Override
    public void showRangeLocation(MyLocation myLocation) {
        rangeList.add(map.addMarker(new MarkerOptions().position(myLocation.getLatLng())));
    }

    @Override
    public void showBuildingLocation(final MyLocation location) {
        if(map != null && location != null) {

            ImageUtils.loadCircledMarker(this, buildingImage, new ImageUtils.BitmapListener() {
                @Override
                public void onBitmapReady(Bitmap bitmap) {

                    map.addMarker(new MarkerOptions().position(location.getLatLng())
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(location.getLatLng(), 16));
                }
            });
            presenter.loadBuildingRange();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        presenter.loadBuildingLocation();
        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
