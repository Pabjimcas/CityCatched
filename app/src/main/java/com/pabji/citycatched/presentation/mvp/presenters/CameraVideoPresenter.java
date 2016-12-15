package com.pabji.citycatched.presentation.mvp.presenters;

import android.content.Context;
import android.util.Log;

import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.domain.features.GetNearBuildingInteractor;
import com.pabji.citycatched.domain.utils.Recognizer;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.presenters.base.BasePresenter;
import com.pabji.citycatched.presentation.mvp.views.CameraVideoView;
import com.pabji.citycatched.presentation.navigation.Router;

import org.opencv.core.Mat;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;

public class CameraVideoPresenter extends BasePresenter<CameraVideoView>{

    @Inject Router router;

    @Inject ThreadExecutor threadExecutor;
    @Inject PostExecutionThread postExecutionThread;

    @Inject
    GetNearBuildingInteractor getNearBuildingInteractor;

    @Inject @Named("ActivityContext") Context context;
    public static final String TAG = CameraVideoPresenter.class.getName();

    public static Recognizer recognizer;

    public void init(){
        recognizer = Recognizer.getInstance();
    }

    @Inject public CameraVideoPresenter() {}

    public void recognizer(Mat mat){

        recognizer.recognize(mat,threadExecutor,postExecutionThread,
                new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,e.toString());
                    }

                    @Override
                    public void onNext(String buildingId) {
                       if(buildingId != null){
                           loadBuildingPreview(buildingId);
                       }else{
                           getView().hideBuildingPreview();
                       }
                    }
                });
    }

    private void loadBuildingPreview(String buildingId) {
        getNearBuildingInteractor.execute(buildingId, new Subscriber<Building>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Building building) {
                getView().showBuildingPreview(building);
            }
        });
    }

}
