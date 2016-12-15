package com.pabji.citycatched.data.repositories;

import android.content.Context;
import android.util.Log;

import com.pabji.citycatched.data.network.Connectivity;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Pablo Jiménez Casado on 19/10/2016.
 */

public class BaseRepository {

    @Inject @Named("ApplicationContext")
    Context context;

    protected boolean haveInternetConnection() {
        return Connectivity.haveInternetConnection(context);
    }
}
