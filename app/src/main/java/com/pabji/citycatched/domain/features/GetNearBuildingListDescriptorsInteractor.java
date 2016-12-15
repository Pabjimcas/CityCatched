package com.pabji.citycatched.domain.features;

import com.pabji.citycatched.data.repositories.BuildingRepository;
import com.pabji.citycatched.data.repositories.OpenCVRepository;
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
public class GetNearBuildingListDescriptorsInteractor extends BaseInteractorImpl<Descriptor> {

    @Inject BuildingRepository buildingRepository;

    @Inject
    public GetNearBuildingListDescriptorsInteractor(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    public void execute(Subscriber<Descriptor> subscriber) {
        super.executeInMainThread(subscriber);
    }

    @Override
    protected Observable<Descriptor> buildFeatureObservable() {
        return buildingRepository.getNearBuildingDescriptors();
    }
}
