package com.pabji.citycatched.domain.components;

import com.pabji.citycatched.domain.modules.RevisionListModule;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.fragments.BuildingListVisitedFragment;
import com.pabji.citycatched.presentation.mvp.presenters.BuildingListVisitedPresenter;

import dagger.Component;

@PerActivity
@Component(dependencies = CityCatchedApplicationComponent.class,modules = RevisionListModule.class)
public interface RevisionListComponent {
    void inject(BuildingListVisitedFragment buildingListVisitedFragment);

    BuildingListVisitedPresenter presenter();
}
