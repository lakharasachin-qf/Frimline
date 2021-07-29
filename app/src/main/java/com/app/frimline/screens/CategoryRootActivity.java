package com.app.frimline.screens;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.app.frimline.BaseNavDrawerActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.R;
import com.app.frimline.views.navigationDrawer.DrawerMenu;
import com.app.frimline.views.navigationDrawer.DrawerMenuForRoot;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;

public class CategoryRootActivity extends BaseNavDrawerActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref.setConfiguration("#EF7F1A", "#EF7F1A");
        setUpToolbar();
        makeStatusBarSemiTranspenret();

    }


    DrawerMenuForRoot drawerMenu;


    @Override
    public void onBackPressed() {
        drawerMenu.onBackPressedForCategoryRoot();
    }


    private void setUpToolbar() {
        FrameLayout frameLayout = findViewById(R.id.content_frame);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_category_root, null, false);
        frameLayout.addView(activityView);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(CategoryRootActivity.this);
        drawerMenu = new DrawerMenuForRoot(act, DrawerMenu.CATEGORY_ROOT_FRAGMENT);
        drawerMenu.setDefaultFragment(DrawerMenu.CATEGORY_ROOT_FRAGMENT);
        drawerMenu.prepareMenuData();
        drawerMenu.populateExpandableList();


        navigationView.setElevation(30);
        float radius = getResources().getDimension(R.dimen.roundcorner);
        MaterialShapeDrawable navViewBackground = (MaterialShapeDrawable) navigationView.getBackground();
        navViewBackground.setShapeAppearanceModel(
                navViewBackground.getShapeAppearanceModel()
                        .toBuilder()
                        .setTopRightCorner(CornerFamily.ROUNDED, radius)
                        .setBottomRightCorner(CornerFamily.ROUNDED, radius)
                        .build());

        DrawerLayout drawerLayout;
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setVisibility(View.VISIBLE);
        Toolbar toolbar_Navigation = findViewById(R.id.toolbar_Navigation);
        toolbar_Navigation.setVisibility(View.VISIBLE);

        ImageView logo = findViewById(R.id.logo);
        HELPER.changeTheme(act, pref.getCategoryColor());
        RelativeLayout cartBackgroundLayar = findViewById(R.id.cartBackgroundLayar);
        RelativeLayout cartBackgroundLayar2 = findViewById(R.id.cartBackgroundLayar2);
        cartBackgroundLayar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
        cartBackgroundLayar2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));

        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) logo.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        layoutParams.leftMargin = 0;
        layoutParams.topMargin = 0;
        logo.setLayoutParams(layoutParams);


        //code apply only in tablet mode
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            RelativeLayout relativeLayout = findViewById(R.id.HomePageLayout);
            relativeLayout.setGravity(Gravity.NO_GRAVITY);
        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(act, drawerLayout, toolbar_Navigation, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer_menu_white);

        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(null);

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer_menu_white); //set your own
        toolbar_Navigation.setNavigationIcon(null);
        ImageView drawerIcon = act.findViewById(R.id.drawerIcon);
        drawerIcon.setVisibility(View.GONE);
    }

}