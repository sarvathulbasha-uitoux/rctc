package com.google.firebase.example.fireeats.Utils;

import android.support.multidex.MultiDexApplication;

import com.google.firebase.example.fireeats.recievers.ConnectivityReceiver;


/**
 * Created by UITOUX10 on 09/02/17.
 * Modified by UITOUX10 on 11/04/17.
 */

public class MyApplication extends MultiDexApplication {
    private static MyApplication mInstance;

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
