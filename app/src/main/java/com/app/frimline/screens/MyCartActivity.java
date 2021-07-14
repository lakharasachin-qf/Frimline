package com.app.frimline.screens;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityMyCartBinding;

public class MyCartActivity extends BaseActivity {
    private ActivityMyCartBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        setStatusBarTransparent();

        binding = DataBindingUtil.setContentView(act,R.layout.activity_my_cart);

        binding.boottomFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(act, CheckoutAddressActivity.class);
            }
        });
    }
}