package com.pabji.citycatched.domain.components;

import android.content.Context;

import com.pabji.citycatched.data.repositories.BuildingRepository;
import com.pabji.citycatched.data.repositories.PictureStorageRepository;
import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.domain.modules.CityCatchedApplicationModule;
import com.pabji.citycatched.domain.utils.PermissionUtils;
import com.pabji.citycatched.presentation.activities.base.BaseActivity;
import com.pabji.citycatched.data.repositories.ApiClientRepository;
import com.pabji.citycatched.presentation.navigation.Router;
import com.pabji.citycatched.data.repositories.OpenCVRepository;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by pabji on 15/06/2016.
 */
@Singleton
@Component(modules = CityCatchedApplicationModule.class)
public interface CityCatchedApplicationComponent {
    void inject(BaseActivity baseActivity);
    @Named("ApplicationContext")
    Context getContext();
    Router getRouter();
    ThreadExecutor getThreadExecutor();
    PostExecutionThread getPostExecutionThread();
    ApiClientRepository getApiClientRepository();
    OpenCVRepository getOpenCVRepository();
    BuildingRepository getBuildingRepository();
    PictureStorageRepository getPictureStorageRepository();
    PermissionUtils getPermissionUtils();
}
