package com.app.frimline.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.adapters.LoginTabAdapter;
import com.app.frimline.databinding.ActivitySignupBinding;
import com.app.frimline.fragments.LoginVEmailFragment;
import com.app.frimline.fragments.LoginVMobileFragment;
import com.app.frimline.fragments.SignupFragment;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.google.android.material.tabs.TabLayout;

public class SignupActivity extends BaseActivity {
    private ActivitySignupBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_signup);

        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 30) {
            setStatusBarTransparent();
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= 30) {
            HELPER.hideStatusBarAPI30(act);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        changeTheme();
    }

    public void changeTheme() {
        binding.includeBtn.button.setText("Sign Up");
        binding.includeBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        binding.tabContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        binding.backgroundLayar2.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        binding.signupTxt1.setTextColor((Color.parseColor(new PREF(act).getCategoryColor())));
        ImageView logo = act.findViewById(R.id.logo);
        VectorChildFinder vector = new VectorChildFinder(act, R.drawable.ic_logo_green, logo);
        VectorDrawableCompat.VFullPath path1 = vector.findPathByName("background");
        path1.setFillColor(Color.parseColor(new PREF(act).getCategoryColor()));
        logo.invalidate();
        HELPER.SET_STYLE(act,binding.usernameEdt,binding.usernameLayout);
        HELPER.SET_STYLE(act,binding.emailEdt,binding.emailEdtLayout);
        HELPER.SET_STYLE(act,binding.mobileNoEdt,binding.mobileNoEdtLayout);
        HELPER.SET_STYLE(act,binding.confirmPassword,binding.confirmPasswordLayout);

    }

}