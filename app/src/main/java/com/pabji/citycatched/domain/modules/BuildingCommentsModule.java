package com.pabji.citycatched.domain.modules;

import android.content.Context;

import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.adapters.BuildingCommentsAdapter;
import com.pabji.citycatched.presentation.adapters.BuildingPicturesAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class BuildingCommentsModule extends BaseActivityModule {

    public BuildingCommentsModule(Context context) {
        super(context);
    }

    @Provides
    @PerActivity
    BuildingCommentsAdapter provideBuildingCommentsAdapter(){return new BuildingCommentsAdapter();}
}
