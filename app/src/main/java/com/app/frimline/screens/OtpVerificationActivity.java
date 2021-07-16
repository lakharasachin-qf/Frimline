package com.app.frimline.screens;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.app.frimline.BaseActivity;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityOtpVerificationBinding;

public class OtpVerificationActivity extends BaseActivity {
        private ActivityOtpVerificationBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act,R.layout.activity_otp_verification);
        setStatusBarTransparent();

    }
}