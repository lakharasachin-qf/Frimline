package com.app.frimline.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.R;

public class MobileVerificationActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);
        setStatusBarTransparent();
        RelativeLayout getOtpBtn = findViewById(R.id.getOtpBtn);
        getOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(act,OtpVerificationActivity.class);

            }
        });
    }
}