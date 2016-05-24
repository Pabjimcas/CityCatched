package com.example.pabji.siftapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pabji.siftapplication.description.DescriptionActivity;
import com.example.pabji.siftapplication.helpers.CityDBHelper;
import com.example.pabji.siftapplication.models.Building;
import com.example.pabji.siftapplication.models.CitySQLiteOpenHelper;
import com.example.pabji.siftapplication.object_recog.ObjectRecognizer;
import com.example.pabji.siftapplication.object_recog.Utilities;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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


    //For SQLITe
    private SQLiteDatabase db;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
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

        setDB();

        setContentView(R.layout.activity_main);
        scrollLinearLayout = (LinearLayout) findViewById(R.id.scrollLinearLayout);

        detectedObj = "-";

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        handler = new Handler();

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

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

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
                        imageDialog.dismiss();
                        // delete file
                        File toBeDeteled = imageFiles.get(clickedImgIdx);
                        toBeDeteled.delete();

                        // adjust UI
                        imageFiles.remove(clickedImgIdx);
                        scrollLinearLayout.removeViewAt(clickedImgIdx);

                        // adjust recognizer
                        recognizer.removeObject(clickedImgIdx);

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
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPhotoWithCamera();
            }
        });

    }


    public void addObject(View view) {
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

                Mat fullSizeTrainImg = Highgui.imread(actuallyPhotoFile.getPath());
			Mat resizedTrainImg = new Mat();
			Imgproc.resize(fullSizeTrainImg, resizedTrainImg, new Size(640, 480), 0, 0, Imgproc.INTER_CUBIC);
                detectedObj = recognizer.recognize(resizedTrainImg);
                Firebase mref = new Firebase("https://city-catched.firebaseio.com/buildings");
                mref.child(detectedObj).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String description = dataSnapshot.child("description").getValue(String.class);
                        String url_image = dataSnapshot.child("url_image").getValue(String.class);
                        if (description == null && name == null && url_image == null) {
                            Toast.makeText(MainActivity.this, "Intentelo de nuevo", Toast.LENGTH_SHORT).show();
                        } else {
                            saveBuildingToSQLite(name,description,url_image);
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, DescriptionActivity.class);
                            intent.putExtra("description", description);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

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

    //For SQLite
    private ArrayList<Building> getBuildingsFromSQLite(){


        ArrayList<Building> buildings = CityDBHelper.getBuildings(db);

        //Quitarlo despues del debug
        if(!buildings.isEmpty()) {
            Log.d(TAG, "costume: " + buildings.get(0).getName());
        }
        else{
            Log.d(TAG, "vacia");
        }

        return buildings;
    }

    private void saveBuildingToSQLite(String name, String description, String url_image) {

        long rows = CityDBHelper.insertBuilding(db,name,description,url_image);

        //Quitarlo despues del debug
        if(rows>0){
            Log.d(TAG, "ha habido inserciones");
        }
        else{
            Log.d(TAG, "error insertando posiblemente");
        }

    }
    private void setDB(){
        if(db==null){
            CitySQLiteOpenHelper cityDB = CitySQLiteOpenHelper.getInstance(this, CitySQLiteOpenHelper.DATABASE_NAME, null, CitySQLiteOpenHelper.DATABASE_VERSION);

            db = cityDB.getWritableDatabase();
        }
    }

}
