package com.pabji.citycatched.presentation.mvp.presenters;

import android.content.Context;

import com.pabji.citycatched.domain.features.GetBuildingLocationInteractor;
import com.pabji.citycatched.domain.features.GetBuildingRangeInteractor;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.models.MyLocation;
import com.pabji.citycatched.presentation.mvp.presenters.base.BasePresenter;
import com.pabji.citycatched.presentation.mvp.views.BuildingMapView;
import com.pabji.citycatched.presentation.navigation.Router;

import javax.inject.Inject;

import rx.Subscriber;

public class BuildingMapPresenter extends BasePresenter<BuildingMapView> {

    @Inject
    Router router;

    @Inject
    GetBuildingRangeInteractor getBuildingRangeInteractor;

    @Inject
    GetBuildingLocationInteractor getBuildingLocationInteractor;

    @Inject
    public BuildingMapPresenter() {
    }

    public void loadBuildingRange() {
        getBuildingRangeInteractor.execute(new Subscriber<MyLocation>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(MyLocation myLocation) {
                getView().showRangeLocation(myLocation);
            }
        });
    }


    public void loadBuildingLocation() {
        getBuildingLocationInteractor.execute(new Subscriber<MyLocation>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(MyLocation location) {
                getView().showBuildingLocation(location);
            }
        });
    }
}
