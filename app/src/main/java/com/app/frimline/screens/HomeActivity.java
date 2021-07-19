package com.app.frimline.screens;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.frimline.BaseNavDrawerActivity;
import com.app.frimline.R;
import com.app.frimline.views.navigationDrawer.DrawerMenu;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

public class HomeActivity extends BaseNavDrawerActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpToolbar();
        //setTheme(R.style.Theme_Frimline_GREENSHADE);

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
        changeTheme(act,pref.getCategoryColor());
    }

    public void changeTheme(Activity act, String primaryColor){
        ImageView logo=act.findViewById(R.id.logo);
        VectorChildFinder vector = new VectorChildFinder(act, R.drawable.ic_logo_green, logo);
        VectorDrawableCompat.VFullPath path1 = vector.findPathByName("background");
        path1.setFillColor(Color.parseColor(primaryColor));
        logo.invalidate();

        TextView cartBackgroundLayar = findViewById(R.id.cartBackgroundLayar);
        TextView cartBackgroundLayar2 = findViewById(R.id.cartBackgroundLayar2);
        cartBackgroundLayar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        cartBackgroundLayar2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
    }
}