package com.app.frimline;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.app.frimline.views.navigationDrawer.DrawerMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;

public class CategoryRootActivity extends BaseNavDrawerActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpToolbar();
        setStatusBarTransparent();

    }

    DrawerMenu drawerMenu;


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
        drawerMenu = new DrawerMenu(act, DrawerMenu.CATEGORY_ROOT_FRAGMENT);
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
    }

}