package com.app.frimline.screens;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.frimline.BaseActivity;
import com.app.frimline.R;
import com.app.frimline.adapters.LoginTabAdapter;
import com.app.frimline.databinding.ActivityLoginBinding;
import com.app.frimline.fragments.LoginVMobileFragment;
import com.app.frimline.fragments.LoginVEmailFragment;
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
        setStatusBarTransparent();

        setupTabIcons();
    }


    private void setupTabIcons() {
        loginFragment = new LoginVMobileFragment();
        vEmailFragment = new LoginVEmailFragment();

        LoginTabAdapter adapter = new LoginTabAdapter(getSupportFragmentManager());
        adapter.addFragment(loginFragment, "Sign In");
        adapter.addFragment(vEmailFragment, "Sign In");

        binding.viewPager.setAdapter(adapter);
        binding.tab.setupWithViewPager(binding.viewPager);
        binding.viewPager.setOffscreenPageLimit(3);

        //Determine indicator width at runtime
        binding.tab.post(new Runnable() {
            @Override
            public void run() {


                indicatorWidth = binding.tab.getWidth() / binding.tab.getTabCount();

                //Assign new width
                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) binding.indicator.getLayoutParams();
                indicatorParams.width = indicatorWidth;
                binding.indicator.setLayoutParams(indicatorParams);
               // binding.helper.getLayoutParams().height=binding.indicator.getLayoutParams().height;
            }
        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


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

}