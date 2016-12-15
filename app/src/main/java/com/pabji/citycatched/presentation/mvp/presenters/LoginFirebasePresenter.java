package com.pabji.citycatched.presentation.mvp.presenters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pabji.citycatched.presentation.mvp.presenters.base.BasePresenter;
import com.pabji.citycatched.data.repositories.ApiClientRepository;
import com.pabji.citycatched.domain.features.login.LoginGoogleInteractor;
import com.pabji.citycatched.presentation.navigation.Router;
import com.pabji.citycatched.presentation.mvp.views.LoginFirebaseView;

import javax.inject.Inject;

import timber.log.Timber;

public class LoginFirebasePresenter extends BasePresenter<LoginFirebaseView> implements GoogleApiClient.OnConnectionFailedListener{

    @Inject
    Router router;

    @Inject
    LoginGoogleInteractor loginGoogleInteractor;

    @Inject
    ApiClientRepository apiClientRepository;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;

    private FragmentActivity fragmentActivity;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Boolean hasLogued = false;
    private CallbackManager mCallbackManager;
    private boolean google;


    @Inject
    public LoginFirebasePresenter() {
    }

    public void init(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        initAuth(fragmentActivity);
    }


    private void initAuth(FragmentActivity fragment) {

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        apiClientRepository.init(fragment,this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null && !hasLogued) {
                    // User is signed in
                    hasLogued = true;

                        router.openMain();
                        //router.finishActivity(fragmentActivity);
                } else {
                    // User is signed out
                    hasLogued = false;
                }
            }
        };

        loginGoogleInteractor.execute(mAuth,mAuthListener);

    }

    /*public void signOut(){
        apiClientRepository.signOut(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });
    }

    public void disconnect(){
        apiClientRepository.revokeAccess(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });
    }*/

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(google) {
            loginGoogleInteractor.onActivityResult(requestCode, data, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {

                        Toast.makeText(fragmentActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    } else {

                        currentUser = mAuth.getCurrentUser();
                        final DatabaseReference myRef = database.getReference("Users");
                        myRef.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    myRef.child(currentUser.getUid()).setValue(currentUser.getUid());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            });
        }else{
            mCallbackManager.onActivityResult(requestCode,resultCode,data);
        }
    }

    public void onStart(){
        mAuth.addAuthStateListener(mAuthListener);
        loginGoogleInteractor.onStart(apiClientRepository.getGoogleApiClient());
    }

    public void onStop(){
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(apiClientRepository.getGoogleApiClient()).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        FirebaseAuth.getInstance().signOut();
                        //updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        /*try {
            connectionResult.startResolutionForResult((Activity) context, Constants.PLUS_CONNECT_RECOVER_ERROR_CODE);
        } catch (Exception e) {
            listener.onError(e.getMessage());
        }*/
    }

    public void signInGoogle() {
        apiClientRepository.signIn();
        google = true;
    }

    public void signInFacebook(LoginButton facebookButton) {
        google = false;
        mCallbackManager = CallbackManager.Factory.create();
        facebookButton.setReadPermissions("email", "public_profile");
        facebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Timber.d("facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Timber.d("facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Timber.d("facebook:onError");
                // ...
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Timber.d( "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(fragmentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Timber.d( "signInWithCredential:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Timber.d(task.getException());
                            Toast.makeText(fragmentActivity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else {

                            currentUser = mAuth.getCurrentUser();
                            final DatabaseReference myRef = database.getReference("Users");
                            myRef.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) {
                                        myRef.child(currentUser.getUid()).setValue(currentUser.getUid());
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        // ...
                    }
                });
    }
}
