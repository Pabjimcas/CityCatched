package com.pabji.citycatched.domain.features;

import com.pabji.citycatched.data.repositories.BuildingRepository;
import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.domain.features.base.BaseInteractorImpl;
import com.pabji.citycatched.domain.scopes.PerActivity;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Pablo Jiménez Casado on 23/10/2016.
 */
@PerActivity
public class AddBuildingDescriptorInteractor extends BaseInteractorImpl<String> {

    private BuildingRepository buildingRepository;
    private String contentDescriptor;

    @Inject
    public AddBuildingDescriptorInteractor(BuildingRepository buildingRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.buildingRepository = buildingRepository;
    }

    public void execute(String contentDescriptor,Subscriber<String> subscriber) {
        this.contentDescriptor = contentDescriptor;
        super.executeInMainThread(subscriber);
    }

    @Override
    protected Observable<String> buildFeatureObservable() {
        return buildingRepository.addBuildingDescriptor(contentDescriptor);
    }
}
