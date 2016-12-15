package com.pabji.citycatched.presentation.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.pabji.citycatched.R;
import com.pabji.citycatched.domain.components.DaggerMainActivityComponent;
import com.pabji.citycatched.domain.constants.RequestConstants;
import com.pabji.citycatched.domain.scopes.HasComponent;
import com.pabji.citycatched.presentation.activities.base.BaseMVPActivity;
import com.pabji.citycatched.domain.components.MainActivityComponent;
import com.pabji.citycatched.domain.modules.MainActivityModule;
import com.pabji.citycatched.presentation.fragments.BuildingListVisitedFragment;
import com.pabji.citycatched.presentation.fragments.CameraVideoFragment;
import com.pabji.citycatched.presentation.fragments.MainFragment;
import com.pabji.citycatched.presentation.mvp.presenters.MainActivityPresenter;
import com.pabji.citycatched.presentation.mvp.views.MainActivityView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends BaseMVPActivity<MainActivityPresenter,MainActivityView> implements MainActivityView, HasComponent<MainActivityComponent>, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    MainActivityPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.fab)
    FloatingActionButton photoButton;
    @BindView(R.id.fab2)
    FloatingActionButton videoButton;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private MainActivityComponent component;

    private Unbinder unbind;
    private Uri uriFile;
    private GoogleApiClient googleApiClient;
    private ProgressDialog progressDialog;
    private String filePath;
    private MainFragment mainFragment;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.initializeInjector();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbind = ButterKnife.bind(this);

        setupDrawerToolbar(toolbar);

        if(savedInstanceState != null){
            uriFile = savedInstanceState.getParcelable("uri");
            filePath = savedInstanceState.getString("filePath");
        }

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        navigationView.setNavigationItemSelectedListener(this);

        presenter.init(uriFile);
        mainFragment = MainFragment.newInstance();
        selectFragment(R.id.nav_main);
    }

    public void setUpFragment(Fragment fragment){
        addFragment(R.id.content_main, fragment);
    }

    public void selectFragment(int id_nav){
        switch (id_nav){
            case R.id.nav_main:
                setUpFragment(mainFragment);
                break;
            case R.id.nav_visites:
                setUpFragment(BuildingListVisitedFragment.newInstance());
                break;
            default:
                setUpFragment(mainFragment);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(googleApiClient != null && !googleApiClient.isConnecting() && !googleApiClient.isConnected()){
            googleApiClient.connect();
        }
        presenter.checkOpenCV();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient.isConnecting() || googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind.unbind();
    }

    @NonNull
    @Override
    public MainActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public MainActivityComponent getComponent() {
        return component;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestConstants.CAPTURE_IMAGE) {
            if (resultCode == RESULT_OK) {
                presenter.recognizeImage(filePath);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uriFile = presenter.getUri();
        filePath = presenter.getFilePath();
        outState.putParcelable("uri",uriFile);
        outState.putString("filePath",filePath);
    }

    private void initializeInjector() {
        this.component = DaggerMainActivityComponent.builder()
                .cityCatchedApplicationComponent(getInjector())
                .mainActivityModule(new MainActivityModule(this))
                .build();
        component.inject(this);
    }

    @OnClick(R.id.fab)
    @Override
    public void photoButton() {
        presenter.takePhoto();
    }

    @OnClick(R.id.fab2)
    public void videoButton() {
        presenter.openVideo();
    }

    @Override
    public void showPhotoButton() {
        photoButton.setVisibility(View.VISIBLE);
        videoButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePhotoButton() {
        photoButton.setVisibility(View.GONE);
        videoButton.setVisibility(View.GONE);
    }

    @Override
    public void showProgressDialog(int messageResource) {
        progressDialog.setMessage(getString(messageResource));
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void openCameraVideo() {
        Intent intent = CameraVideoActivity.getCallingIntent(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void canReadNearBuildingList() {
        mainFragment.loadNearBuildings();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        presenter.getLocation(googleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"Conexión suspendida",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"Conexión suspendida",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        selectFragment(id);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
