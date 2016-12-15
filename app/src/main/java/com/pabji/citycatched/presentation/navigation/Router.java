package com.pabji.citycatched.presentation.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.pabji.citycatched.domain.constants.RequestConstants;
import com.pabji.citycatched.presentation.activities.BuildingCommentsActivity;
import com.pabji.citycatched.presentation.activities.BuildingGalleryActivity;
import com.pabji.citycatched.presentation.activities.BuildingMapActivity;
import com.pabji.citycatched.presentation.activities.WebviewActivity;
import com.pabji.citycatched.presentation.activities.base.BaseActivity;
import com.pabji.citycatched.presentation.activities.base.BaseMVPActivity;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.activities.BuildingActivity;
import com.pabji.citycatched.presentation.activities.LoginActivity;
import com.pabji.citycatched.presentation.activities.MainActivity;
import com.pabji.citycatched.presentation.activities.NewBuildingActivity;
import com.pabji.citycatched.presentation.activities.NoRecognizedActivity;
import com.pabji.citycatched.presentation.activities.RevisionListActivity;
import com.pabji.citycatched.presentation.mvp.models.MyLocation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by pabji on 16/06/2016.
 */

public class Router {

    Context context;

    @Inject
    public Router(Context context){
        this.context = context;
    }

    public void openMain(){
        if (context != null) {
            Intent intent = MainActivity.getCallingIntent(context);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public void openRevisionList(){
        if (context != null) {
            Intent intent = RevisionListActivity.getCallingIntent(context);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public void openLogin() {
        if (context != null) {
            Intent intent = LoginActivity.getCallingIntent(context);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }


    public void openCameraFragment(Fragment fragment, Uri fileUri, int request){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        fragment.startActivityForResult(intent, request);
    }

    public void openGalleryFragment(Fragment fragment, int request){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        fragment.startActivityForResult(Intent.createChooser(intent, "Select File"), request);
    }


    public void openGalleryActivity(Activity activity){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select File"), RequestConstants.SELECT_IMAGE_GALLERY);
    }

    public void finishActivity(Context context) {
        ((BaseActivity) context).finish();
    }

    public void openDetailBuilding(Building building) {
        if (context != null) {
            Intent intent = BuildingActivity.getCallingIntent(context);
            intent.putExtra("building",building);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public void openNoRecognized(MyLocation location, String descriptorDetected){
        if (context != null) {
            Intent intent = NoRecognizedActivity.getCallingIntent(context);
            intent.putExtra("myLocation",location);
            intent.putExtra("descriptor",descriptorDetected);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public void closeFragment(Context context) {
        ((BaseActivity)context).getSupportFragmentManager().popBackStack();
    }

    public void openNewBuilding(Bundle extras) {
        Intent intent = NewBuildingActivity.getCallingIntent(context);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void openEditBuilding(Building building) {

    }

    public void openCameraActivity(BaseMVPActivity activity, Uri fileUri) {
        if(context != null) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            activity.startActivityForResult(intent, RequestConstants.CAPTURE_IMAGE);
        }
    }

    public void openBuildingPicturesGallery(ArrayList<String> buildingPictures, int position) {
        if (context != null) {
            Intent intent = BuildingGalleryActivity.getCallingIntent(context);
            intent.putStringArrayListExtra("pictures",buildingPictures);
            intent.putExtra("position",position);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public void openMap(String buildingImage) {
        if (context != null) {
            Intent intent = BuildingMapActivity.getCallingIntent(context);
            intent.putExtra("buildingImage",buildingImage);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public void openComments(String buildingImage) {
        if (context != null) {
            Intent intent = BuildingCommentsActivity.getCallingIntent(context);
            intent.putExtra("buildingImage",buildingImage);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public void openWebview(String urlDescription) {
        if (context != null) {
            Intent intent = WebviewActivity.getCallingIntent(context);
            intent.putExtra("buildingUrl",urlDescription);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
