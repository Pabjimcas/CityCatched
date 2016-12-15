package com.pabji.citycatched.domain.modules;

import android.content.Context;

import com.pabji.citycatched.data.repositories.BuildingRepository;
import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.domain.features.GetBuildingCommentsInteractor;
import com.pabji.citycatched.domain.features.GetBuildingRangeInteractor;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.adapters.BuildingPicturesAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class BuildingMapModule extends BaseActivityModule {

    public BuildingMapModule(Context context) {
        super(context);
    }

    @Provides
    @PerActivity
    GetBuildingRangeInteractor provideBuildingRangeInteractor(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BuildingRepository buildingRepository){return new GetBuildingRangeInteractor(buildingRepository,threadExecutor,postExecutionThread);}

}
