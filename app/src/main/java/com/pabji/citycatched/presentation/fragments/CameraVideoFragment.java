package com.pabji.citycatched.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pabji.citycatched.R;
import com.pabji.citycatched.domain.components.MainActivityComponent;
import com.pabji.citycatched.presentation.activities.MainActivity;
import com.pabji.citycatched.presentation.mvp.presenters.MainActivityPresenter;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Pablo Jiménez Casado on 04/08/2016.
 */

public class CameraVideoFragment extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2 {


    @BindView(R.id.cameraView) CameraBridgeViewBase cameraView;

    private Unbinder unbind;
    private MainActivityPresenter presenter;
    private MainActivityComponent component;
    private Mat mRgba;
    private Mat mGray;
    private Mat mRgbaF;
    private Mat mRgbaT;

    public static CameraVideoFragment newInstance() {
        CameraVideoFragment frag = new CameraVideoFragment();
        return frag;
    }

    @Inject
    public CameraVideoFragment() {
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_camera_video,container,false);
        unbind = ButterKnife.bind(this,view);

        this.presenter = ((MainActivity) getActivity()).getPresenter();
        this.component = ((MainActivity)getActivity()).getComponent();
        this.component.inject(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cameraView.enableView();
        cameraView.setFocusable(true);
        cameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cameraView != null)
            cameraView.disableView();
        unbind.unbind();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(height, width, CvType.CV_8UC4);
        mRgbaT = new Mat(width, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        // Rotate mRgba 90 degrees
        /*Core.transpose(mRgba, mRgbaT);
        Imgproc.resize(mRgbaT, mRgbaF, mRgbaF.size(), 0,0, 0);
        Core.flip(mRgbaF, mRgba, 1 );*/

        return mRgba; // This function must return
    }
}


