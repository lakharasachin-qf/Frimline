package com.app.frimline.screens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.BaseNavDrawerActivity;
import com.app.frimline.Common.APIs;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.views.navigationDrawer.DrawerMenu;
import com.app.frimline.views.navigationDrawer.DrawerMenuForRoot;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CategoryRootActivity extends BaseNavDrawerActivity {

    private FrameLayout nav_host_fragment;
    private ProgressBar screenLoader;
    private Toolbar toolbar_Navigation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref.setConfiguration("#EF7F1A", "#EF7F1A");
        makeStatusBarSemiTranspenret();
        setUpToolbar();
        nav_host_fragment = findViewById(R.id.nav_host_fragment);
        screenLoader = findViewById(R.id.screenLoader);
        if (API_MODE) {
            nav_host_fragment.setVisibility(View.GONE);
            screenLoader.setVisibility(View.VISIBLE);
            screenLoader.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(act, R.color.orange)));
            toolbar_Navigation = findViewById(R.id.toolbar_Navigation);
            toolbar_Navigation.setVisibility(View.GONE);
            screenLoader.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(act, R.color.orange), android.graphics.PorterDuff.Mode.MULTIPLY);

            getThemeColor();

        } else {
            nav_host_fragment.setVisibility(View.VISIBLE);
            screenLoader.setVisibility(View.GONE);
            pref.setConfiguration("#EF7F1A", "#EF7F1A");
            ApplyTheme();
        }


    }


    DrawerMenuForRoot drawerMenu;


    @Override
    public void onBackPressed() {
        drawerMenu.onBackPressedForCategoryRoot();
    }


    private void setUpToolbar() {
        FrameLayout frameLayout = findViewById(R.id.content_frame);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View activityView = layoutInflater.inflate(R.layout.activity_category_root, null, false);
        frameLayout.addView(activityView);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(CategoryRootActivity.this);


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
        toolbar_Navigation = findViewById(R.id.toolbar_Navigation);
        toolbar_Navigation.setVisibility(View.VISIBLE);

        ImageView logo = findViewById(R.id.logo);
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

        ApplyTheme();
    }


    public void ApplyTheme() {
        ImageView logo = findViewById(R.id.logo);
        HELPER.changeTheme(act, pref.getCategoryColor());
        RelativeLayout cartBackgroundLayar = findViewById(R.id.cartBackgroundLayar);
        RelativeLayout cartBackgroundLayar2 = findViewById(R.id.cartBackgroundLayar2);
        cartBackgroundLayar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        cartBackgroundLayar2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));

        drawerMenu = new DrawerMenuForRoot(act, DrawerMenu.CATEGORY_ROOT_FRAGMENT);
        drawerMenu.setDefaultFragment(DrawerMenu.CATEGORY_ROOT_FRAGMENT);
        drawerMenu.prepareMenuData();
        drawerMenu.populateExpandableList();
        if (toolbar_Navigation.getVisibility() == View.GONE)
            toolbar_Navigation.setVisibility(View.VISIBLE);

    }

    private void getThemeColor() {
        //HELPER.showLoadingTran(act);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.THEME_COLOR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    JSONObject object = new JSONObject(response);

                    nav_host_fragment.setVisibility(View.VISIBLE);
                    screenLoader.setVisibility(View.GONE);
                    pref.setConfiguration(object.getString("theme_color"), "#EF7F1A");
                    ApplyTheme();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        LinearLayout NoDataFound = findViewById(R.id.NoDataFound);
                        AppCompatButton button = findViewById(R.id.button);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                NoDataFound.setVisibility(View.GONE);
                                nav_host_fragment.setVisibility(View.GONE);
                                screenLoader.setVisibility(View.VISIBLE);
                                screenLoader.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(act, R.color.orange)));
                                toolbar_Navigation = findViewById(R.id.toolbar_Navigation);
                                toolbar_Navigation.setVisibility(View.GONE);
                                screenLoader.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(act, R.color.orange), android.graphics.PorterDuff.Mode.MULTIPLY);
                                getThemeColor();
                            }
                        });
                        NoDataFound.setVisibility(View.VISIBLE);
                        nav_host_fragment.setVisibility(View.GONE);
                        screenLoader.setVisibility(View.GONE);
                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("Accept", "application/x-www-form-urlencoded");//application/json
//                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }


    public void nextFlow() {
        setUpToolbar();

    }
}