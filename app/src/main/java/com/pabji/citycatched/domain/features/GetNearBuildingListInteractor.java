package com.pabji.citycatched.domain.features;

import com.pabji.citycatched.data.repositories.BuildingRepository;
import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.domain.features.base.BaseInteractorImpl;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.models.Descriptor;
import com.pabji.citycatched.presentation.mvp.models.MyLocation;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Pablo Jiménez Casado on 23/10/2016.
 */
@PerActivity
public class GetNearBuildingListInteractor extends BaseInteractorImpl<List<Building>> {

    private BuildingRepository buildingRepository;
    private String uid;

    @Inject
    public GetNearBuildingListInteractor(BuildingRepository buildingRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.buildingRepository = buildingRepository;
    }

    public void execute(String uid,Subscriber<List<Building>> subscriber) {
        this.uid = uid;
        super.executeInMainThread(subscriber);
    }

    @Override
    protected Observable<List<Building>> buildFeatureObservable() {
        return buildingRepository.getNearBuildings(uid);
    }
}
