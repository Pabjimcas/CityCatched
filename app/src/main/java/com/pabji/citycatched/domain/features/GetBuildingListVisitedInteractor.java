package com.pabji.citycatched.domain.features;

import com.pabji.citycatched.data.repositories.BuildingRepository;
import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.domain.features.base.BaseInteractorImpl;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.mvp.models.Building;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Pablo Jiménez Casado on 23/10/2016.
 */
@PerActivity
public class GetBuildingListVisitedInteractor extends BaseInteractorImpl<Map<String,List<Building>>> {

    private BuildingRepository buildingRepository;
    private String uid;

    @Inject
    public GetBuildingListVisitedInteractor(BuildingRepository buildingRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.buildingRepository = buildingRepository;
    }

    public void execute(String uid,Subscriber<Map<String,List<Building>>> subscriber) {
        this.uid = uid;
        super.executeInMainThread(subscriber);
    }

    @Override
    protected Observable<Map<String,List<Building>>> buildFeatureObservable() {
        return buildingRepository.getBuildingsVisited(uid);
    }
}
