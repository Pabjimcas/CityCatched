package com.pabji.citycatched.presentation.mvp.presenters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pabji.citycatched.R;
import com.pabji.citycatched.domain.features.GetBuildingListVisitedInteractor;
import com.pabji.citycatched.domain.features.GetBuildingVisitedInteractor;
import com.pabji.citycatched.presentation.mvp.presenters.base.BasePresenter;
import com.pabji.citycatched.data.repositories.ApiClientRepository;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.navigation.Router;
import com.pabji.citycatched.presentation.mvp.views.BuildingListVisitedView;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class BuildingListVisitedPresenter extends BasePresenter<BuildingListVisitedView>{

    @Inject
    Router router;


    @Inject
    GetBuildingListVisitedInteractor getBuildingListVisitedInteractor;

    @Inject
    GetBuildingVisitedInteractor getBuildingVisitedInteractor;

    Context context;
    private FirebaseUser mAuth;

    @Inject
    public BuildingListVisitedPresenter() {
    }

    public void init(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        loadBuildingVisited();
    }

    public void loadBuildingVisited(){
        getView().showProgressDialog(R.string.app_name);
        getBuildingListVisitedInteractor.execute(mAuth.getUid(), new Subscriber<Map<String,List<Building>>>() {
            @Override
            public void onCompleted() {
                getView().hideProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                getView().hideProgressDialog();
            }

            @Override
            public void onNext(Map<String,List<Building>> buildingMap) {
                getView().showBuildingVisited(buildingMap);
            }
        });
    }

    public void getBuilding(String key, String buildingId) {
        getBuildingVisitedInteractor.execute(key,buildingId, new Subscriber<Building>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Building building) {
                router.openDetailBuilding(building);
            }
        });

    }
}
