package com.pabji.citycatched.domain.modules;

import android.content.Context;

import com.pabji.citycatched.data.repositories.BuildingRepository;
import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.domain.features.GetBuildingCommentsInteractor;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.adapters.BuildingPicturesAdapter;
import com.pabji.citycatched.presentation.adapters.NoRecognizedAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class BuildingModule extends BaseActivityModule {

    public BuildingModule(Context context) {
        super(context);
    }

    @Provides
    @PerActivity
    BuildingPicturesAdapter provideBuildingPicturesAdapter(){return new BuildingPicturesAdapter();}

}
