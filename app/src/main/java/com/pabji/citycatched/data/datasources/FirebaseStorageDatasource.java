package com.pabji.citycatched.data.datasources;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pabji.citycatched.data.repositories.PictureStorageRepository;
import com.pabji.citycatched.data.utils.Utilities;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Pablo Jiménez Casado on 07/08/2016.
 */

@Singleton
public class FirebaseStorageDatasource implements PictureStorageRepository{
    private ContentResolver contentResolver;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://citycatched-bbccf.appspot.com");

    @Inject
    public FirebaseStorageDatasource(Context context){
        contentResolver = context.getContentResolver();
    }

    @Override
    public Observable<Integer> savePicture(final Uri uri, final String name) {

        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(final Subscriber<? super Integer> subscriber) {

                StorageReference buildingRef = storageRef.child(name + ".jpg");
                byte[] data;
                try {
                    data = Utilities.compressBitmap(contentResolver, uri);

                    if (data != null) {
                        UploadTask uploadTask = buildingRef.putBytes(data);
                        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                Double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                subscriber.onNext(progress.intValue());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                subscriber.onError(e);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                subscriber.onCompleted();
                            }
                        });
                    }
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> removePictureFirebase(final String buildingPath) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                StorageReference desertRef = storageRef.child(buildingPath);
                desertRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        subscriber.onNext(task.isSuccessful());
                        subscriber.onCompleted();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        subscriber.onError(e);
                    }
                });
            }
        });
    }
}
