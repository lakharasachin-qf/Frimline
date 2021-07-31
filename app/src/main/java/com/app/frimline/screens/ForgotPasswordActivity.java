package com.app.frimline.screens;

import android.animation.LayoutTransition;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.Validators;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityMobileVerificationBinding;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

public class ForgotPasswordActivity extends BaseActivity {
    private ActivityMobileVerificationBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_mobile_verification);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ((ViewGroup) findViewById(R.id.containerLinear)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.backPress.getLayoutParams();
        layoutParams.topMargin = HELPER.getStatusBarHeight(act);
        binding.backPress.setLayoutParams(layoutParams);

        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        changeTheme();
    }

    public void changeTheme() {
        PREF pref = new PREF(act);
        binding.includeBtn.button.setText("Reset");
        binding.includeBtn.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PROTOTYPE_MODE) {
                    HELPER.SIMPLE_ROUTE(act, ResetPasswordActivity.class);
                } else {
//                    if (binding.phoneNoEdt.getText().toString().length() == 10) {
//                        HELPER.SIMPLE_ROUTE(act, OtpVerificationActivity.class);
//                        finish();
//                    } else if (binding.phoneNoEdt.getText().toString().trim().length() == 0) {
//                        binding.phoneNoEdt.requestFocus();
//                        binding.phoneNoEdtLayout.setError("Enter Mobile No.");
//                    } else {
//                        binding.phoneNoEdt.requestFocus();
//                        binding.phoneNoEdtLayout.setError("Enter Valid Mobile No.");
//                    }

                    if (Validators.Companion.isEmailValid(binding.phoneNoEdt.getText().toString())) {
                        HELPER.SIMPLE_ROUTE(act, ResetPasswordActivity.class);
                        finish();
                    } else if (binding.phoneNoEdt.getText().toString().trim().length() == 0) {
                        binding.phoneNoEdt.requestFocus();
                        binding.phoneNoEdtLayout.setError("Enter email id");
                    } else {
                        binding.phoneNoEdt.requestFocus();
                        binding.phoneNoEdtLayout.setError("Enter valid Email Id");
                    }
                }
            }
        });
        binding.includeBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));

        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.backgroundLayar2.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.otpIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.otpTitle.setTextColor((Color.parseColor(pref.getThemeColor())));

        VectorChildFinder vector = new VectorChildFinder(act, R.drawable.ic_mobile_password, binding.otpIcon);
        VectorDrawableCompat.VFullPath path1 = vector.findPathByName("colorGreen");
        path1.setFillColor(Color.parseColor(new PREF(act).getThemeColor()));
        path1 = vector.findPathByName("colorGreen1");
        path1.setFillColor(Color.parseColor(new PREF(act).getThemeColor()));

        HELPER.ERROR_HELPER_For_MOBILE_VALIDATION(binding.phoneNoEdt, binding.phoneNoEdtLayout);

    }
}