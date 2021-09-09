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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.app.frimline.BaseNavDrawerActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.ObserverActionID;
import com.app.frimline.Common.PREF;
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

import java.util.Observable;

public class CategoryLandingActivity extends BaseNavDrawerActivity {

    CustomViewPager customViewPager;
    DrawerLayout drawerLayout;
    DrawerMenu drawerMenu;
    Handler handler;
    Runnable r;
    ShimmerFrameLayout shimmer_view_container;
    private int CurrentPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpToolbar();
        makeStatusBarSemiTranspenret();

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

        if (getIntent().hasExtra("targetCategory")) {
            shimmer_view_container = findViewById(R.id.shimmer_view_container);
            drawerLayout.setVisibility(View.VISIBLE);
            customViewPager.setVisibility(View.GONE);
            Toolbar toolbar_Navigation = findViewById(R.id.toolbar_Navigation);
            toolbar_Navigation.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public void onBackPressed() {
        if (shimmer_view_container != null)
            shimmer_view_container.stopShimmer();
        drawerMenu.onBackPressed();
    }

    private void setUpToolbar() {
        FrameLayout frameLayout = findViewById(R.id.content_frame);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_category_landing, null, false);
        frameLayout.addView(activityView);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(CategoryLandingActivity.this);

        int defaultLoad = DrawerMenu.HOME_FRAGMENT;
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
            if (getIntent().getStringExtra("fragment").equalsIgnoreCase("order")) {
                defaultLoad = DrawerMenu.ORDER_HISTORY;
            }
        }



        drawerMenu = new DrawerMenu(act, defaultLoad);
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
                        .setTopRightCorner(CornerFamily.ROUNDED, radius)
                        .setBottomRightCorner(CornerFamily.ROUNDED, radius)
                        .build());
        hangeTheme(act, pref.getThemeColor());
        ImageView logo = findViewById(R.id.logo);


    }

    public void hangeTheme(Activity act, String primaryColor) {
        ImageView logo = act.findViewById(R.id.logo);
        VectorChildFinder vector = new VectorChildFinder(act, R.drawable.ic_logo_green, logo);
        VectorDrawableCompat.VFullPath path1 = vector.findPathByName("background");
        path1.setFillColor(ContextCompat.getColor(act, R.color.orange));
        logo.invalidate();

        RelativeLayout cartBackgroundLayar = findViewById(R.id.cartBackgroundLayar);
        RelativeLayout cartBackgroundLayar2 = findViewById(R.id.cartBackgroundLayar2);
        cartBackgroundLayar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        cartBackgroundLayar2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        TextView userNameTxt = findViewById(R.id.userNameTxt);
        if (pref.isLogin()) {
            userNameTxt.setText(pref.getUser().getDisplayName());
        } else {
            userNameTxt.setText("Sign In");
        }
        HELPER.changeCartCounter(act);
    }

    public void setStatusBarTransparent() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }

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
                } else if (CurrentPosition == 0) {
                    customViewPager.setPagingEnabled(false);
                    drawerLayout.setVisibility(View.VISIBLE);
                    // changeStatusBarColor(ContextCompat.getColor(act, R.color.colorScreenBackground));
                    customViewPager.setVisibility(View.GONE);
                    Toolbar toolbar_Navigation = findViewById(R.id.toolbar_Navigation);
                    toolbar_Navigation.setVisibility(View.VISIBLE);
                    startAnimation();


                    //  changeStatusBarColor(ContextCompat.getColor(act, R.color.transparent));
                }
            }
        };

        handler.postDelayed(r, 200);
    }

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
                        makeStatusBarSemiTranspenret();

                    } else if (CurrentPosition == 2) {

                    }
                }
            };

            handler.postDelayed(r, 200);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (frimline.getObserver().getValue() == ObserverActionID.SLIDE_VIEW_RIGHT) {
                    customViewPager.setCurrentItem(2, true);
                }
                if (frimline.getObserver().getValue() == ObserverActionID.SLIDE_VIEW_LEFT) {
                    customViewPager.setCurrentItem(0, true);
                }
                if (frimline.getObserver().getValue() == ObserverActionID.CART_COUNTER_UPDATE) {
                    HELPER.changeCartCounter(act);
                }
                if (frimline.getObserver().getValue() == ObserverActionID.LOGOUT) {
                    TextView userNameTxt = findViewById(R.id.userNameTxt);
                    pref = new PREF(act);
                    if (pref.isLogin()) {
                        userNameTxt.setText(pref.getUser().getDisplayName());
                    } else {
                        userNameTxt.setText("Sign In");
                    }

                }
                if (frimline.getObserver().getValue() == ObserverActionID.LOGIN) {
                    TextView userNameTxt = findViewById(R.id.userNameTxt);
                    pref = new PREF(act);
                    if (pref.isLogin()) {
                        userNameTxt.setText(pref.getUser().getDisplayName());
                     } else {
                        userNameTxt.setText("Sign In");
                    }
                }

                if (frimline.getObserver().getValue() == ObserverActionID.ADD_MENU) {
                    pref = new PREF(act);
                    if (pref.isLogin()) {
                        drawerMenu.addLogoutBtn();
                    }
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        HELPER.changeCartCounter(act);
        TextView userNameTxt = findViewById(R.id.userNameTxt);
        if (pref.isLogin()) {
            userNameTxt.setText(pref.getUser().getDisplayName());
        } else {
            userNameTxt.setText("Sign In");
        }
    }
}