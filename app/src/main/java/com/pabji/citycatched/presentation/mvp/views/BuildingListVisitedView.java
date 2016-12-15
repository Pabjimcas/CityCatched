package com.pabji.citycatched.presentation.mvp.views;

import com.pabji.citycatched.presentation.mvp.views.base.BaseView;
import com.pabji.citycatched.presentation.mvp.models.Building;

import java.util.List;
import java.util.Map;

/**
 * Created by Pablo Jiménez Casado on 03/08/2016.
 */
public interface BuildingListVisitedView extends BaseView {

    void showBuildingVisited(Map<String, List<Building>> buildingMap);

    void showProgressDialog(int messageResource);

    void hideProgressDialog();
}
