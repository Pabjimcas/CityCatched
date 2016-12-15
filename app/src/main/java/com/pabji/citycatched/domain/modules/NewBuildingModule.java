package com.pabji.citycatched.domain.modules;

import android.content.Context;

import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.adapters.AddPicturesAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class NewBuildingModule extends BaseActivityModule {

    public NewBuildingModule(Context context) {
        super(context);
    }

    @Provides
    @PerActivity
    AddPicturesAdapter provideAddPicturesAdapter(){return new AddPicturesAdapter();}

}
