package com.pabji.citycatched.domain.modules;

import android.content.Context;

import com.pabji.citycatched.data.repositories.BuildingRepository;
import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.domain.features.GetBuildingCommentsInteractor;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.adapters.BuildingPicturesAdapter;
import com.pabji.citycatched.presentation.adapters.ScreenSlidePagerAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class BuildingGalleryModule extends BaseActivityModule {

    public BuildingGalleryModule(Context context) {
        super(context);
    }
}
