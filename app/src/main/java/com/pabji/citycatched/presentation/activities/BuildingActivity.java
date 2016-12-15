package com.pabji.citycatched.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pabji.citycatched.R;
import com.pabji.citycatched.domain.components.DaggerBuildingComponent;
import com.pabji.citycatched.domain.scopes.HasComponent;
import com.pabji.citycatched.presentation.activities.base.BaseActivity;
import com.pabji.citycatched.domain.components.BuildingComponent;
import com.pabji.citycatched.domain.modules.BuildingModule;
import com.pabji.citycatched.presentation.activities.base.BaseMVPActivity;
import com.pabji.citycatched.presentation.adapters.BuildingPagerAdapter;
import com.pabji.citycatched.presentation.adapters.BuildingPicturesAdapter;
import com.pabji.citycatched.presentation.adapters.ScreenSlidePagerAdapter;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.models.Comment;
import com.pabji.citycatched.presentation.mvp.presenters.BuildingPresenter;
import com.pabji.citycatched.presentation.mvp.views.BuildingView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BuildingActivity extends BaseMVPActivity<BuildingPresenter,BuildingView> implements BuildingView,HasComponent<BuildingComponent>,BuildingPagerAdapter.OnItemClickListener {

    private BuildingComponent component;

    @BindView(R.id.vp_pictures)
    ViewPager vpPictures;

    @BindView(R.id.tv_short_description)
    TextView tvShortDescription;

    /*@BindView(R.id.rv_pictures)
    RecyclerView rvPictures;*/

    @Inject
    BuildingPicturesAdapter buildingPicturesAdapter;

    private Unbinder unbind;
    private BuildingPagerAdapter mPagerAdapter;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, BuildingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.initializeInjector();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        unbind = ButterKnife.bind(this);
        Building building = getIntent().getParcelableExtra("building");
        /*rvPictures.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPictures.setAdapter(buildingPicturesAdapter);*/

        presenter.init(this,building);
    }

    @NonNull
    @Override
    public BuildingPresenter createPresenter() {
        return component.presenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind.unbind();
    }

    @Override
    public BuildingComponent getComponent() {
        return component;
    }

    private void initializeInjector() {
        this.component = DaggerBuildingComponent.builder()
                .cityCatchedApplicationComponent(getInjector())
                .buildingModule(new BuildingModule(this))
                .build();
        this.component.inject(this);
    }

    @Override
    public void showShortDescription(String shortDescription) {
        tvShortDescription.setText(shortDescription);
    }

    @OnClick(R.id.fb_map)
    public void openMap(){
        presenter.openMap();
    }

    @OnClick(R.id.fb_comments)
    public void openComments(){
        presenter.openComments();
    }

    @OnClick(R.id.tv_show_more)
    public void openWebview(){
        presenter.openWebview();
    }


    @Override
    public void showBuildingPictures(List<String> buildingPictures) {
        mPagerAdapter = new BuildingPagerAdapter(getSupportFragmentManager(),buildingPictures,this);
        vpPictures.setAdapter(mPagerAdapter);
    }

    @Override
    public void onItemClickListener(int position) {
        presenter.openGallery(position);
    }
}
