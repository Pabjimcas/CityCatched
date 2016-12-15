package com.pabji.citycatched.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.facebook.login.widget.LoginButton;
import com.pabji.citycatched.R;
import com.pabji.citycatched.domain.components.DaggerLoginFirebaseComponent;
import com.pabji.citycatched.domain.scopes.HasComponent;
import com.pabji.citycatched.presentation.activities.base.BaseMVPActivity;
import com.pabji.citycatched.domain.components.LoginFirebaseComponent;
import com.pabji.citycatched.domain.modules.LoginFirebaseModule;
import com.pabji.citycatched.presentation.mvp.presenters.LoginFirebasePresenter;
import com.pabji.citycatched.presentation.mvp.views.LoginFirebaseView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends BaseMVPActivity<LoginFirebasePresenter,LoginFirebaseView> implements LoginFirebaseView, HasComponent<LoginFirebaseComponent> {

    private LoginFirebaseComponent component;

    private Unbinder unbind;

    @BindView(R.id.sign_in_facebook)
    LoginButton facebookButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.initializeInjector();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbind = ButterKnife.bind(this);
        presenter.init(this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbind.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @NonNull
    @Override
    public LoginFirebasePresenter createPresenter() {
        return component.presenter();
    }

    private void initializeInjector() {
       this.component = DaggerLoginFirebaseComponent.builder()
                .cityCatchedApplicationComponent(getInjector())
               .loginFirebaseModule(new LoginFirebaseModule(this))
                .build();
        component.inject(this);
    }

    public LoginFirebaseComponent getComponent(){
        return component;
    }

    public static Intent getCallingIntent(Context context){
        return new Intent(context,LoginActivity.class);
    }

    @OnClick(R.id.sign_in_google)
    @Override
    public void signInGoogle() {
        presenter.signInGoogle();
    }

    @OnClick(R.id.sign_in_facebook)
    @Override
    public void signInFacebook() {
        presenter.signInFacebook(facebookButton);
    }


}
