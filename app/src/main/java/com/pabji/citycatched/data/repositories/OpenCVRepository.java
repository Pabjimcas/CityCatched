package com.pabji.citycatched.data.repositories;

import android.content.Context;

import com.pabji.citycatched.data.constants.ErrorConstants;
import com.pabji.citycatched.data.exceptions.CustomOpenCvException;
import com.pabji.citycatched.domain.utils.Recognizer;
import com.pabji.citycatched.presentation.mvp.models.Descriptor;
import com.pabji.citycatched.presentation.mvp.models.RecognizedObject;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Pablo Jiménez Casado on 04/08/2016.
 */

@Singleton
public class OpenCVRepository{

    Context context;

    @Inject
    public OpenCVRepository(Context context){
        this.context = context;
    }

    public Observable<Boolean> isOpenCvLoaded(){
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                /*final BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(context) {
                    @Override
                    public void onManagerConnected(int status) {
                        switch (status) {
                            case LoaderCallbackInterface.SUCCESS: {
                                subscriber.onNext(true);
                                subscriber.onCompleted();
                            }
                            break;
                            case LoaderCallbackInterface.INIT_FAILED:{
                                subscriber.onError(new CustomOpenCvException(ErrorConstants.OPENCV_INIT_FAILED));
                            }
                            break;
                            case LoaderCallbackInterface.INSTALL_CANCELED:{
                                subscriber.onError(new CustomOpenCvException(ErrorConstants.OPENCV_INSTALL_CANCELED));
                            }
                            break;
                            case LoaderCallbackInterface.INCOMPATIBLE_MANAGER_VERSION:{
                                subscriber.onError(new CustomOpenCvException(ErrorConstants.OPENCV_INCOMPATIBLE_MANAGER_VERSION));
                            }
                            break;
                            case LoaderCallbackInterface.MARKET_ERROR:{
                                subscriber.onError(new CustomOpenCvException(ErrorConstants.OPENCV_MARKET_ERROR));
                            }
                            break;
                            default: {
                                super.onManagerConnected(status);
                            }
                            break;
                        }
                    }
                };
                OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, context, mLoaderCallback);
            */


            }
        });
    }

}
