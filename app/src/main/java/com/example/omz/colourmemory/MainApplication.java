package com.example.omz.colourmemory;

import android.app.Application;
import android.util.Log;

import io.realm.Realm;

/**
 * Created by Omz on 15/5/2017.
 */

public class MainApplication extends Application {

    public static final String TAG = "MainApplication";
    private Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }
}
