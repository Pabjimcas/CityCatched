package com.pabji.citycatched.presentation.mvp.views;

import com.pabji.citycatched.presentation.mvp.models.Comment;
import com.pabji.citycatched.presentation.mvp.views.base.BaseView;

import java.util.List;

/**
 * Created by Pablo Jiménez Casado on 03/08/2016.
 */
public interface BuildingGalleryView extends BaseView {
    void showBuildingPictures(List<String> buildingPictures);
}
