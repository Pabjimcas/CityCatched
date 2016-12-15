package com.pabji.citycatched.presentation.mvp.views;

import com.pabji.citycatched.presentation.mvp.models.Comment;
import com.pabji.citycatched.presentation.mvp.views.base.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablo Jiménez Casado on 03/08/2016.
 */
public interface BuildingView extends BaseView {
    void showShortDescription(String shortDescription);
    void showBuildingPictures(List<String> buildingPictures);
}
