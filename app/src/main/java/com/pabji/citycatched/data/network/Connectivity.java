package com.pabji.citycatched.data.network;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Pablo Jiménez Casado on 19/10/2016.
 */

public class Connectivity {

    public static Boolean haveInternetConnection(Context context) {
        if (context != null) {
            try {
                ConnectivityManager conMgr =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                return conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected();
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
