package com.app.frimline.screens;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityBillingAddressBinding;

public class BillingAddressActivity extends BaseActivity {
    private ActivityBillingAddressBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_billing_address);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorScreenBackground));
        }


        binding.toolbarNavigation.title.setText("Checkout");
        binding.toolbarNavigation.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS_ANIM(act);
            }
        });
        binding.includeBtn.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(act, CheckoutAddressActivity.class);

            }
        });
        binding.includeBtn.button.setText("Next");
        changeTheme();
    }

    private void changeTheme() {
        PREF pref = new PREF(act);

        HELPER.SET_STYLE(act, binding.nameEdt, binding.nameEdtLayout);
        HELPER.SET_STYLE(act, binding.lnameEdt, binding.lnameEdtLayout);
        HELPER.SET_STYLE(act, binding.companyEdt, binding.companyEdtLayout);
        HELPER.SET_STYLE(act, binding.countryEdt, binding.countryEdtLayout);
        HELPER.SET_STYLE(act, binding.streetEdt, binding.streetEdtLayout);
        HELPER.SET_STYLE(act, binding.cityEdt, binding.cityEdtLayout);
        HELPER.SET_STYLE(act, binding.postalCodeEdt, binding.postalCodeEdtLayout);
        HELPER.SET_STYLE(act, binding.phoneNoEdt, binding.phoneNoEdtLayout);
        HELPER.SET_STYLE(act, binding.emailEdt, binding.emailEdtLayout);

        binding.includeBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));

    }
}