package com.app.frimline.screens;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityBillingAddressBinding;

public class BillingAddressActivity extends BaseActivity {
    private ActivityBillingAddressBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_billing_address);
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = this.getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//              window.setStatusBarColor(this.getResources().getColor(R.color.colorGreen));
//        }


        HELPER.SET_STYLE(act,binding.nameEdt,binding.nameEdtLayout);
        HELPER.SET_STYLE(act,binding.lnameEdt,binding.lnameEdtLayout);

    }
}