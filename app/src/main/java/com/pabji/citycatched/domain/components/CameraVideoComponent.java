package com.pabji.citycatched.domain.components;

import com.pabji.citycatched.domain.modules.CameraVideoModule;
import com.pabji.citycatched.domain.modules.MainActivityModule;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.activities.CameraVideoActivity;
import com.pabji.citycatched.presentation.activities.MainActivity;
import com.pabji.citycatched.presentation.fragments.CameraVideoFragment;
import com.pabji.citycatched.presentation.mvp.presenters.CameraVideoPresenter;

import dagger.Component;

@PerActivity
@Component(dependencies = CityCatchedApplicationComponent.class,modules = CameraVideoModule.class)
public interface CameraVideoComponent {
   void inject(CameraVideoActivity activity);

   CameraVideoPresenter createPresenter();
}
