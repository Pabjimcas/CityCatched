package com.example.pabji.siftapplication.foto;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.pabji.siftapplication.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoActivity extends AppCompatActivity {

    private static final String TAG = "PHOTO_ACTIVITY";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private File actuallyPhotoFile;
    private ImageView imageViewPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        doPhotoWithCamera();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d(TAG, "resultCode del captureimage    "+ resultCode);
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
               /* if(data!=null) {
                    Log.d(TAG, "Image saved to:\n" + data.getData());
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imageViewPhoto.setImageBitmap(imageBitmap);
                }
*/
                Bitmap imageBitmap = BitmapFactory.decodeFile(actuallyPhotoFile.getPath());
                //imageViewPhoto.setImageBitmap(imageBitmap);
                setPic(imageBitmap);

            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "Cancelado en onActivityResult de capture image  " + data.getData());
            } else {
                // Image capture failed, advise user
                Log.d(TAG, "Algo raro en onActivityResult de capture image");
            }
        }
    }
}
