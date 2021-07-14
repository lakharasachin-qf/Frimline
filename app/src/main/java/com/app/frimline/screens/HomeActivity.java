package com.app.frimline.screens;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.app.frimline.BaseNavDrawerActivity;
import com.app.frimline.R;
import com.app.frimline.views.navigationDrawer.DrawerMenu;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends BaseNavDrawerActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpToolbar();
        setTheme(R.style.Theme_Frimline_GREENSHADE);

    }



    @Override
    public void onBackPressed() {
        drawerMenu.onBackPressed();
    }

    DrawerMenu drawerMenu;
    private void setUpToolbar() {
        FrameLayout frameLayout = findViewById(R.id.content_frame);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_home, null, false);
        frameLayout.addView(activityView);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(HomeActivity.this);
        drawerMenu = new DrawerMenu(HomeActivity.this,DrawerMenu.HOME_FRAGMENT);
        drawerMenu.prepareMenuData();
        drawerMenu.populateExpandableList();
    }

}