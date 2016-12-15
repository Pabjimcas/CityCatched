package com.pabji.citycatched.presentation.mvp.views;

import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.views.base.BaseView;

/**
 * Created by Pablo Jiménez Casado on 03/08/2016.
 */
public interface CameraVideoView extends BaseView {

    void showBuildingPreview(Building building);

    void hideBuildingPreview();
}
