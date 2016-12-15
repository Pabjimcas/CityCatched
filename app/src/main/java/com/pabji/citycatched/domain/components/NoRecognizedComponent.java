package com.pabji.citycatched.domain.components;

import com.pabji.citycatched.domain.modules.NoRecognizedModule;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.activities.NoRecognizedActivity;
import com.pabji.citycatched.presentation.fragments.NotRecognizeListFragment;
import com.pabji.citycatched.presentation.mvp.presenters.NoRecognizedPresenter;

import dagger.Component;

@PerActivity
@Component(dependencies = CityCatchedApplicationComponent.class,modules = NoRecognizedModule.class)
public interface NoRecognizedComponent {
    void inject(NoRecognizedActivity notRecognizeFragment);
    NoRecognizedPresenter noRecognizedPresenter();

    void inject(NotRecognizeListFragment notRecognizeListFragment);
}


