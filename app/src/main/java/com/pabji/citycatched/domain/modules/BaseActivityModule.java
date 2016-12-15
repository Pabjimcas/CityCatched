package com.pabji.citycatched.domain.modules;

import android.content.Context;

import com.pabji.citycatched.domain.scopes.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pabji on 16/06/2016.
 */
@Module
public class BaseActivityModule {

    private Context context;

    public BaseActivityModule(Context context){
        this.context = context;
    }

    @PerActivity
    @Named("ActivityContext")  @Provides
    Context provideActivityContext(){return context;}

}
