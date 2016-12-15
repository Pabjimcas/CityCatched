package com.pabji.citycatched.domain.features;

import com.pabji.citycatched.data.repositories.BuildingRepository;
import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.domain.features.base.BaseInteractorImpl;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.models.Comment;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Pablo Jiménez Casado on 23/10/2016.
 */
@PerActivity
public class GetBuildingCommentsInteractor extends BaseInteractorImpl<List<Comment>> {

    private BuildingRepository buildingRepository;

    @Inject
    public GetBuildingCommentsInteractor(BuildingRepository buildingRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.buildingRepository = buildingRepository;
    }

    public void execute(Subscriber<List<Comment>> subscriber) {
        super.executeInMainThread(subscriber);
    }

    @Override
    protected Observable<List<Comment>> buildFeatureObservable() {
        return buildingRepository.getBuildingComments();
    }
}
