package com.pabji.citycatched.domain.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.facebook.FacebookSdk.getCacheDir;

/**
 * Created by Pablo Jiménez Casado on 04/08/2016.
 */

public class FileManager {

    public static Uri getUriFromFile(Context context, File file) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, "com.pabji.citycatched.fileprovider", file);
        }else{
            return Uri.fromFile(file);
        }
    }

    public static File createTempFile(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File tempFile = null;
        try {
            tempFile = File.createTempFile(timeStamp, ".jpg", getCacheDir());
            tempFile.setWritable(true, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

}
