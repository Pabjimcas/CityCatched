package com.pabji.citycatched.presentation.mvp.views.newBuilding;

import android.net.Uri;

import com.pabji.citycatched.presentation.mvp.views.base.BaseView;

import java.util.List;

/**
 * Created by Pablo Jiménez Casado on 26/11/2016.
 */
public interface NewBuildingStep2View extends BaseView {
    void showPhotoList(List<Uri> imagesPath);
}
