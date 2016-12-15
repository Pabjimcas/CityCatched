package com.pabji.citycatched.domain.components;

import com.pabji.citycatched.domain.modules.BuildingMapModule;
import com.pabji.citycatched.domain.modules.BuildingModule;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.activities.BuildingActivity;
import com.pabji.citycatched.presentation.activities.BuildingMapActivity;
import com.pabji.citycatched.presentation.mvp.presenters.BuildingMapPresenter;
import com.pabji.citycatched.presentation.mvp.presenters.BuildingPresenter;

import dagger.Component;

@PerActivity
@Component(dependencies = CityCatchedApplicationComponent.class,modules = BuildingMapModule.class)
public interface BuildingMapComponent {
    void inject(BuildingMapActivity buildingMapActivity);

    BuildingMapPresenter presenter();
}
