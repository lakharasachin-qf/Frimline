package com.app.frimline.screens;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
        pref.setConfiguration("#EF7F1A","#EF7F1A");
        setUpToolbar();
        setStatusBarTransparent();
        changeStatusBarColor(this.getResources().getColor(R.color.colorScreenBackground));
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

        ImageView logo=findViewById(R.id.logo);
        HELPER.changeTheme(act,pref.getCategoryColor());
        TextView cartBackgroundLayar = findViewById(R.id.cartBackgroundLayar);
        TextView cartBackgroundLayar2 = findViewById(R.id.cartBackgroundLayar2);
        cartBackgroundLayar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
        cartBackgroundLayar2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
    }

}