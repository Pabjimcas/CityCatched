package com.pabji.citycatched.presentation.fragments.newBuilding;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pabji.citycatched.R;
import com.pabji.citycatched.presentation.activities.NewBuildingActivity;
import com.pabji.citycatched.presentation.fragments.base.BaseMVPFragment;
import com.pabji.citycatched.presentation.mvp.models.MyLocation;
import com.pabji.citycatched.presentation.mvp.presenters.newBuilding.NewBuildingStep3Presenter;
import com.pabji.citycatched.presentation.mvp.views.newBuilding.NewBuildingStep3View;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Pablo Jiménez Casado on 07/08/2016.
 */

public class NewBuildingStep3Fragment extends BaseMVPFragment<NewBuildingStep3Presenter,NewBuildingStep3View> implements NewBuildingStep3View, OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener{

    private Unbinder unbind;
    private View view;
    private SupportMapFragment mapFragment;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap map;
    private LatLng currentLocation;
    private Marker buildingMarker;

    public static NewBuildingStep3Fragment newInstance() {
        NewBuildingStep3Fragment frag = new NewBuildingStep3Fragment();
        return frag;
    }

    @BindView(R.id.et_address)
    EditText etAddress;

    @Inject
    public NewBuildingStep3Fragment() {
        setRetainInstance(true);
    }

    @Override
    public NewBuildingStep3Presenter createPresenter() {
        return ((NewBuildingActivity)getActivity()).getComponent().newBuildingStep3Presenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_new_building_step3,container,false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        unbind = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((NewBuildingActivity)getActivity()).getComponent().inject(this);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        presenter.init(getActivity());
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void showError(int error) {

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
    public void onMapClick(LatLng latLng) {
        currentLocation = latLng;
        buildingMarker.remove();
        moveToLocation();
        presenter.setCurrentLocation(currentLocation);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        moveToLocation();
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public void showLocation(LatLng latLng) {
        currentLocation = latLng;
        moveToLocation();
    }

    private void moveToLocation() {
        if(map != null && currentLocation != null) {
            buildingMarker = map.addMarker(new MarkerOptions().position(currentLocation));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
        }
    }

    @Override
    public void showAddress(String address) {
        etAddress.setText(address);
    }

}
