package com.pabji.citycatched.domain.features.login;

import android.content.Intent;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pabji.citycatched.data.constants.FirebaseConstants;
import com.pabji.citycatched.domain.scopes.PerActivity;

import javax.inject.Inject;

/**
 * Created by pabji on 18/07/2016.
 */

@PerActivity
public class LoginFacebookInteractorImpl implements LoginFacebookInteractor {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private OnCompleteListener<AuthResult> onCompleteListener;

    @Inject
    public LoginFacebookInteractorImpl(){}


    @Override
    public void execute(FirebaseAuth mAuth, FirebaseAuth.AuthStateListener listener) {
        this.mAuthListener = listener;
        this.mAuth = mAuth;
    }

    @Override
    public void onActivityResult(int requestCode,Intent data,OnCompleteListener completeListener) {
        if (requestCode == FirebaseConstants.RC_SIGN_IN) {
            this.onCompleteListener = completeListener;
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(onCompleteListener);
    }
}
