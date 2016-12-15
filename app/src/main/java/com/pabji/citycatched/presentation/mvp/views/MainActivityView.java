package com.pabji.citycatched.presentation.mvp.views;

import com.pabji.citycatched.presentation.fragments.base.BaseMVPFragment;
import com.pabji.citycatched.presentation.mvp.views.base.BaseView;

/**
 * Created by Pablo Jiménez Casado on 03/08/2016.
 */
public interface MainActivityView extends BaseView {

    void photoButton();

    void showPhotoButton();

    void hidePhotoButton();

    void showProgressDialog(int messageResource);

    void hideProgressDialog();

    void openCameraVideo();

    void canReadNearBuildingList();

}
