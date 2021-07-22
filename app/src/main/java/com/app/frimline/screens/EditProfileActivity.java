package com.app.frimline.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
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
import com.app.frimline.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends BaseActivity {
    private ActivityEditProfileBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act,R.layout.activity_edit_profile);
        //setStatusBarTransparent();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorScreenBackground));
        }
        binding.toolbar.title.setText("Edit Profile");
        binding.toolbar.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS_ANIM(act);
            }
        });
        changeTheme();
    }

    public void changeTheme(){
        binding.includeBtn.button.setText("Save Profile");
        HELPER.SET_STYLE(act, binding.nameEdt, binding.nameEdtLayout);
        HELPER.SET_STYLE(act, binding.lnameEdt, binding.lnameEdtLayout);
        HELPER.SET_STYLE(act, binding.displayNameEdt, binding.displayNameEdtLayout);
        HELPER.SET_STYLE(act, binding.phoneNoEdt, binding.phoneNoEdtLayout);
        HELPER.SET_STYLE(act, binding.emailEdt, binding.emailEdtLayout);

        binding.newPasswordEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    binding.newPasswordLayout.setBackground(ContextCompat.getDrawable(act, R.drawable.shape_editext_background_active));
                    GradientDrawable drawable = (GradientDrawable)binding.newPasswordLayout.getBackground();
                    drawable.setStroke(2, Color.parseColor(new PREF(act).getCategoryColor()));
                } else {
                    binding.newPasswordLayout.setBackground(ContextCompat.getDrawable(act, R.drawable.shape_editext_background));
                }
            }
        });

        binding.confirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    binding.confirmPasswordLayout.setBackground(ContextCompat.getDrawable(act, R.drawable.shape_editext_background_active));
                    GradientDrawable drawable = (GradientDrawable)binding.confirmPasswordLayout.getBackground();
                    drawable.setStroke(2, Color.parseColor(new PREF(act).getCategoryColor()));
                } else {
                    binding.confirmPasswordLayout.setBackground(ContextCompat.getDrawable(act, R.drawable.shape_editext_background));
                }
            }
        });
        binding.includeBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
    }
}