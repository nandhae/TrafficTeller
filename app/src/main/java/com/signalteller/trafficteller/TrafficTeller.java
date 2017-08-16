package com.signalteller.trafficteller;

import android.app.Application;

import com.firebase.client.Firebase;


public class TrafficTeller extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
