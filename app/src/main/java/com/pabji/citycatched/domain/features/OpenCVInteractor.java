package com.pabji.citycatched.domain.features;

import com.pabji.citycatched.data.repositories.OpenCVRepository;
import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.domain.features.base.BaseInteractorImpl;
import com.pabji.citycatched.domain.scopes.PerActivity;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Pablo Jiménez Casado on 22/10/2016.
 */

@PerActivity
public class OpenCVInteractor extends BaseInteractorImpl<Boolean> {

    private OpenCVRepository openCVRepository;

    @Inject
    public OpenCVInteractor(OpenCVRepository openCVRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.openCVRepository = openCVRepository;
    }

    @Override
    protected Observable<Boolean> buildFeatureObservable() {
        return openCVRepository.isOpenCvLoaded();
    }
}
