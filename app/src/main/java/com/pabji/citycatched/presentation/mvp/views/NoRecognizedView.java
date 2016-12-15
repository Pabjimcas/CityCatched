package com.pabji.citycatched.presentation.mvp.views;

import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.views.base.BaseView;
import com.pabji.citycatched.presentation.mvp.models.Descriptor;

import java.util.List;

/**
 * Created by Pablo Jiménez Casado on 03/08/2016.
 */
public interface NoRecognizedView extends BaseView {

    void showProgressDialog(int app_name);

    void hideProgressDialog();

    void showQuestion(String s);

    void showNoRecognizedView(List<Building> buildingList);
}
