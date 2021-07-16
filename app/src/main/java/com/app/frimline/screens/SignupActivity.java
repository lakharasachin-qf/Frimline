package com.app.frimline.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.app.frimline.BaseActivity;
import com.app.frimline.R;
import com.app.frimline.adapters.LoginTabAdapter;
import com.app.frimline.databinding.ActivitySignupBinding;
import com.app.frimline.fragments.LoginVEmailFragment;
import com.app.frimline.fragments.LoginVMobileFragment;
import com.app.frimline.fragments.SignupFragment;
import com.google.android.material.tabs.TabLayout;

public class SignupActivity extends BaseActivity {
    private ActivitySignupBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_signup);
        setStatusBarTransparent();


    }

}