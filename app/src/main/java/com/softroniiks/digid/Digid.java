package com.softroniiks.digid;

import android.app.Application;

import com.microblink.MicroblinkSDK;

public class Digid extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MicroblinkSDK.setLicenseFile("license.key", this);
        //MicroblinkSDK.setLicenseFile("blinkcardlicense.key", this);
    }
}
