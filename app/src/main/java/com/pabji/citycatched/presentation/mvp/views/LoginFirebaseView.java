package com.pabji.citycatched.presentation.mvp.views;


import com.pabji.citycatched.presentation.mvp.views.base.BaseView;

public interface LoginFirebaseView extends BaseView {
    void signInGoogle();
    void signInFacebook();
}
