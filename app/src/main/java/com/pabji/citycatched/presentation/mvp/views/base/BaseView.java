package com.pabji.citycatched.presentation.mvp.views.base;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by pabji on 16/06/2016.
 */
public interface BaseView extends MvpView {
    void showError(int error);
}
