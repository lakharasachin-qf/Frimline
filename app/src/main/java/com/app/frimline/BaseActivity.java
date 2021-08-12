package com.app.frimline;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.FRIMLINE;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.NetworkChangeReceiver;
import com.app.frimline.Common.PREF;
import com.app.frimline.databaseHelper.CartRoomDatabase;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class BaseActivity extends AppCompatActivity implements Observer {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private static Dialog noconnectionAlertDialog;
    private static View view;
    public Activity act;
    public PREF prefManager;
    public FRIMLINE frimline;
    private BroadcastReceiver mNetworkReceiver;
    public Gson gson;
    public boolean PROTOTYPE_MODE = CONSTANT.PROTOTYPING_MODE;
    public static final boolean API_MODE = CONSTANT.API_MODE;
    public CartRoomDatabase db;

    public BaseActivity() {
    }

    private static void showNoConnectionDialog() {
        if (!noconnectionAlertDialog.isShowing()) {

            noconnectionAlertDialog.setContentView(R.layout.dialog_no_internet_connection);
            noconnectionAlertDialog.setCancelable(false);
            noconnectionAlertDialog.show();
        }
    }

    public static void InternetError(boolean value) {
        if (value) {
            if (noconnectionAlertDialog.isShowing()) {
                noconnectionAlertDialog.dismiss();
            }
        } else {
            showNoConnectionDialog();

        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        db = CartRoomDatabase.getAppDatabase(this);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        prefManager = new PREF(this);

        gson = new Gson();
        frimline = (FRIMLINE) this.getApplication();
        frimline.getObserver().addObserver(this);
        view = getLayoutInflater().inflate(R.layout.dialog_no_internet_connection, null);
        AppCompatButton appCompatButton = view.findViewById(R.id.button);
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
//                startActivity(getIntent());
            }
        });
        noconnectionAlertDialog = new Dialog(this, R.style.MyAlertDialogStyle_extend);
        noconnectionAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mNetworkReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();


    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected Map<String, String> getHeader() {
        Map<String, String> headers = new HashMap<>();
        if (prefManager.isLogin())
            headers.put("Authorization", "Bearer " + prefManager.getToken());
        return headers;
    }

    private void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void update(Observable observable, Object data) {

    }

    public void setStatusBarTransparent() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }

    public void changeBottomNavigationColor(int color) {
        getWindow().setNavigationBarColor(color);
    }

    public void changeStatusBarColor(int color) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(color);
    }

    protected void makeStatusBarSemiTranspenret(Toolbar toolbar) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        toolbar.setPadding(0, HELPER.getStatusBarHeight(act), 0, 0);
    }
}
