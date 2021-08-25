package com.app.frimline.screens;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.Validators;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityResetPasswordBinding;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;


public class ResetPasswordActivity extends BaseActivity {
    private ActivityResetPasswordBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_reset_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        changeTheme();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.backPress.getLayoutParams();
        layoutParams.topMargin = HELPER.getStatusBarHeight(act);

        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    public void changeTheme() {
        PREF pref = new PREF(act);


        binding.includeBtn.button.setText("Reset");
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


        binding.includeBtn.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PROTOTYPE_MODE) {
                    Intent i = new Intent(act, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    if (!validations()) {
                        Intent i = new Intent(act, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                }

            }
        });

        HELPER.ERROR_HELPER(binding.newPasswordEdt, binding.newPasswordLayout);
        HELPER.ERROR_HELPER(binding.confirmPassword, binding.confirmPasswordLayout);

        HELPER.FOCUS_HELPER(binding.scrollView, binding.newPasswordEdt, binding.newPasswordLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.confirmPassword, binding.confirmPasswordLayout);

        binding.confirmPasswordLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.newPasswordLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));

    }

    public boolean validations() {
        boolean isError = false;
        boolean isFocus = false;

        if (binding.newPasswordEdt.getText().toString().trim().length() == 0) {
            isError = true;
            binding.newPasswordLayout.setError("Enter Password");
            if (!isFocus) {
                isFocus = true;
                binding.newPasswordEdt.requestFocus();
            }
        }
        if (!Validators.Companion.isValidPassword(binding.newPasswordEdt.getText().toString())) {
            isError = true;
            binding.newPasswordLayout.setError("Password should be at least 12 characters, at least one uppercase and one lowercase letter, one number and one special character.");
            if (!isFocus) {
                isFocus = true;
                binding.newPasswordEdt.requestFocus();
            }
        }

        if (binding.confirmPassword.getText().toString().length() == 0) {
            isError = true;
            binding.confirmPasswordLayout.setError("Enter Confirm Password");
            if (!isFocus) {
                isFocus = true;
                binding.confirmPassword.requestFocus();
            }
        }
        if (!binding.newPasswordEdt.getText().toString().equals(binding.confirmPassword.getText().toString())) {
            isError = true;
            binding.confirmPasswordLayout.setError("Password doest not match.");
            if (!isFocus) {
                isFocus = true;
                binding.confirmPassword.requestFocus();
            }
        }


        return isError;
    }

}