package com.pabji.citycatched.domain.components;

import com.pabji.citycatched.domain.modules.NewBuildingModule;
import com.pabji.citycatched.domain.scopes.PerActivity;
import com.pabji.citycatched.presentation.activities.NewBuildingActivity;
import com.pabji.citycatched.presentation.fragments.newBuilding.NewBuildingStep1Fragment;
import com.pabji.citycatched.presentation.fragments.newBuilding.NewBuildingStep2Fragment;
import com.pabji.citycatched.presentation.fragments.newBuilding.NewBuildingStep3Fragment;
import com.pabji.citycatched.presentation.mvp.presenters.newBuilding.NewBuildingPresenter;
import com.pabji.citycatched.presentation.mvp.presenters.newBuilding.NewBuildingStep2Presenter;
import com.pabji.citycatched.presentation.mvp.presenters.newBuilding.NewBuildingStep3Presenter;

import dagger.Component;

@PerActivity
@Component(dependencies = CityCatchedApplicationComponent.class,modules = NewBuildingModule.class)
public interface NewBuildingComponent {
    void inject(NewBuildingActivity newBuildingActivity);
    void inject(NewBuildingStep1Fragment newBuildingStep1Fragment);
    void inject(NewBuildingStep2Fragment newBuildingStep2Fragment);
    void inject(NewBuildingStep3Fragment newBuildingStep3Fragment);

    NewBuildingPresenter newBuildingPresenter();
    NewBuildingStep2Presenter newBuildingStep2Presenter();
    NewBuildingStep3Presenter newBuildingStep3Presenter();


}


