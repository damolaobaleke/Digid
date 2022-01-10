package com.softroniiks.digid;

import android.app.Application;

import com.microblink.MicroblinkSDK;

public class Digid extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        //blink id --> License expired
        MicroblinkSDK.setLicenseFile("license.key", this);

        //blink card
        com.microblink.blinkcard.MicroblinkSDK.setLicenseFile("blinkcardlicense.key", this);


    }
}
