package com.pabji.citycatched.presentation.mvp.presenters.newBuilding;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.pabji.citycatched.domain.constants.RequestConstants;
import com.pabji.citycatched.presentation.activities.NewBuildingActivity;
import com.pabji.citycatched.presentation.mvp.presenters.base.BasePresenter;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.views.newBuilding.NewBuildingStep1View;
import com.pabji.citycatched.presentation.navigation.Router;
import com.pabji.citycatched.domain.utils.FileManager;
import com.pabji.citycatched.domain.utils.PermissionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Pablo Jiménez Casado on 06/08/2016.
 */

public class NewBuildingStep1Presenter extends BasePresenter<NewBuildingStep1View> {

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
    private NewBuildingPresenter activityPresenter;

    @Inject
    public NewBuildingStep1Presenter() {
    }

    public void init(Context context) {
        activityPresenter = ((NewBuildingActivity) context).getPresenter();
    }

    public void requestPermission(final DialogInterface dialog, final int item){

        permissionUtils.requestReadStoragePermission(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                //router.openGallery(fragment);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        });
    }

    public void addPicture() {
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
                    router.openCameraFragment(fragment,uriImageCapture, RequestConstants.CAPTURE_IMAGE_NEWBUILDING_STEP2);
                } else {
                    userChoosenTask="Choose from Library";
                    requestPermission(dialog,item);
                    //galleryIntent();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RequestConstants.SELECT_IMAGE_GALLERY)
                onSelectFromGalleryResult(data);
            else if (requestCode == RequestConstants.CAPTURE_IMAGE)
                onCaptureImageResult();
        }
    }

    private void onCaptureImageResult() {
        addPictureFirebase(uriImageCapture);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        imagesPath.add(selectedImageUri);
        addPictureFirebase(selectedImageUri);
    }

    public void sendToRevision(){

        String revisionName = getRevisionName();

        /*firebasePictureStorage.addPictureFirebase(mainImage, revisionName, new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                building.mainImage = taskSnapshot.getDownloadUrl().toString();
                firebaseDatasource.sendToRevision(building,building.myLocation.getCity(context),new DataListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        Toast.makeText(context,"Enviado a revisión",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int error) {

                    }
                });
            }
        });*/
    }

    private String getRevisionName() {

        String time = String.valueOf(System.currentTimeMillis());
        return building.myLocation.latitude+"/"+building.myLocation.longitude+"/"+time;
    }

    public void saveBuilding(){

        //building.name = getView().getBuildingName();

        /*firebasePictureStorage.addPictureFirebase(mainImage, building.name, new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                building.mainImage = taskSnapshot.getDownloadUrl().toString();
                building.addressList.add(building.myLocation.getAddress(context));
                building.pictures = uploadedImages;
                firebaseDatasource.saveBuilding(building,new DataListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        Toast.makeText(context,"Terminado", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int error) {

                    }
                });
            }
        });*/

    }

    public void addPictureFirebase(Uri uri) {

        //getView().showProgress();

        //String pictureName = building.myLocation.latitude+building.myLocation.longitude+imagesPath.size();

        /*firebasePictureStorage.addPictureFirebase(uri, pictureName, new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                getView().setProgress(progress.intValue());
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        }, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri uri = taskSnapshot.getDownloadUrl();
                uploadedImages.add(uri.toString());
                getView().hideProgress();
                getView().showPictureList(imagesPath);
            }
        });*/
    }


    public void setBuildingName(String name) {
        activityPresenter.setBuildingName(name);
    }
}
