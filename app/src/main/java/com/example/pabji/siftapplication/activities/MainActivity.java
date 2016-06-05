package com.example.pabji.siftapplication.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.example.pabji.siftapplication.R;
import com.example.pabji.siftapplication.adapters.ItemListAdapter;
import com.example.pabji.siftapplication.models.Building;
import com.example.pabji.siftapplication.persistence.CityDBHelper;
import com.example.pabji.siftapplication.persistence.CitySQLiteOpenHelper;
import com.example.pabji.siftapplication.utils.ObjectRecognizer;
import com.example.pabji.siftapplication.utils.Utilities;
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
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    private File actuallyPhotoFile;
    private RecyclerView recyclerView;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 1;

    private String detectedObj;
    private Firebase firebase;
    public static ObjectRecognizer recognizer;
    public List nearBuilding = new ArrayList();

    private static final int CAPTURE_IMAGE = 100;

    private GoogleApiClient mGoogleApiClient;

    private Toolbar mToolbar;

    //For SQLITe
    public static SQLiteDatabase db;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    getLocationFirebase();
                    if (nearBuilding != null)
                        recognizer = new ObjectRecognizer(MainActivity.this, nearBuilding);
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
    private ItemListAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, MainActivity.this, mLoaderCallback);
        mGoogleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        setDB();
        detectedObj = "-";
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        final List<Building> buildings = CityDBHelper.getBuildings(db);

        adapter = new ItemListAdapter(this, buildings);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SecondActivity.class);
                Building building = buildings.get(recyclerView.getChildAdapterPosition(v));

                intent.putExtra("name", building.getName());
                intent.putExtra("description", building.getDescription());
                intent.putExtra("latitude", building.getLatitude());
                intent.putExtra("url_image", building.getUrl_image());
                intent.putExtra("longitude", building.getLatitude());
                intent.putExtra("intro",building.getIntro());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getLocationFirebase();
    }

    @Override
    public void onConnected(Bundle bundle) {
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

    @SuppressWarnings("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            Log.v(TAG, "Permission: " + permissions[1] + "was " + grantResults[1]);
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } else {
            finish();
        }
    }


    public void recognizePhoto(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPhotoWithCamera(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

    }


    public void addObject() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);

        builder.setCancelable(false)
                .setTitle("Edificios disponibles")
                .setMessage("Elija uno de los edificios")
                .setView(input)
                .setPositiveButton("Aceptar", null)
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.cancel();
                            }
                        });

        LinearLayout viewDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_selector_fragment, null);
        radioGroup = (RadioGroup) viewDialog.findViewById(R.id.rg_selector);
        //addRadioButtons(nearBuilding);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
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
                        if (radioGroup.getCheckedRadioButtonId() == -1){

                        }else{
                            dialog.dismiss();
                            setPhotoToBuilding();

                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Felicidades")
                                    .setMessage("Muchas gracias por ayudar a perfeccionar la aplicación")
                                    .setPositiveButton(android.R.string.ok, null) // dismisses by default
                                    .create()
                                    .show();


//                            doPhotoWithCamera(CAPTURE_IMAGE);
                        }
                    }
                });
            }
        });

        dialog.show();


    }

    private void setPhotoToBuilding(){
        Mat fullSizeTrainImg = Highgui.imread(actuallyPhotoFile.getPath());
        Mat resizedTrainImg = new Mat();
        Imgproc.resize(fullSizeTrainImg, resizedTrainImg, new Size(640, 480), 0, 0, Imgproc.INTER_CUBIC);

        Mat descriptors = recognizer.getDescriptorsImage(resizedTrainImg);

        byte[] data2 = new byte[(int) (descriptors.total() * descriptors.channels())];
        descriptors.get(0, 0, data2);

        Firebase mref = new Firebase("https://city-catched.firebaseio.com/descriptors");
        mref.child(String.valueOf(idBuild)).child(String.valueOf(System.currentTimeMillis())).setValue(Utilities.encodeImage(data2));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d(TAG, "resultCode del captureimage    " + resultCode);
            if (resultCode == RESULT_OK) {

                Mat fullSizeTrainImg = Highgui.imread(actuallyPhotoFile.getPath());
                Mat resizedTrainImg = new Mat();
                Imgproc.resize(fullSizeTrainImg, resizedTrainImg, new Size(640, 480), 0, 0, Imgproc.INTER_CUBIC);
                detectedObj = recognizer.recognize(resizedTrainImg);
                Firebase mref = new Firebase("https://city-catched.firebaseio.com/buildings");
                mref.child(detectedObj).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("TAG", dataSnapshot.getKey());
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String description = dataSnapshot.child("description").getValue(String.class);
                        String url_image = dataSnapshot.child("url_image").getValue(String.class);
                        String latitude = dataSnapshot.child("latitude").getValue(String.class);
                        String longitude = dataSnapshot.child("longitude").getValue(String.class);
                        String intro = dataSnapshot.child("intro").getValue(String.class);

                        if (name == null) {

                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Edificio no reconocido")
                                    .setIcon(R.drawable.build_failed)
                                    .setMessage("¿Desea ayudar a que la App crezca y aprenda seleccionando un edificio?")
                                    .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            addObject();
                                        }
                                    })
                                    .create()
                                    .show();

                        } else {
                            CityDBHelper.insertBuilding(db,intro, name, description, url_image, latitude, longitude, dataSnapshot.getKey());
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, SecondActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("description", description);
                            intent.putExtra("latitude", latitude);
                            intent.putExtra("url_image", url_image);
                            intent.putExtra("id", dataSnapshot.getKey());
                            intent.putExtra("longitude", longitude);
                            intent.putExtra("intro",intro);
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

    private void doPhotoWithCamera(int code){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri fileUri = null; // create a file to save the image
        try {
            fileUri = getOutputMediaFileUri();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "doPhotoWithCamera  " + fileUri.getEncodedPath());

        if(fileUri!=null) {
            intent.putExtra("Image",actuallyPhotoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
            startActivityForResult(intent, code);
        }
        else{
            Log.d(TAG, "file URI null");
        }
    }

    private  Uri getOutputMediaFileUri() throws IOException {
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

    private void setDB() {
        if (db == null) {
            CitySQLiteOpenHelper cityDB = CitySQLiteOpenHelper.getInstance(this, CitySQLiteOpenHelper.DATABASE_NAME, null, CitySQLiteOpenHelper.DATABASE_VERSION);

            db = cityDB.getWritableDatabase();
        }
    }

    public void getLocationFirebase() {
        nearBuilding.clear();
        firebase = new Firebase("https://city-catched.firebaseio.com/buildings");
        firebase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Double latitude = postSnapshot.child("latitude").getValue(Double.class);
                    Double longitude = postSnapshot.child("longitude").getValue(Double.class);
                    if (40.9628 < latitude + 0.0015 && 40.9628 > latitude - 0.0015 &&
                            -5.6659 < longitude + 0.0015 && -5.6659 > longitude - 0.0015) {
                        nearBuilding.add(postSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
