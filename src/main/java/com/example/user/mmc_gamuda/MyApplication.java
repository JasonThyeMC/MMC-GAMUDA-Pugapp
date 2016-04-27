package com.example.user.mmc_gamuda;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;


import com.firebase.client.Firebase;

/**
 * Created by user on 9/22/15.
 */
public class MyApplication extends Application{

    public static final String TAG = MyApplication.class.getSimpleName();

    SharedPreferences pref;

    private static MyApplication ourInstance;

    public static synchronized MyApplication getInstance() {

        return ourInstance;
    }

    public MyApplication() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        pref = getSharedPreferences("MMC-Gamuda-Pugapp", Context.MODE_PRIVATE);
        if(!pref.getBoolean("logInExist", false)){
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("logInExist",true);
            editor.putBoolean("authState", false);
            editor.commit();
        }
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        Firebase ref = new Firebase("https://sizzling-fire-1548.firebaseio.com/Ring");
        //ref.keepSynced(false);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public boolean getLoginState(){

        return pref.getBoolean("authState", false);

    }




}
