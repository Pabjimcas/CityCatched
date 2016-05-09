package com.example.pabji.siftapplication.foto;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pabji.siftapplication.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TakePhotoFragment extends Fragment {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public TakePhotoFragment() {
        // Required empty public constructor
    }


    public static TakePhotoFragment newInstance() {
        TakePhotoFragment fragment = new TakePhotoFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_take_photo, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        //fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        try {
            fileUri = getOutputMediaFileUri();
        } catch (IOException e) {
            Log.d("ERRRRORRRR", "NO  SE CREOOO EL FICHERRROOOO");
            e.printStackTrace();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(getActivity(), "Image saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }



    private  Uri getOutputMediaFileUri() throws IOException {
        return Uri.fromFile(createTempFile());
    }

    //** Create a File for saving an image or video *//*
   /* private File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

       File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
               Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        return mediaFile;
    }*/
    /*private File getInternalStorage(int type) throws FileNotFoundException {
        File mediaFile = new File("h");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String FileName = new String("IMG_" + timeStamp + ".jpg");

        FileOutputStream fos = getActivity().openFileOutput(FileName,Context.MODE_PRIVATE);
        return mediaFile;
    }*/

    private File createTempFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File outPutDir = getActivity().getCacheDir();
        File outPutFile = File.createTempFile(timeStamp,".jpg",outPutDir);
        return outPutFile;
    }


}
