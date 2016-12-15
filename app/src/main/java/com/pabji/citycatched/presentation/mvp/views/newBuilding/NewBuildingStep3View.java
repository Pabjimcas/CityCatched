package com.pabji.citycatched.presentation.mvp.views.newBuilding;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.pabji.citycatched.presentation.mvp.models.MyLocation;
import com.pabji.citycatched.presentation.mvp.views.base.BaseView;

import java.util.List;

/**
 * Created by Pablo Jiménez Casado on 26/11/2016.
 */
public interface NewBuildingStep3View extends BaseView {
    void showLocation(LatLng latLng);
    void showAddress(String address);
}
