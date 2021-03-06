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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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

import java.io.Console;
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
    public static final boolean PROTOTYPE= CONSTANT.PROTOTYPING_MODE;
    public static final boolean API_MODE= CONSTANT.API_MODE;
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        setContentView(R.layout.custom_navigation);


        pref = new PREF(this);
        gson = new Gson();
        frimline = (FRIMLINE) this.getApplication();
        frimline.getObserver().addObserver(this);

        cartRoomDatabase = CartRoomDatabase.getAppDatabase(this);
        actt =this;
        noconnectionAlertDialog = new Dialog(this, R.style.MyAlertDialogStyle_extend);
        view = getLayoutInflater().inflate(R.layout.dialog_no_internet_connection, null);
        AppCompatButton appCompatButton = view.findViewById(R.id.button);
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (noconnectionAlertDialog.isShowing()) {
//                    noconnectionAlertDialog.dismiss();
//                }
            }
        });
        noconnectionAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mNetworkReceiver = new NetworkChangeReceiver2();
        registerNetworkBroadcastForNougat();


        toolbar = findViewById(R.id.toolbar_Navigation);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(act, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        // toggle.setHomeAsUpIndicator(R.drawable.ic_drawer_menu); //set your own

        //toggle.syncState();

        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toggle.setDrawerIndicatorEnabled(false);
//        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer_menu);
//        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                if (drawer.isDrawerOpen(GravityCompat.START)) {
//                    drawer.closeDrawer(GravityCompat.START);
//                } else {
//                    drawer.openDrawer(GravityCompat.START);
//                }
//            }
//        });


        ImageView drawerIcon = act.findViewById(R.id.drawerIcon);
        ImageView drawerIcon2 = act.findViewById(R.id.drawerIcon2);
        drawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        drawerIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });


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

//        if (prefManager.getUserToken() != null) {
//            headers.put("Authorization", "Bearer" + prefManager.getUserToken());
//        }
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
