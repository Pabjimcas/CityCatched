package com.pabji.citycatched.domain.features;

import android.location.Location;

import com.pabji.citycatched.data.repositories.BuildingRepository;
import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.domain.features.base.BaseInteractorImpl;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.mvp.models.MyLocation;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Pablo Jiménez Casado on 23/10/2016.
 */
@PerActivity
public class UpdateMyCurrentLocationInteractor extends BaseInteractorImpl<MyLocation> {

    private BuildingRepository buildingRepository;
    private Location location;

    @Inject
    public UpdateMyCurrentLocationInteractor(BuildingRepository buildingRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.buildingRepository = buildingRepository;
    }

    public void execute(Location location,Subscriber<MyLocation> subscriber) {
        this.location = location;
        super.executeInMainThread(subscriber);
    }

    @Override
    protected Observable<MyLocation> buildFeatureObservable() {
        return buildingRepository.updateMyCurrentLocation(location);
    }
}
