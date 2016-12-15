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
public class CameraVideoModule extends BaseActivityModule {

    public CameraVideoModule(Context context) {
        super(context);
    }

}
