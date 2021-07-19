package com.app.frimline.screens;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import com.app.frimline.R;
import com.app.frimline.adapters.CategoryNavViewPager;
import com.app.frimline.views.CustomViewPager;
import com.app.frimline.views.navigationDrawer.DrawerMenu;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;

public class CategoryLandingActivity extends BaseNavDrawerActivity {

    CustomViewPager customViewPager;
    DrawerLayout drawerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpToolbar();
        setStatusBarTransparent();

        CategoryNavViewPager adapter = new CategoryNavViewPager(this, getSupportFragmentManager(), 3);

        customViewPager = findViewById(R.id.viewPager);
        customViewPager.setAdapter(adapter);
        customViewPager.setOffscreenPageLimit(3);
        customViewPager.setCurrentItem(1);
        drawerLayout = findViewById(R.id.drawer_layout);
        customViewPager.addOnPageChangeListener(new androidx.viewpager.widget.ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("onPageScrolled", String.valueOf(position));
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("onPageSelected", String.valueOf(position));
                CurrentPosition = position;
                // launch();
                launch();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (getIntent().hasExtra("targetCategory")){
            shimmer_view_container = findViewById(R.id.shimmer_view_container);
            drawerLayout.setVisibility(View.VISIBLE);
            customViewPager.setVisibility(View.GONE);
            Toolbar toolbar_Navigation = findViewById(R.id.toolbar_Navigation);
            toolbar_Navigation.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onBackPressed() {
        if (shimmer_view_container!=null)
            shimmer_view_container.stopShimmer();

        drawerMenu.onBackPressed();
    }

    DrawerMenu drawerMenu;

    private void setUpToolbar() {
        FrameLayout frameLayout = findViewById(R.id.content_frame);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_category_landing, null, false);
        frameLayout.addView(activityView);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(CategoryLandingActivity.this);

        int defaultLoad=DrawerMenu.HOME_FRAGMENT;
        if (getIntent().hasExtra("fragment")) {
            if (getIntent().getStringExtra("fragment").equalsIgnoreCase("Home")) {
                defaultLoad = DrawerMenu.HOME_FRAGMENT;
            }
            if (getIntent().getStringExtra("fragment").equalsIgnoreCase("Shop")) {
                defaultLoad = DrawerMenu.SHOP_FRAGMENT;
            }
            if (getIntent().getStringExtra("fragment").equalsIgnoreCase("Checkout")) {
                defaultLoad = DrawerMenu.HOME_FRAGMENT;
            }
        }


        drawerMenu = new DrawerMenu(act,defaultLoad);
        drawerMenu.prepareMenuData();
        drawerMenu.populateExpandableList();



        Toolbar toolbar_Navigation = findViewById(R.id.toolbar_Navigation);
        toolbar_Navigation.setVisibility(View.GONE);
        toolbar_Navigation.setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigationView.setElevation(30);
        float radius = getResources().getDimension(R.dimen.roundcorner);
         MaterialShapeDrawable navViewBackground = (MaterialShapeDrawable) navigationView.getBackground();
        navViewBackground.setShapeAppearanceModel(
                navViewBackground.getShapeAppearanceModel()
                        .toBuilder()
                        .setTopRightCorner(CornerFamily.ROUNDED,radius)
                        .setBottomRightCorner(CornerFamily.ROUNDED,radius)
                        .build());
        hangeTheme(act,pref.getCategoryColor());
    }

    public void hangeTheme(Activity act, String primaryColor){
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

    public void setStatusBarTransparent() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }

    Handler handler;
    Runnable r;
    private int CurrentPosition;

    public void launch() {
        handler = new Handler();
        if (r != null)
            handler.removeCallbacks(r);

        r = new Runnable() {
            public void run() {

                if (CurrentPosition == 2) {

                    drawerMenu.performCategoryProfile();
                    drawerLayout.setVisibility(View.VISIBLE);
                    customViewPager.setVisibility(View.GONE);
                    setStatusBarTransparent();
                } else if (CurrentPosition == 0) {
                    customViewPager.setPagingEnabled(false);
                    drawerLayout.setVisibility(View.VISIBLE);
                    customViewPager.setVisibility(View.GONE);
                    Toolbar toolbar_Navigation = findViewById(R.id.toolbar_Navigation);
                    toolbar_Navigation.setVisibility(View.VISIBLE);
                    startAnimation();
                }
            }
        };

        handler.postDelayed(r, 200);
    }
    ShimmerFrameLayout shimmer_view_container;
    private void startAnimation() {
        if (!isDestroyed()) {
            shimmer_view_container = findViewById(R.id.shimmer_view_container);
            shimmer_view_container.startShimmer();
            shimmer_view_container.setVisibility(View.VISIBLE);
            handler = new Handler();
            if (r != null)
                handler.removeCallbacks(r);

            r = new Runnable() {
                public void run() {

                    if (CurrentPosition == 0) {
                        customViewPager.setPagingEnabled(false);
                        drawerLayout.setVisibility(View.VISIBLE);
                        shimmer_view_container.setVisibility(View.GONE);
                        Toolbar toolbar_Navigation = findViewById(R.id.toolbar_Navigation);
                        toolbar_Navigation.setVisibility(View.VISIBLE);
                        startAnimation();
                    } else if (CurrentPosition == 2) {

                    }
                }
            };

            handler.postDelayed(r, 200);
        }
    }
}