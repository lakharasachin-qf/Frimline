package com.app.frimline.screens;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.adapters.LoginTabAdapter;
import com.app.frimline.databinding.ActivityLoginBinding;
import com.app.frimline.fragments.LoginVEmailFragment;
import com.app.frimline.fragments.LoginVMobileFragment;
import com.app.frimline.views.WrapContentHeightViewPager;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding binding;
    private int indicatorWidth;
    private LoginVMobileFragment loginFragment;
    private LoginVEmailFragment vEmailFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_login);

        changeTheme();
        setupTabIcons();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) binding.backPress.getLayoutParams();
        layoutParams.topMargin = HELPER.getStatusBarHeight(act);
        binding.backPress.setLayoutParams(layoutParams);

//
//        if (Build.VERSION.SDK_INT >= 30) {
//            Log.e("Android ", "11");
//            getWindow().getDecorView().getWindowInsetsController().hide(android.view.WindowInsets.Type.statusBars());
//
//        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }


    private void setupTabIcons() {
        loginFragment = new LoginVMobileFragment();
        vEmailFragment = new LoginVEmailFragment();
        WrapContentHeightViewPager wrapContentHeightViewPager = findViewById(R.id.viewPager);
        LoginTabAdapter adapter = new LoginTabAdapter(getSupportFragmentManager());
        adapter.addFragment(loginFragment, "Sign In");
        adapter.addFragment(vEmailFragment, "Sign In");

        wrapContentHeightViewPager.setAdapter(adapter);
        binding.tab.setupWithViewPager(wrapContentHeightViewPager);
        wrapContentHeightViewPager.setOffscreenPageLimit(3);
        wrapContentHeightViewPager.setNestedScrollingEnabled(false);
        //Determine indicator width at runtime
        binding.tab.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = binding.tab.getWidth() / binding.tab.getTabCount();
                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) binding.indicator.getLayoutParams();
                indicatorParams.width = indicatorWidth;
                binding.indicator.setLayoutParams(indicatorParams);
                // binding.helper.getLayoutParams().height=binding.indicator.getLayoutParams().height;
            }
        });

        wrapContentHeightViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.indicator.getLayoutParams();
                float translationOffset = (positionOffset + i) * indicatorWidth;
                params.leftMargin = (int) translationOffset;
                binding.indicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                // Log.e("PAge", i + "d");
            }
        });

        binding.tab.getTabAt(0).setIcon(ContextCompat.getDrawable(act, R.drawable.ic_tab_mobile_indicator));
        binding.tab.getTabAt(1).setIcon(ContextCompat.getDrawable(act, R.drawable.ic_tab_indicator_email));

        binding.tab.getTabAt(0).getIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        binding.tab.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.colorToolbarHeader), PorterDuff.Mode.SRC_IN);

        // setupTabIcons();
        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {

                    case 0:
                        binding.tab.getTabAt(0).getIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
                        binding.tab.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.colorToolbarHeader), PorterDuff.Mode.SRC_IN);

                        break;
                    case 1:
                        binding.tab.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorToolbarHeader), PorterDuff.Mode.SRC_IN);
                        binding.tab.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                        break;
                }
                binding.tab.invalidate();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void changeTheme() {
        binding.indicator.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.backgroundLayar2.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.signupTxtTXT.setTextColor((Color.parseColor(new PREF(act).getThemeColor())));
        ImageView logo = act.findViewById(R.id.logo);
        VectorChildFinder vector = new VectorChildFinder(act, R.drawable.ic_logo_green, logo);
        VectorDrawableCompat.VFullPath path1 = vector.findPathByName("background");
        path1.setFillColor(Color.parseColor(new PREF(act).getThemeColor()));
        logo.invalidate();

        binding.signupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(act, SignupActivity.class);
            }
        });

    }

}