package com.app.frimline.Common;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FRIMLINE extends MultiDexApplication {
    private static FRIMLINE sInstance;
    private AppObserver observer;
    private RequestQueue mRequestQueue;


    public static FRIMLINE getsInstance() {
        return sInstance;
    }


    public synchronized static FRIMLINE getInstance() {
        return sInstance;
    }

    @SuppressLint("HardwareIds")
    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        MultiDex.install(this);

        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        ApplicationLifeCycle.init(sInstance);

        observer = new AppObserver(getApplicationContext());
        mRequestQueue = Volley.newRequestQueue(sInstance);

        printHashKey();
    }

    private void printHashKey() {
        // Add code to print out the key hash
        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {

        }
    }


    public AppObserver getObserver() {
        return observer;
    }

}