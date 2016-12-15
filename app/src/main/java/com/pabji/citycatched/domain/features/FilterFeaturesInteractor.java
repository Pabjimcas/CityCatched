package com.pabji.citycatched.domain.features;

import com.pabji.citycatched.data.repositories.BuildingRepository;
import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.domain.features.base.BaseInteractorImpl;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.models.Comment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Pablo Jiménez Casado on 23/10/2016.
 */
@PerActivity
public class FilterFeaturesInteractor extends BaseInteractorImpl<Map<String,List<String>>> {

    private final BuildingRepository buildingRepository;

    @Inject
    public FilterFeaturesInteractor(BuildingRepository buildingRepository,ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.buildingRepository = buildingRepository;
    }

    public void execute(Subscriber<Map<String,List<String>>> subscriber) {
        super.executeInMainThread(subscriber);
    }

    @Override
    protected Observable<Map<String,List<String>>> buildFeatureObservable() {
        return buildingRepository.getBuildingsFeatures();
    }
}
