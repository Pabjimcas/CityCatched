package com.pabji.citycatched.domain.components;

import com.pabji.citycatched.domain.modules.LoginFirebaseModule;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.activities.LoginActivity;
import com.pabji.citycatched.presentation.mvp.presenters.LoginFirebasePresenter;

import dagger.Component;

@PerActivity
@Component(dependencies = CityCatchedApplicationComponent.class,modules = LoginFirebaseModule.class)
public interface LoginFirebaseComponent {
   void inject(LoginActivity loginActivity);

    LoginFirebasePresenter presenter();
}
