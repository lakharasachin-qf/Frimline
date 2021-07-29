package com.app.frimline.screens;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityAddressesBinding;
import com.app.frimline.databinding.IncludeActivityToolbarLayoutBinding;

public class AddressesActivity extends BaseActivity {
    private ActivityAddressesBinding binding;
    private IncludeActivityToolbarLayoutBinding toolbarBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_addresses);
        //setStatusBarTransparent();

        makeStatusBarSemiTranspenret(binding.toolbar.toolbar);

        binding.toolbar.title.setText("Addresses");
        binding.toolbar.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS_ANIM(act);
            }
        });

        changeTheme();
    }

    public void changeTheme() {
        binding.actionAdd.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.actionEdit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));

        binding.actionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(act, BillingAddressActivity.class);
            }
        });
        binding.actionEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(act, BillingAddressActivity.class);
            }
        });

    }
}