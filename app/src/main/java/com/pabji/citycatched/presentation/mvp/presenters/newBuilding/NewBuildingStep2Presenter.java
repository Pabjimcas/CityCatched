package com.pabji.citycatched.presentation.mvp.presenters.newBuilding;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.pabji.citycatched.R;
import com.pabji.citycatched.domain.constants.RequestConstants;
import com.pabji.citycatched.domain.utils.FileManager;
import com.pabji.citycatched.presentation.mvp.presenters.base.BasePresenter;
import com.pabji.citycatched.presentation.mvp.views.newBuilding.NewBuildingStep2View;
import com.pabji.citycatched.presentation.navigation.Router;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Pablo Jiménez Casado on 06/08/2016.
 */

public class NewBuildingStep2Presenter extends BasePresenter<NewBuildingStep2View> {

    @Inject
    Router router;

    List<Uri> imagesPath = new ArrayList<>();
    private Uri currentImage;
    private Fragment fragment;

    @Inject
    public NewBuildingStep2Presenter() {
    }

    public void init(Fragment fragment) {
        this.fragment = fragment;
    }

    public void showPictureOptions() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(fragment.getContext());
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

    public void openCamera() {
        File tempFile = FileManager.createTempFile();
        currentImage = FileManager.getUriFromFile(fragment.getContext(),tempFile);
        if (currentImage != null) {
            router.openCameraFragment(fragment, currentImage,RequestConstants.CAPTURE_IMAGE_NEWBUILDING_STEP2);
        }
    }

    public void openGallery() {
        router.openGalleryFragment(fragment,RequestConstants.SELECT_IMAGE_GALLERY_NEWBUILDING_STEP2);
    }

    public void addImageFromGallery(Uri selectedImage) {
        currentImage = selectedImage;
        imagesPath.add(currentImage);
        getView().showPhotoList(imagesPath);
    }

    public void addImageFromCamera() {
        imagesPath.add(currentImage);
        getView().showPhotoList(imagesPath);
    }
}
