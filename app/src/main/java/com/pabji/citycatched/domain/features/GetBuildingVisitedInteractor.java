package com.pabji.citycatched.domain.features;

import com.pabji.citycatched.data.repositories.BuildingRepository;
import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.domain.features.base.BaseInteractorImpl;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.mvp.models.Building;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Pablo Jiménez Casado on 23/10/2016.
 */
@PerActivity
public class GetBuildingVisitedInteractor extends BaseInteractorImpl<Building> {

    private BuildingRepository buildingRepository;
    private String buildingId;
    private String key;

    @Inject
    public GetBuildingVisitedInteractor(BuildingRepository buildingRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.buildingRepository = buildingRepository;
    }

    public void execute(String key,String buildingId,Subscriber<Building> subscriber) {
        this.buildingId = buildingId;
        this.key = key;
        super.executeInMainThread(subscriber);
    }

    @Override
    protected Observable<Building> buildFeatureObservable() {
        return buildingRepository.getBuildingVisited(key,buildingId);
    }
}
