package com.pabji.citycatched.domain.modules;

import android.content.Context;

import com.pabji.citycatched.domain.features.login.LoginGoogleInteractor;
import com.pabji.citycatched.domain.features.login.LoginGoogleInteractorImpl;
import com.pabji.citycatched.domain.scopes.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginFirebaseModule extends BaseActivityModule {
    public LoginFirebaseModule(Context context) {
        super(context);
    }

    @Provides
    @PerActivity
    LoginGoogleInteractor providerLoginWithGoogleInteractor(){
        return new LoginGoogleInteractorImpl();
    }

}
