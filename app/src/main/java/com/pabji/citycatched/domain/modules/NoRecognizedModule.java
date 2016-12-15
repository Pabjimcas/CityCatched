package com.pabji.citycatched.domain.modules;

import android.content.Context;

import com.pabji.citycatched.data.repositories.BuildingRepository;
import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.domain.features.GetNearBuildingListInteractor;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.adapters.NoRecognizedAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class NoRecognizedModule extends BaseActivityModule {

    public NoRecognizedModule(Context context) {
        super(context);
    }

    @Provides
    @PerActivity
    NoRecognizedAdapter provideNoRecognizedAdapter(){return new NoRecognizedAdapter();}

    @Provides
    @PerActivity
    GetNearBuildingListInteractor provideBuildingInteractor(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BuildingRepository buildingRepository){return new GetNearBuildingListInteractor(buildingRepository,threadExecutor,postExecutionThread);}
}
