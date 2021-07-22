package com.app.frimline.screens;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityMyCartBinding;

public class MyCartActivity extends BaseActivity {
    private ActivityMyCartBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        changeBottomNavigationColor(ContextCompat.getColor(act,R.color.bottomNavbar));
        changeStatusBarColor(ContextCompat.getColor(act,R.color.colorScreenBackground));
        binding = DataBindingUtil.setContentView(act,R.layout.activity_my_cart);

        binding.boottomFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(act, BillingAddressActivity.class);
            }
        });
        binding.toolbarNavigation.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS_ANIM(act);
            }
        });

        binding.toolbarNavigation.title.setText("Cart");
        changeColor();
    }

    public void changeColor(){
        binding.applyCode.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        binding.bottomSlider.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        binding.boottomText.setTextColor((Color.parseColor(new PREF(act).getCategoryColor())));

    }
}