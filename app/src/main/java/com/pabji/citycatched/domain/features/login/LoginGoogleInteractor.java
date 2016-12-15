package com.pabji.citycatched.domain.features.login;

import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by pabji on 18/07/2016.
 */

public interface LoginGoogleInteractor {

    void onStart(GoogleApiClient mGoogleApiClient);

    void execute(FirebaseAuth mAuth, FirebaseAuth.AuthStateListener listener);

    void onActivityResult(int requestCode, Intent data, OnCompleteListener onCompleteListener);
}