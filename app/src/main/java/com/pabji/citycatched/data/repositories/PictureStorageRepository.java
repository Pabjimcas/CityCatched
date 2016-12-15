package com.pabji.citycatched.data.repositories;

import android.net.Uri;

import com.pabji.citycatched.presentation.mvp.models.Building;

import rx.Observable;

/**
 * Created by Pablo Jiménez Casado on 22/10/2016.
 */

public interface PictureStorageRepository {

    Observable<Integer> savePicture(Uri uri, String name);

    Observable<Boolean> removePictureFirebase(String buildingPath);

}
