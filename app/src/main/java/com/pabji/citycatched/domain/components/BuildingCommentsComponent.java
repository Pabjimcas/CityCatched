package com.pabji.citycatched.domain.components;

import com.pabji.citycatched.domain.modules.BuildingCommentsModule;
import com.pabji.citycatched.domain.modules.BuildingModule;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.activities.BuildingActivity;
import com.pabji.citycatched.presentation.activities.BuildingCommentsActivity;
import com.pabji.citycatched.presentation.mvp.presenters.BuildingCommentsPresenter;
import com.pabji.citycatched.presentation.mvp.presenters.BuildingPresenter;

import dagger.Component;

@PerActivity
@Component(dependencies = CityCatchedApplicationComponent.class,modules = BuildingCommentsModule.class)
public interface BuildingCommentsComponent {
    void inject(BuildingCommentsActivity buildingCommentsActivity);

    BuildingCommentsPresenter presenter();
}
