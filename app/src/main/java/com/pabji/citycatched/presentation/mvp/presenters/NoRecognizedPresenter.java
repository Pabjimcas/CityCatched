package com.pabji.citycatched.presentation.mvp.presenters;

import android.content.Context;
import android.os.Bundle;

import com.pabji.citycatched.domain.features.AddBuildingDescriptorInteractor;
import com.pabji.citycatched.domain.features.FilterFeaturesInteractor;
import com.pabji.citycatched.domain.features.GetNearBuildingInteractor;
import com.pabji.citycatched.domain.features.GetNearBuildingListInteractor;
import com.pabji.citycatched.presentation.mvp.models.MyLocation;
import com.pabji.citycatched.presentation.mvp.presenters.base.BasePresenter;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.navigation.Router;
import com.pabji.citycatched.presentation.mvp.views.NoRecognizedView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Pablo Jiménez Casado on 06/08/2016.
 */

public class NoRecognizedPresenter extends BasePresenter<NoRecognizedView> {

    @Inject
    Router router;

    @Inject
    GetNearBuildingListInteractor getNearBuildingListInteractor;

    @Inject
    GetNearBuildingInteractor getNearBuildingInteractor;

    @Inject
    AddBuildingDescriptorInteractor descriptorInteractor;

    @Inject
    FilterFeaturesInteractor getFeaturesInteractor;

    private Context context;
    Map<String,List<String>> features = new HashMap<>();
    private String descriptor;
    private Iterator<Map.Entry<String, List<String>>> mapIterator;
    private String currentKey;
    private List<Building> nearBuildingList = new ArrayList<>();

    @Inject
    public NoRecognizedPresenter() {
    }

    public void init(Context context, Bundle extras) {
        descriptor = extras.getString("descriptor");
        this.context = context;
        loadFeatures();
    }

    private void getNearBuildings() {
        getNearBuildingListInteractor.execute(new Subscriber<List<Building>>() {
            @Override
            public void onCompleted() {
                getView().showNoRecognizedView(nearBuildingList);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Building> buildingList) {
                nearBuildingList = buildingList;
            }
        });
    }

    private void loadFeatures() {
        getFeaturesInteractor.execute(new Subscriber<Map<String, List<String>>>() {
            @Override
            public void onCompleted() {
                getView().hideProgressDialog();
                if(!features.isEmpty()) {
                    mapIterator = features.entrySet().iterator();
                    startQuestion();
                }
            }

            @Override
            public void onError(Throwable e) {
                getView().hideProgressDialog();
            }

            @Override
            public void onNext(Map<String, List<String>> featuresMap) {
                features = featuresMap;
            }
        });
    }

    private void startQuestion() {
        if(mapIterator != null && mapIterator.hasNext()){
            currentKey = mapIterator.next().getKey();
            if(features.get(currentKey).size()>0){
                getView().showQuestion("¿Este edificio tiene "+features.get(currentKey).get(0));
            }
        }else{
            getNearBuildings();
        }
    }

    public void addDescriptor(){
        descriptorInteractor.execute(descriptor, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        });
    }

    public void selectBuilding(String buildingId) {
        openBuildingDetailAndAddDescriptor(buildingId);
    }

    public void clickNo() {
        startQuestion();
    }

    public void clickYes() {
        openBuildingDetailAndAddDescriptor(currentKey);
    }

    public List<Building> getNearBuildingList(){
        return nearBuildingList;
    }

    public void openBuildingDetailAndAddDescriptor(String buildingId){
        getNearBuildingInteractor.execute(buildingId, new Subscriber<Building>() {
            @Override
            public void onCompleted() {
                router.finishActivity(context);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Building building) {
                addDescriptor();
                router.openDetailBuilding(building);
            }
        });
    }
}
