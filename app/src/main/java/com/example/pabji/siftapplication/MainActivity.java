package com.example.pabji.siftapplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.location.Location;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pabji.siftapplication.object_recog.ObjectRecognizer;
import com.example.pabji.siftapplication.object_recog.Utilities;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private File actuallyPhotoFile;
    private ImageView imageViewPhoto;

    private Mat mRgba;
    private Mat mGray;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 1;

    //private CameraBridgeViewBase cameraView;
    private LinearLayout scrollLinearLayout;

    private Handler handler;

    private String detectedObj;
    private String lastDetectedObj;

    private static ObjectRecognizer recognizer;

    private static final int CAPTURE_IMAGE = 100;
    private static final int RECOGNIZE_IMAGE = 300;
    private static final int TTS_CHECK = 200;

    private ArrayList<File> imageFiles;
    private GoogleApiClient mGoogleApiClient;

    /*static {
        if(!OpenCVLoader.initDebug()){
            Log.d("TAG","Not loaded");
        }else{
            Log.d("TAG","Loaded");
            recognizer = new ObjectRecognizer();
        }
    }*/


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    //cameraView.enableView();
                    //cameraView.setFocusable(true);
                    recognizer = new ObjectRecognizer(MainActivity.this);
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };
    private Location lastLocation;
    private RadioGroup radioGroup;
    private int idBuild;

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, MainActivity.this, mLoaderCallback);
        mGoogleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    TextView zeroObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        scrollLinearLayout = (LinearLayout) findViewById(R.id.scrollLinearLayout);

        detectedObj = "-";

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        handler = new Handler();

        //cameraView = (CameraBridgeViewBase) findViewById(R.id.cameraView);
        //cameraView.setVisibility(View.VISIBLE);
        //cameraView.setCvCameraViewListener(this);

        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, TTS_CHECK);

        // prepare textview to insert if database gets empty
        zeroObjects = new TextView(this);
        zeroObjects.setText("There are no objects in your database yet."
                + "\nStart by clicking the \"New Object\" button above");
        zeroObjects.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        zeroObjects.setPadding(5, 5, 5, 5);

        imageFiles = Utilities.getJPGFiles(getFilesDir());
        if (imageFiles.size() > 0) {
            for (File file : imageFiles) {
                addImageThumbnail(file, -1);
            }
        } else {
            scrollLinearLayout.addView(zeroObjects);
        }
    }

    /*@Override
    public void onPause() {
        super.onPause();
        if (cameraView != null)
            cameraView.disableView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraView != null)
            cameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    @Override
    public void onCameraViewStopped() {
    }


    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        /*mGray = inputFrame.gray();
        lastDetectedObj = detectedObj;
        detectedObj = recognizer.recognize(mGray);

        handler.post(new EditViewRunnable());

        return mRgba;
    }*/

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        lastLocation = null;
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                Log.v(TAG, "Permission is granted");
                lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        /*if (lastLocation != null) {
            Double locationLatitudeCasaConchas = 40.9628577;
            Double locationLongitudeCasaConchas = -5.6660739;
            //tvLocalizacion.setText(lastLocation.getLatitude() + " , " + lastLocation.getLongitude());
            if (lastLocation.getLatitude() < locationLatitudeCasaConchas + 0.00001 && lastLocation.getLatitude() > locationLatitudeCasaConchas - 0.00001 &&
                    lastLocation.getLongitude() < locationLongitudeCasaConchas + 0.00001 && lastLocation.getLongitude() > locationLongitudeCasaConchas - 0.00001) {
                tvRango.setText("Estas en el rango guapeton");
            } else {
                tvRango.setText("No estas en el rango guapeton");
            }

        }*/

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /*private class EditViewRunnable implements Runnable {
        @Override
        public void run() {
            TextView detectedObjTextView = (TextView) findViewById(R.id.detectedObjTextView);
            detectedObjTextView.setText(detectedObj);
        }
    }*/

    // creates a horizontal linear layout containing the thumbnail of the image
    // in the given file together with its name
    // and inserts it in the activity's scrollView
    private void addImageThumbnail(File file, int index) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView image = new ImageView(this);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(file.getPath(), options), 64, 64);
        image.setImageBitmap(thumbnail);
        image.setPadding(5, 5, 5, 5);

        TextView text = new TextView(this);
        text.setText(file.getName().substring(0,
                file.getName().lastIndexOf(".")));
        text.setPadding(5, 5, 5, 5);


        layout.addView(image);
        layout.addView(text);
        layout.setOnClickListener(viewObject());

        if (index != -1) {
            scrollLinearLayout.addView(layout, index);
        } else {
            scrollLinearLayout.addView(layout);
        }
    }

    int clickedImgIdx;

    // creates a dialog to show a larger image of the clicked object
    View.OnClickListener viewObject() {
        return new View.OnClickListener() {
            public void onClick(View view) {
                clickedImgIdx = scrollLinearLayout.indexOfChild(view);
                File imageFile = imageFiles.get(clickedImgIdx);

                final Dialog imageDialog = new Dialog(MainActivity.this);
                imageDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

                LinearLayout dialogLayout = new LinearLayout(MainActivity.this);
                dialogLayout.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                dialogLayout.setOrientation(LinearLayout.VERTICAL);
                imageDialog.addContentView(dialogLayout, new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

                ImageView fullSizeImage = new ImageView(MainActivity.this);
                fullSizeImage.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap fullSizeBM = BitmapFactory.decodeFile(
                        imageFile.getPath(), options);
                fullSizeImage.setImageBitmap(fullSizeBM);
                fullSizeImage.setAdjustViewBounds(true);
                dialogLayout.addView(fullSizeImage);

                LinearLayout buttonsLayout = new LinearLayout(MainActivity.this);
                buttonsLayout.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);

                Button deleteBtn = new Button(MainActivity.this);
                deleteBtn.setText("Delete");
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //cameraView.disableView();
                        imageDialog.dismiss();
                        // delete file
                        File toBeDeteled = imageFiles.get(clickedImgIdx);
                        toBeDeteled.delete();

                        // adjust UI
                        imageFiles.remove(clickedImgIdx);
                        scrollLinearLayout.removeViewAt(clickedImgIdx);

                        // adjust recognizer
                        recognizer.removeObject(clickedImgIdx);
                        //cameraView.enableView();

                        // if database gets empty insert zeroObjects textview
                        if (imageFiles.size() == 0) {
                            scrollLinearLayout.addView(zeroObjects);
                        }
                    }
                });
                buttonsLayout.addView(deleteBtn);

                Button cancelBtn = new Button(MainActivity.this);
                cancelBtn.setText("Cancel");
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageDialog.dismiss();
                    }
                });

                buttonsLayout.addView(cancelBtn);
                dialogLayout.addView(buttonsLayout);

                imageDialog.show();

            }
        };
    }

    private String newImgFilename;
    private static File tempDir = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

    // pops up a dialog where user enters the name of the new object and
    // continues to capture its image using a camera
    public void recognizePhoto(View view) {
        //cameraView.disableView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPhotoWithCamera();
            }
        });

    }


    public void addObject(View view) {
        //cameraView.disableView();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);

        builder.setCancelable(false)
                .setTitle("New Object")
                .setMessage("Choose a name for your new object")
                .setView(input)
                .setPositiveButton("Proceed", null)
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.cancel();
                                //cameraView.enableView();
                            }
                        });
        

        
        LinearLayout viewDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_selector_fragment,null);
        radioGroup = (RadioGroup) viewDialog.findViewById(R.id.rg_selector);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.build_1:
                        idBuild = 1;
                        break;
                    case R.id.build_2:
                        idBuild = 2;
                        break;
                    case R.id.build_3:
                        idBuild = 3;
                        break;
                    case R.id.build_4:
                        idBuild = 4;
                        break;
                    case R.id.build_5:
                        idBuild = 5;
                        break;
                }
            }
        });
        final AlertDialog dialog = builder.setView(viewDialog).setTitle("Selecciona el edificio a entrenar").create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        doPhotoWithCamera2();
                        /*newImgFilename = String.valueOf(idBuild);
                        if (newImgFilename != null
                                && newImgFilename.length() <= 127
                                && newImgFilename.matches("[a-zA-Z1-9 ]+")
                                && !newImgFilename.matches(" +")) {
                            Intent camera = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            File tempFile = new File(tempDir, newImgFilename
                                    + ".jpg");
                            try {
                                tempFile.createNewFile();
                                Uri imageUri = Uri.fromFile(tempFile);
                                camera.putExtra(MediaStore.EXTRA_OUTPUT,
                                        imageUri);
                                startActivityForResult(camera, CAPTURE_IMAGE);
                                Toast instructionsToast = Toast
                                        .makeText(getBaseContext(), "For best results:\n"
                                                        + "- Fill the photo with your object.\n"
                                                        + "- Avoid harsh lighting.",
                                                Toast.LENGTH_LONG);
                                instructionsToast.show();

                            } catch (IOException e) {
                                Toast invalidToast = Toast
                                        .makeText(MainActivity.this, "Invalid name.\nObject was not created", Toast.LENGTH_SHORT);
                                invalidToast.show();
                                //cameraView.enableView();
                            } finally {
                                dialog.dismiss();
                            }
                        } else {
                            dialog.setMessage("Choose a name for your new object\n\nThe name you entered is invalid. Please retry.");
                        }*/
                    }
                });
            }
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d(TAG, "resultCode del captureimage    "+ resultCode);
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
               /* if(data!=null) {
                    Log.d(TAG, "Image saved to:\n" + data.getData());
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imageViewPhoto.setImageBitmap(imageBitmap);
                }*/
                Mat fullSizeTrainImg = Highgui.imread(actuallyPhotoFile.getPath());
			Mat resizedTrainImg = new Mat();
			Imgproc.resize(fullSizeTrainImg, resizedTrainImg, new Size(640, 480), 0, 0, Imgproc.INTER_CUBIC);
                detectedObj = recognizer.recognize(resizedTrainImg);
                Firebase mref = new Firebase("https://city-catched.firebaseio.com/buildings");
                mref.child(detectedObj).child("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String s = dataSnapshot.getValue(String.class);
                        Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                //Bitmap imageBitmap = BitmapFactory.decodeFile(actuallyPhotoFile.getPath());


                //imageViewPhoto.setImageBitmap(imageBitmap);
                //setPic(imageBitmap);

            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "Cancelado en onActivityResult de capture image  " + data.getData());
            } else {
                // Image capture failed, advise user
                Log.d(TAG, "Algo raro en onActivityResult de capture image");
            }
        } else if (requestCode == CAPTURE_IMAGE) {
            if (resultCode == RESULT_OK) {

                Mat fullSizeTrainImg = Highgui.imread(actuallyPhotoFile.getPath());
                Mat resizedTrainImg = new Mat();
                Imgproc.resize(fullSizeTrainImg, resizedTrainImg, new Size(640, 480), 0, 0, Imgproc.INTER_CUBIC);

                Mat descriptors = recognizer.getDescriptorsImage(resizedTrainImg);

                byte[] data2 = new byte[(int) (descriptors.total() * descriptors.channels())];
                descriptors.get(0, 0, data2);

                Firebase mref = new Firebase("https://city-catched.firebaseio.com/descriptors");
                mref.child(String.valueOf(idBuild)).child(String.valueOf(System.currentTimeMillis())).setValue(Utilities.encodeImage(data2));




                /*File newFile = new File(getFilesDir(), newImgFilename + ".jpg");
                File tempFile = new File(tempDir, newImgFilename + ".jpg");

                try {
                    Utilities.copyFile(tempFile, newFile);
                } catch (IOException e) {

                }

                tempFile.delete();

                // apply change to UI
                // if database was previously empty, remove zeroObjects textview
                if (imageFiles.size() == 0) {
                    scrollLinearLayout.removeView(zeroObjects);
                }
                imageFiles.add(newFile);
                recognizer.updateFirebase(getFilesDir());

                /*Collections.sort(imageFiles);
                int newFileIdx = imageFiles.indexOf(newFile);
                addImageThumbnail(newFile, newFileIdx);*/

            } else if (resultCode == RESULT_CANCELED) {
                super.onActivityResult(requestCode, resultCode, data);
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


    private void doPhotoWithCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri fileUri = null; // create a file to save the image
        try {
            fileUri = getOutputMediaFileUri();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "doPhotoWithCamera  " + fileUri.getEncodedPath());

        if(fileUri!=null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

            // start the image capture Intent
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
        else{
            Log.d(TAG, "file URI null");
        }
    }
    private void doPhotoWithCamera2(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri fileUri = null; // create a file to save the image
        try {
            fileUri = getOutputMediaFileUri();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "doPhotoWithCamera2  " + fileUri.getEncodedPath());

        if(fileUri!=null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

            // start the image capture Intent
            startActivityForResult(intent, CAPTURE_IMAGE);
        }
        else{
            Log.d(TAG, "file URI null");
        }
    }

    private  Uri getOutputMediaFileUri() throws IOException {
        //  Log.d(TAG, "getOutPutMediFileUri    " + createFile().getAbsolutePath());
        return Uri.fromFile(createFile());
    }

    //Create file in cache and accesible for the camera app
    private File createFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File tempFile = File.createTempFile(timeStamp, ".jpg", getCacheDir());
        tempFile.setWritable(true, false);
        actuallyPhotoFile = tempFile;
        return tempFile;
    }


    //Change dimension of the image
    private void setPic(Bitmap immutableBitmap){
        // Get the dimensions of the View
        int targetW = imageViewPhoto.getWidth();
        int targetH = imageViewPhoto.getHeight();


        Bitmap output = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) targetW / immutableBitmap.getWidth(), (float) targetH / immutableBitmap.getHeight());
        canvas.drawBitmap(immutableBitmap, m, new Paint());
        Log.d(TAG, "Mutableeeeeeee");
        imageViewPhoto.setImageBitmap(output);

    }

}
