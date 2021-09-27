package com.app.frimline;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.FRIMLINE;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.NetworkChangeReceiver2;
import com.app.frimline.Common.PREF;
import com.app.frimline.databaseHelper.CartRoomDatabase;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class BaseNavDrawerActivity extends AppCompatActivity implements Observer, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = BaseNavDrawerActivity.class.getSimpleName();
    private static Dialog noconnectionAlertDialog;
    private static Activity actt;
    private static View view;
    public Activity act;
    public PREF pref;
    public FRIMLINE frimline;
    private BroadcastReceiver mNetworkReceiver;
    public Gson gson;
    Toolbar toolbar;
    public static final boolean PROTOTYPE = CONSTANT.PROTOTYPING_MODE;
    public static final boolean API_MODE = CONSTANT.API_MODE;
    public CartRoomDatabase cartRoomDatabase;

    public BaseNavDrawerActivity() {
    }

    private static void showNoConnectionDialog() {
        if (!noconnectionAlertDialog.isShowing()) {
            if (!actt.isDestroyed() && !actt.isFinishing()) {
                noconnectionAlertDialog.setContentView(view);
                noconnectionAlertDialog.setCancelable(false);
                noconnectionAlertDialog.show();
            }
        }
    }

    public static void InternetError(boolean value) {
        if (value) {
            if (!actt.isDestroyed() && !actt.isFinishing()) {
                if (noconnectionAlertDialog.isShowing()) {
                    noconnectionAlertDialog.dismiss();
                }
            }
        } else {
            showNoConnectionDialog();

        }
    }

    public DrawerLayout drawer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        setContentView(R.layout.custom_navigation);


        pref = new PREF(this);
        gson = new Gson();
        frimline = (FRIMLINE) this.getApplication();
        frimline.getObserver().addObserver(this);

        cartRoomDatabase = CartRoomDatabase.getAppDatabase(this);
        actt = this;
        noconnectionAlertDialog = new Dialog(this, R.style.MyAlertDialogStyle_extend);
        view = getLayoutInflater().inflate(R.layout.dialog_no_internet_connection, null);
        AppCompatButton appCompatButton = view.findViewById(R.id.button);
        appCompatButton.setOnClickListener(v -> { });


        noconnectionAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mNetworkReceiver = new NetworkChangeReceiver2();
        registerNetworkBroadcastForNougat();


        toolbar = act.findViewById(R.id.toolbar_Navigation);
        setSupportActionBar(toolbar);
        drawer = act.findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(act, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);


        ImageView drawerIcon = act.findViewById(R.id.drawerIcon);
        ImageView drawerIcon2 = act.findViewById(R.id.drawerIcon2);
        drawerIcon.setOnClickListener(v -> {
            DrawerLayout drawer1 = findViewById(R.id.drawer_layout);
            if (drawer1.isDrawerOpen(GravityCompat.START)) {
                drawer1.closeDrawer(GravityCompat.START);
            } else {
                drawer1.openDrawer(GravityCompat.START);
            }
        });
        drawerIcon2.setOnClickListener(v -> {
            DrawerLayout drawer12 = findViewById(R.id.drawer_layout);
            if (drawer12.isDrawerOpen(GravityCompat.START)) {
                drawer12.closeDrawer(GravityCompat.START);
            } else {
                drawer12.openDrawer(GravityCompat.START);
            }
        });
        setLockDrawer(true);

    }

    public void setLockDrawer(boolean lock) {
        if (lock) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
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
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    protected void makeStatusBarSemiTranspenret() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Toolbar toolbar = act.findViewById(R.id.toolbar_Navigation);
        toolbar.setPadding(0, HELPER.getStatusBarHeight(act), 0, 0);
    }
}
