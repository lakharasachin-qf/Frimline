package com.app.frimline.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends BaseActivity {
    private ActivityEditProfileBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act,R.layout.activity_edit_profile);
        setStatusBarTransparent();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorGreen));
        }
        binding.toolbar.title.setText("Edit Profile");
        binding.toolbar.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS_ANIM(act);
            }
        });
    }
}