package com.pabji.citycatched.presentation.mvp.presenters;

import android.content.Context;

import com.pabji.citycatched.domain.features.GetBuildingPicturesInteractor;
import com.pabji.citycatched.presentation.mvp.presenters.base.BasePresenter;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.navigation.Router;
import com.pabji.citycatched.presentation.mvp.views.BuildingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class BuildingPresenter extends BasePresenter<BuildingView> {

    @Inject
    Router router;

    @Inject
    GetBuildingPicturesInteractor picturesInteractor;

    Context context;

    private Building building;
    private List<String> buildingPictures = new ArrayList<>();

    @Inject
    public BuildingPresenter() {
    }

    public void init(Context context, Building building) {
        this.context = context;
        this.building = building;
        getView().showShortDescription(building.shortDescription);
        getBuildingPictures();
    }


    private void getBuildingPictures() {
        if(building != null) {
            buildingPictures.clear();
            buildingPictures.add(building.mainImage);
            picturesInteractor.execute(new Subscriber<List<String>>() {
                @Override
                public void onCompleted() {
                    getView().showBuildingPictures(buildingPictures);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<String> pictures) {
                    buildingPictures.addAll(pictures);
                }
            });
        }
    }

    public void openGallery(int position) {
        router.openBuildingPicturesGallery((ArrayList<String>) buildingPictures,position);
    }

    public void openWebview(){
        router.openWebview(building.getUrlDescription());
    }

    public void openMap(){
        router.openMap(building.mainImage);
    }

    public void openComments() {
        router.openComments(building.mainImage);
    }
}
