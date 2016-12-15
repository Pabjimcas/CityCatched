package com.pabji.citycatched.presentation.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.pabji.citycatched.R;
import com.pabji.citycatched.domain.components.DaggerNoRecognizedComponent;
import com.pabji.citycatched.domain.scopes.HasComponent;
import com.pabji.citycatched.domain.components.NoRecognizedComponent;
import com.pabji.citycatched.domain.modules.NoRecognizedModule;
import com.pabji.citycatched.presentation.activities.base.BaseMVPActivity;
import com.pabji.citycatched.presentation.fragments.NotRecognizeListFragment;
import com.pabji.citycatched.presentation.fragments.NotRecognizeQuestionsFragment;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.presenters.NoRecognizedPresenter;
import com.pabji.citycatched.presentation.mvp.views.NoRecognizedView;

import java.util.List;

/**
 * Created by Pablo Jiménez Casado on 06/08/2016.
 */

public class NoRecognizedActivity extends BaseMVPActivity<NoRecognizedPresenter, NoRecognizedView> implements HasComponent<NoRecognizedComponent>, NoRecognizedView {
    private NoRecognizedComponent component;
    private ProgressDialog progressDialog;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, NoRecognizedActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.initializeInjector();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_recognized);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        presenter.init(this, getIntent().getExtras());
    }

    @NonNull
    @Override
    public NoRecognizedPresenter createPresenter() {
        return component.noRecognizedPresenter();
    }

    @Override
    public NoRecognizedComponent getComponent() {
        return component;
    }

    private void initializeInjector() {
        this.component = DaggerNoRecognizedComponent.builder()
                .cityCatchedApplicationComponent(getInjector())
                .noRecognizedModule(new NoRecognizedModule(this))
                .build();
        this.component.inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showProgressDialog(int resource) {
        progressDialog.setTitle(getString(resource));
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        progressDialog.dismiss();
    }


    @Override
    public void showQuestion(String s) {
        addFragment(R.id.content, NotRecognizeQuestionsFragment.newInstance(s));
    }

    @Override
    public void showNoRecognizedView(List<Building> buildingList) {
        addFragment(R.id.content, NotRecognizeListFragment.newInstance());
    }
}
