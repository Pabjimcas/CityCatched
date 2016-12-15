package com.pabji.citycatched.domain.features;

import com.pabji.citycatched.data.repositories.BuildingRepository;
import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.domain.features.base.BaseInteractorImpl;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.mvp.models.Comment;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Pablo Jiménez Casado on 23/10/2016.
 */
@PerActivity
public class SendBuildingCommentInteractor extends BaseInteractorImpl<String> {

    private BuildingRepository buildingRepository;
    private Comment comment;
    private String uid;

    @Inject
    public SendBuildingCommentInteractor(BuildingRepository buildingRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.buildingRepository = buildingRepository;
    }

    public void execute(String uid,Comment comment,Subscriber<String> subscriber) {
        this.comment = comment;
        this.uid = uid;
        super.executeInMainThread(subscriber);
    }

    @Override
    protected Observable<String> buildFeatureObservable() {
        return buildingRepository.sendComment(uid,comment);
    }
}
