package com.app.frimline.screens;

import android.os.Bundle;

import com.app.frimline.BaseActivity;
import com.app.frimline.R;

public class OtpVerificationActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        setStatusBarTransparent();
    }
}