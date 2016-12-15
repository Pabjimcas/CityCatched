package com.pabji.citycatched.presentation.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pabji.citycatched.R;
import com.pabji.citycatched.domain.components.DaggerNewBuildingComponent;
import com.pabji.citycatched.domain.constants.RequestConstants;
import com.pabji.citycatched.domain.scopes.HasComponent;
import com.pabji.citycatched.domain.components.NewBuildingComponent;
import com.pabji.citycatched.domain.modules.NewBuildingModule;
import com.pabji.citycatched.presentation.activities.base.BaseMVPActivity;
import com.pabji.citycatched.presentation.fragments.newBuilding.NewBuildingStep1Fragment;
import com.pabji.citycatched.presentation.fragments.newBuilding.NewBuildingStep2Fragment;
import com.pabji.citycatched.presentation.fragments.newBuilding.NewBuildingStep3Fragment;
import com.pabji.citycatched.presentation.fragments.newBuilding.NewBuildingStep4Fragment;
import com.pabji.citycatched.presentation.fragments.newBuilding.NewBuildingStep5Fragment;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.presenters.newBuilding.NewBuildingPresenter;
import com.pabji.citycatched.presentation.mvp.views.newBuilding.NewBuildingView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Pablo Jiménez Casado on 16/10/2016.
 */

public class NewBuildingActivity extends BaseMVPActivity<NewBuildingPresenter,NewBuildingView> implements HasComponent<NewBuildingComponent>, NewBuildingView{
    private NewBuildingComponent component;
    private Building building;
    private Uri uriImage;

    @BindView(R.id.iv_mainImage)
    ImageView mainImage;

    @BindView(R.id.btn_back)
    Button backButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btn_continue) Button continueButton;
    private Unbinder unbind;

    private ArrayList<Fragment> fragments = new ArrayList<>();

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, NewBuildingActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.initializeInjector();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_building);
        unbind = ButterKnife.bind(this);

        uriImage = getIntent().getExtras().getParcelable("uriImage");
        building = getIntent().getExtras().getParcelable("building");

        setDefaultToolbar();
        loadFragments();

        presenter.init(this,building,uriImage,fragments.size());
    }

    private void setDefaultToolbar() {
        toolbar.setTitle("Hola");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }


    @NonNull
    @Override
    public NewBuildingPresenter createPresenter() {
        return component.newBuildingPresenter();
    }


    @Override
    public NewBuildingComponent getComponent() {
        return component;
    }

    private void initializeInjector() {
        this.component = DaggerNewBuildingComponent.builder()
                .cityCatchedApplicationComponent(getInjector())
                .newBuildingModule(new NewBuildingModule(this))
                .build();
        component.inject(this);
    }

    @Override
    public void openGallery(Intent chooser) {

    }

    @Override
    public void showMainImage(Uri uriImage) {
        Glide.with(this).load(uriImage).into(mainImage);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void loadFragments() {
        fragments.add(NewBuildingStep1Fragment.newInstance());
        fragments.add(NewBuildingStep2Fragment.newInstance());
        fragments.add(NewBuildingStep3Fragment.newInstance());
        fragments.add(NewBuildingStep4Fragment.newInstance());
        fragments.add(NewBuildingStep5Fragment.newInstance());
    }

    @Override
    public void showFragment(int currentFragment) {
        addFragment(R.id.content, fragments.get(currentFragment));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_building,menu);
        return true;
    }

    @Override
    public void enableBackButton() {
        backButton.setEnabled(true);
    }

    @Override
    public void disableBackButton() {
        backButton.setEnabled(false);
    }

    @Override
    public void enableContinueButton() {
        continueButton.setEnabled(true);
    }

    @Override
    public void disableContinueButton() {
        continueButton.setEnabled(false);
    }

    @Override
    public void showBuildingName(String name) {
        //Not show title
        this.getSupportActionBar().setTitle(name);
    }

    @OnClick(R.id.btn_continue)
    public void nextFragment() {
        presenter.nextFragment();
    }

    @OnClick(R.id.btn_back)
    public void previousFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            presenter.previousFragment();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_photo:
                presenter.showPictureOptions();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RequestConstants.SELECT_IMAGE_GALLERY){
                presenter.addImageFromGallery(data.getData());
            }else if(requestCode == RequestConstants.CAPTURE_IMAGE){
                presenter.addImageFromCamera();
            }
        }
    }
}
