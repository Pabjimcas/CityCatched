package com.pabji.citycatched.domain.modules;

import android.content.Context;

import com.pabji.citycatched.data.datasources.FirebaseStorageDatasource;
import com.pabji.citycatched.data.repositories.ApiClientRepository;
import com.pabji.citycatched.data.repositories.BuildingRepository;
import com.pabji.citycatched.data.repositories.PictureStorageRepository;
import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.domain.executor.ThreadExecutorImpl;
import com.pabji.citycatched.domain.executor.UIThread;
import com.pabji.citycatched.domain.utils.Recognizer;
import com.pabji.citycatched.presentation.navigation.Router;
import com.pabji.citycatched.data.datasources.FirebaseDatasource;
import com.pabji.citycatched.data.repositories.OpenCVRepository;
import com.pabji.citycatched.domain.utils.PermissionUtils;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pabji on 15/06/2016.
 */
@Module
public class CityCatchedApplicationModule {

    private final Context context;

    public CityCatchedApplicationModule(Context context) {
        this.context = context;
    }

    @Named("ApplicationContext") @Provides @Singleton Context provideApplicationContext(){
        return this.context;
    }

    @Provides @Singleton
    Router provideRouter(){
        return new Router(context);
    }

    @Provides @Singleton
    ThreadExecutor provideThreadExecutor(ThreadExecutorImpl threadExecutor){
        return threadExecutor;
    }

    @Provides @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread){
        return uiThread;
    }


    @Provides
    @Singleton
    ApiClientRepository provideApiClientRepository(){return new ApiClientRepository();}

    @Provides
    @Singleton
    OpenCVRepository provideOpenCVRepository(){return new OpenCVRepository(context);}

    @Provides
    @Singleton
    BuildingRepository provideBuildingRepository(){return new FirebaseDatasource(context);}

    @Provides
    @Singleton
    PictureStorageRepository providePictureStorageRepository(){return new FirebaseStorageDatasource(context);}

    @Provides
    @Singleton
    PermissionUtils providePermissionUtils(){return new PermissionUtils();}

}
