package com.pabji.citycatched.presentation.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.pabji.citycatched.R;
import com.pabji.citycatched.domain.components.CameraVideoComponent;
import com.pabji.citycatched.domain.components.DaggerCameraVideoComponent;
import com.pabji.citycatched.domain.components.DaggerMainActivityComponent;
import com.pabji.citycatched.domain.components.MainActivityComponent;
import com.pabji.citycatched.domain.constants.RequestConstants;
import com.pabji.citycatched.domain.modules.CameraVideoModule;
import com.pabji.citycatched.domain.modules.MainActivityModule;
import com.pabji.citycatched.domain.scopes.HasComponent;
import com.pabji.citycatched.presentation.activities.base.BaseMVPActivity;
import com.pabji.citycatched.presentation.fragments.CameraVideoFragment;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.presenters.CameraVideoPresenter;
import com.pabji.citycatched.presentation.mvp.presenters.MainActivityPresenter;
import com.pabji.citycatched.presentation.mvp.views.CameraVideoView;
import com.pabji.citycatched.presentation.mvp.views.MainActivityView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CameraVideoActivity extends BaseMVPActivity<CameraVideoPresenter,CameraVideoView> implements CameraVideoView, HasComponent<CameraVideoComponent>, CameraBridgeViewBase.CvCameraViewListener2 {

    private CameraVideoComponent component;

    private Unbinder unbind;

    /*@BindView(R.id.ll_container)
    LinearLayout llContainer;*/

    @BindView(R.id.cameraView) CameraBridgeViewBase cameraView;
    private Mat mRgba;
    private Handler handler;
    private boolean miBoolean;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, CameraVideoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.initializeInjector();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_video);
        unbind = ButterKnife.bind(this);

        handler = new Handler();

        cameraView.enableView();
        cameraView.setFocusable(true);
        cameraView.setCvCameraViewListener(this);

        presenter.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraView != null)
            cameraView.disableView();
        unbind.unbind();
    }

    @NonNull
    @Override
    public CameraVideoPresenter createPresenter() {
        return component.createPresenter();
    }

    @Override
    public CameraVideoComponent getComponent() {
        return component;
    }

    private void initializeInjector() {
        this.component = DaggerCameraVideoComponent.builder()
                .cityCatchedApplicationComponent(getInjector())
                .cameraVideoModule(new CameraVideoModule(this))
                .build();
        component.inject(this);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        Mat mGray = inputFrame.gray();
        presenter.recognizer(mGray);
        return mRgba;
    }

    @Override
    public void showBuildingPreview(Building building) {
        handler.post(new EditViewRunnable(this,building));
    }

    @Override
    public void hideBuildingPreview() {
        handler.post(new EditViewRunnable(this,null));
    }

    private class EditViewRunnable implements Runnable {

        private final Context context;
        Building building;

        public EditViewRunnable(Context context,Building building) {
            this.building = building;
            this.context = context;
        }

        @Override
        public void run() {

            LinearLayout container = (LinearLayout) findViewById(R.id.ll_container);
            TextView tv = (TextView) findViewById(R.id.tv_buildingName);
            ImageView iv = (ImageView) findViewById(R.id.iv_buildingImage);
            if(building != null){
                tv.setText(building.getName());
                container.setVisibility(View.VISIBLE);
            }else{
                container.setVisibility(View.GONE);
            }
        }
    }
}
