package com.pabji.citycatched.domain.features;

import com.pabji.citycatched.data.repositories.BuildingRepository;
import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.domain.features.base.BaseInteractorImpl;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.models.MyLocation;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Pablo Jiménez Casado on 23/10/2016.
 */
@PerActivity
public class GetBuildingInteractor extends BaseInteractorImpl<Building> {

    private BuildingRepository buildingRepository;
    private String buildingId;

    @Inject
    public GetBuildingInteractor(BuildingRepository buildingRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.buildingRepository = buildingRepository;
    }

    public void execute(String buildingId,Subscriber<Building> subscriber) {
        this.buildingId = buildingId;
        super.executeInMainThread(subscriber);
    }

    @Override
    protected Observable<Building> buildFeatureObservable() {
        return buildingRepository.getBuilding(buildingId);
    }
}
