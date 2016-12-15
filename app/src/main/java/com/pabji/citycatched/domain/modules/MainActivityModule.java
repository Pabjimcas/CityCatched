package com.pabji.citycatched.domain.modules;

import android.content.Context;

import com.pabji.citycatched.data.repositories.OpenCVRepository;
import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.domain.features.OpenCVInteractor;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.adapters.NoRecognizedAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule extends BaseActivityModule {

    public MainActivityModule(Context context) {
        super(context);
    }

    @Provides
    @PerActivity
    OpenCVInteractor provideOpenCVInteractor(OpenCVRepository openCVRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread){return new OpenCVInteractor(openCVRepository,threadExecutor,postExecutionThread);}

    @Provides
    @PerActivity
    NoRecognizedAdapter provideNoRecognizedAdapter(){return new NoRecognizedAdapter();}

}
