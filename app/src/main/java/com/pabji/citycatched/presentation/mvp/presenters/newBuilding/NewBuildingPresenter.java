package com.pabji.citycatched.presentation.mvp.presenters.newBuilding;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.pabji.citycatched.R;
import com.pabji.citycatched.domain.utils.FileManager;
import com.pabji.citycatched.domain.utils.PermissionUtils;
import com.pabji.citycatched.presentation.activities.base.BaseMVPActivity;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.models.MyLocation;
import com.pabji.citycatched.presentation.mvp.presenters.base.BasePresenter;
import com.pabji.citycatched.presentation.mvp.views.newBuilding.NewBuildingView;
import com.pabji.citycatched.presentation.navigation.Router;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Pablo Jiménez Casado on 06/08/2016.
 */

public class NewBuildingPresenter extends BasePresenter<NewBuildingView> {

    @Inject
    Router router;

    /*@Inject
    FirebaseDatasource firebaseDatasource;*/

    /*@Inject
    FirebaseStorageDatasource firebasePictureStorage;*/

    @Inject
    PermissionUtils permissionUtils;

    final CharSequence[] items = { "Take Photo", "Choose from Library"};

    List<Uri> imagesPath = new ArrayList<>();
    ArrayList<String> uploadedImages = new ArrayList<>();
    private Context context;
    private String userChoosenTask;
    private Fragment fragment;
    private Building building;
    private Uri mainImage;
    private Uri uriImageCapture;
    private int currentFragment;
    private int numberOfFragments;

    @Inject
    public NewBuildingPresenter() {
    }

    public void init(Context context,Building building, Uri uriImage, int size) {
        this.building = building;
        this.context = context;
        this.mainImage = uriImage;
        this.currentFragment = 0;
        this.numberOfFragments = size;
        getView().showMainImage(uriImage);
        getView().loadFragments();
        showCurrentFragment();
    }

    private void showCurrentFragment() {
        checkButtons();
        if(currentFragment < numberOfFragments) {
            getView().showFragment(currentFragment);
        }
    }

    public void checkButtons(){
        if(currentFragment > 0){
            getView().enableBackButton();
        }else{
            getView().disableBackButton();
        }

        if(currentFragment < numberOfFragments -1){
            getView().enableContinueButton();
        }else{
            getView().disableContinueButton();
        }
    }

    public void previousFragment() {
        currentFragment--;
        checkButtons();
    }

    public void nextFragment() {
        currentFragment++;
        showCurrentFragment();
    }

    public void openCamera() {
        File tempFile = FileManager.createTempFile();
        mainImage = FileManager.getUriFromFile(context,tempFile);
        if (mainImage != null) {
            router.openCameraActivity((BaseMVPActivity) context, mainImage);
        }
    }

    public void openGallery() {
        router.openGalleryActivity((Activity) context);
    }

    public void showPictureOptions() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_title_mainphoto);
        builder.setPositiveButton(R.string.dialog_btn_positive_camera, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openCamera();
            }
        });
        builder.setNegativeButton(R.string.dialog_btn_negative_gallery, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openGallery();
            }
        });
        android.support.v7.app.AlertDialog alertDialogPhoto = builder.create();
        alertDialogPhoto.show();

    }

    public void addImageFromGallery(Uri selectedImage) {
        mainImage = selectedImage;
        getView().showMainImage(selectedImage);
    }

    public void addImageFromCamera() {
        getView().showMainImage(mainImage);
    }

    public void setBuildingName(String name){
        building.setName(name);
        getView().showBuildingName(name);
    }

    public MyLocation getBuildingLocation(){
        return building.getMyLocation();
    }

    public void setBuildingLocation(MyLocation myLocation) {
        building.setMyLocation(myLocation);
    }

    /*public void addPicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    File file = FileManager.createTempFile();
                    uriImageCapture = FileManager.getUriFromFile(context,file);
                    imagesPath.add(uriImageCapture);
                    router.openCameraFragment(fragment,uriImageCapture);
                } else {
                    userChoosenTask="Choose from Library";
                    requestPermission(dialog,item);
                    //galleryIntent();
                }
            }
        });
        builder.show();
    }*/
}
