package com.pabji.citycatched;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.karumi.dexter.Dexter;
import com.pabji.citycatched.domain.components.CityCatchedApplicationComponent;
import com.pabji.citycatched.domain.components.DaggerCityCatchedApplicationComponent;
import com.pabji.citycatched.domain.modules.CityCatchedApplicationModule;

public class CityCatchedApplication extends Application {
    private CityCatchedApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerCityCatchedApplicationComponent.builder()
                .cityCatchedApplicationModule(new CityCatchedApplicationModule(this))
                .build();
        initFacebook();
        initDexter();

    }

    private void initDexter() {
        Dexter.initialize(this);
    }

    public CityCatchedApplicationComponent getInjector() {
        return component;
    }

    public Boolean isDebugMode() {
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            return (pi.applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception nnfe) {
            nnfe.printStackTrace();
            return false;
        }
    }

    private void initFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

}
