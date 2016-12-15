package com.pabji.citycatched.presentation.mvp.views.newBuilding;

import android.content.Intent;
import android.net.Uri;

import com.pabji.citycatched.presentation.mvp.views.base.BaseView;

import java.util.List;

/**
 * Created by Pablo Jiménez Casado on 03/08/2016.
 */
public interface NewBuildingView extends BaseView {

    void openGallery(Intent chooser);

    void showMainImage(Uri uriImage);

    void showProgress();

    void hideProgress();

    void setProgress(int i);

    void loadFragments();

    void showFragment(int currentFragment);

    void enableBackButton();

    void disableBackButton();

    void enableContinueButton();

    void disableContinueButton();

    void showBuildingName(String name);
}


