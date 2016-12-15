package com.pabji.citycatched.presentation.mvp.presenters;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pabji.citycatched.R;
import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.domain.features.AddBuildingRangeInteractor;
import com.pabji.citycatched.domain.features.AddBuildingUserVisitedInteractor;
import com.pabji.citycatched.domain.features.GetNearBuildingInteractor;
import com.pabji.citycatched.domain.features.GetNearBuildingListDescriptorsInteractor;
import com.pabji.citycatched.domain.features.GetNearBuildingListInteractor;
import com.pabji.citycatched.domain.features.UpdateMyCurrentLocationInteractor;
import com.pabji.citycatched.domain.utils.Recognizer;
import com.pabji.citycatched.presentation.activities.base.BaseMVPActivity;
import com.pabji.citycatched.presentation.mvp.models.Descriptor;
import com.pabji.citycatched.presentation.mvp.models.RecognizedObject;
import com.pabji.citycatched.presentation.mvp.presenters.base.BasePresenter;
import com.pabji.citycatched.data.repositories.ApiClientRepository;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.models.MyLocation;
import com.pabji.citycatched.presentation.navigation.Router;
import com.pabji.citycatched.presentation.mvp.views.MainActivityView;
import com.pabji.citycatched.domain.utils.FileManager;
import com.pabji.citycatched.domain.utils.PermissionUtils;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;
import timber.log.Timber;

public class MainActivityPresenter extends BasePresenter<MainActivityView>{

    @Inject Router router;

    @Inject
    GetNearBuildingListDescriptorsInteractor getNearBuildingListDescriptorsInteractor;

    @Inject
    GetNearBuildingListInteractor getNearBuildingListInteractor;

    @Inject
    GetNearBuildingInteractor getNearBuildingInteractor;

    @Inject
    AddBuildingRangeInteractor addBuildingRangeInteractor;

    @Inject
    AddBuildingUserVisitedInteractor addBuildingUserVisitedInteractor;

    @Inject
    UpdateMyCurrentLocationInteractor updateMyCurrentLocationInteractor;

    @Inject ThreadExecutor threadExecutor;
    @Inject PostExecutionThread postExecutionThread;
    @Inject ApiClientRepository apiClientRepository;
    @Inject PermissionUtils permissionUtils;
    @Inject @Named("ActivityContext") Context context;
    public static final String TAG = MainActivityPresenter.class.getName();

    public static Recognizer recognizer;
    static {
        if (!OpenCVLoader.initDebug()) {

        }else{
            System.loadLibrary("opencv_java3");
            initRecognizer();
        }
    }

    private List<Descriptor> descriptorList = new ArrayList<>();
    private MyLocation lastLocation;
    private FirebaseUser mAuth;
    private List<Building> nearBuildingListVisited = new ArrayList<>();

    private static void initRecognizer() {
        recognizer = Recognizer.getInstance();
    }

    private File tempFile;
    private MyLocation myLocation;
    private Uri fileUri;
    private boolean permissionGranted;
    private List<Building> nearBuildingList = new ArrayList<>();

    @Inject public MainActivityPresenter() {}

    public void init(Uri uriFile) {
        requestPermission();
        if(uriFile != null){
            fileUri = uriFile;
        }
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void checkOpenCV() {
        if(recognizer != null){
            Toast.makeText(context,"ACTIVADO",Toast.LENGTH_SHORT).show();
        }else{
            getView().hidePhotoButton();
        }
    }

    public void takePhoto() {
        if(permissionGranted){
            if(myLocation != null) {
                openCamera();
            }else{
                Toast.makeText(context,"No location",Toast.LENGTH_SHORT).show();
                takePhoto();
            }
        }else{
            requestPermission();
        }
    }

    private void loadNearBuildings() {
        getNearBuildingListInteractor.execute(mAuth.getUid(),new Subscriber<List<Building>>() {
            @Override
            public void onCompleted() {
                getBuildingsDescriptors();
                getView().canReadNearBuildingList();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Building> buildingList) {
                nearBuildingList = buildingList;
            }
        });
    }

    private void getBuildingsDescriptors() {
        descriptorList.clear();
        getNearBuildingListDescriptorsInteractor.execute(new Subscriber<Descriptor>() {
            @Override
            public void onCompleted() {
                try {
                    recognizer.addDescriptors(descriptorList);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                getView().showPhotoButton();
            }

            @Override
            public void onError(Throwable e) {
                Log.d("Error",e.toString());
            }

            @Override
            public void onNext(Descriptor descriptor) {
                descriptorList.add(descriptor);
            }
        });
    }

    private void openCamera() {
        tempFile = FileManager.createTempFile();
        fileUri = FileManager.getUriFromFile(context,tempFile);

        if (fileUri != null) {
            router.openCameraActivity((BaseMVPActivity) context, fileUri);
        } else {
            Timber.d("file URI null");
        }
    }

    public void recognizeImage(String filePath){
        getView().showProgressDialog(R.string.app_name);
        recognizer.recognize(filePath,threadExecutor,postExecutionThread,
                new Subscriber<RecognizedObject>() {
            @Override
            public void onCompleted() {
                getView().hideProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,e.toString());
                getView().hideProgressDialog();
            }

            @Override
            public void onNext(RecognizedObject recognizedObject) {
                if(recognizedObject.descriptor != null){
                    getBuilding(recognizedObject.getDescriptor().getBuildingId());
                }else{
                    noBuildingRecognized(recognizedObject);
                }
            }
        });
    }

    private void noBuildingRecognized(RecognizedObject recognizedObject) {
        router.openNoRecognized(myLocation,recognizedObject.contentDescriptorDetected);
    }

    public void addBuildingRange(){
        addBuildingRangeInteractor.execute(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        });
    }

    public void addBuildingUserVisited(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        addBuildingUserVisitedInteractor.execute(mAuth.getCurrentUser().getUid(),new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        });
    }

    public void getBuilding(String buildingId){
        getNearBuildingInteractor.execute(buildingId, new Subscriber<Building>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Building building) {
                addBuildingRange();
                addBuildingUserVisited();
                router.openDetailBuilding(building);
            }
        });

    }

    public void requestPermission() {

        Dexter.checkPermissions(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                permissionGranted = report.areAllPermissionsGranted();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }, Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    public void getLocation(GoogleApiClient googleApiClient){
        try {

            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            updateMyCurrentLocationInteractor.execute(location,new Subscriber<MyLocation>() {
                @Override
                public void onCompleted() {
                    if(lastLocation != null && lastLocation.getDistanceTo(myLocation) <= 600){

                    }else{
                        loadNearBuildings();
                    }

                    lastLocation = myLocation;
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(MyLocation location) {
                    myLocation = location;
                    Toast.makeText(context,myLocation.latitude +","+ myLocation.longitude,Toast.LENGTH_SHORT).show();
                }
            });

        }catch (SecurityException e){
            Toast.makeText(context,"No location",Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getUri() {
        return fileUri;
    }

    public String getFilePath(){
        if(tempFile != null){
            return tempFile.getPath();
        }else{
            return null;
        }
    }

    public void openVideo() {
        getView().openCameraVideo();
    }

    public List<Building> getNearBuildingList(){
        return nearBuildingList;
    }
}
