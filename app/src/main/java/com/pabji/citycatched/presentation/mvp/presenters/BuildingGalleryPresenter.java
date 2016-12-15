package com.pabji.citycatched.presentation.mvp.presenters;

import android.content.Context;

import com.pabji.citycatched.domain.features.GetBuildingCommentsInteractor;
import com.pabji.citycatched.domain.features.GetBuildingPicturesInteractor;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.models.Comment;
import com.pabji.citycatched.presentation.mvp.presenters.base.BasePresenter;
import com.pabji.citycatched.presentation.mvp.views.BuildingGalleryView;
import com.pabji.citycatched.presentation.mvp.views.BuildingView;
import com.pabji.citycatched.presentation.navigation.Router;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class BuildingGalleryPresenter extends BasePresenter<BuildingGalleryView> {

    @Inject
    Router router;

    @Inject
    GetBuildingPicturesInteractor picturesInteractor;

    Context context;

    private List<String> buildingPictures;
    private String buildingId;
    private int currentPosition;

    @Inject
    public BuildingGalleryPresenter() {
    }

    public void init(Context context, String buildingId,int position) {
        this.context = context;
        this.buildingId = buildingId;
        this.currentPosition = position;
        getBuildingPictures();
    }

    private void getBuildingPictures() {
        if(buildingId != null) {
            picturesInteractor.execute(new Subscriber<List<String>>() {
                @Override
                public void onCompleted() {
                    if(!buildingPictures.isEmpty()) {
                        getView().showBuildingPictures(buildingPictures);
                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<String> pictures) {
                    buildingPictures = pictures;
                }
            });
        }
    }
}
