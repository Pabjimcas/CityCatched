package com.pabji.citycatched.presentation.mvp.presenters.newBuilding;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.pabji.citycatched.presentation.activities.NewBuildingActivity;
import com.pabji.citycatched.presentation.mvp.models.MyLocation;
import com.pabji.citycatched.presentation.mvp.presenters.base.BasePresenter;
import com.pabji.citycatched.presentation.mvp.views.newBuilding.NewBuildingStep3View;
import com.pabji.citycatched.presentation.navigation.Router;

import javax.inject.Inject;

/**
 * Created by Pablo Jiménez Casado on 06/08/2016.
 */

public class NewBuildingStep3Presenter extends BasePresenter<NewBuildingStep3View> {

    @Inject
    Router router;
    private NewBuildingPresenter activityPresenter;
    private MyLocation myLocation;
    private Context context;

    @Inject
    public NewBuildingStep3Presenter() {
    }

    public void init(Context context) {
        this.context = context;
        this.activityPresenter = ((NewBuildingActivity) context).getPresenter();
        this.myLocation = activityPresenter.getBuildingLocation();
        showBuildingLocation();
        showBuildingAddress();
    }

    private void showBuildingLocation() {
        if ( myLocation != null) {
            getView().showLocation(myLocation.getLatLng());
        }
    }

    private void showBuildingAddress() {
        if ( myLocation != null) {
            getView().showAddress(myLocation.getFullAddress(context));
        }
    }

    public void setCurrentLocation(LatLng currentLocation) {
        myLocation.setLatitude(currentLocation.latitude);
        myLocation.setLongitude(currentLocation.longitude);
        activityPresenter.setBuildingLocation(myLocation);
        showBuildingAddress();
    }
}
