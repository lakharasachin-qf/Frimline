package com.app.frimline.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    private static final int TYPE_WIFI = 1;
    private static final int TYPE_MOBILE = 2;

    public static boolean getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return true;

            return activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
        }
        return false;
    }

    public static int getStrConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        int TYPE_NOT_CONNECTED = -1;
        if (null != activeNetwork) {
            TYPE_NOT_CONNECTED = -1;
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        } else {
            TYPE_NOT_CONNECTED = 0;
        }

        return TYPE_NOT_CONNECTED;
    }
}
