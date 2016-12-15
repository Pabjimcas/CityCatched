package com.pabji.citycatched.domain.components;

import com.pabji.citycatched.domain.modules.MainActivityModule;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.activities.MainActivity;
import com.pabji.citycatched.presentation.fragments.BuildingListVisitedFragment;
import com.pabji.citycatched.presentation.fragments.CameraVideoFragment;
import com.pabji.citycatched.presentation.fragments.MainFragment;
import com.pabji.citycatched.presentation.mvp.presenters.BuildingListVisitedPresenter;

import dagger.Component;

@PerActivity
@Component(dependencies = CityCatchedApplicationComponent.class,modules = MainActivityModule.class)
public interface MainActivityComponent {
   void inject(MainActivity mainActivity);

   void inject(CameraVideoFragment cameraVideoFragment);

   void inject(MainFragment mainFragment);

   void inject(BuildingListVisitedFragment buildingListVisitedFragment);

   BuildingListVisitedPresenter createPresenter();
}
