package com.app.frimline;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.frimline.screens.CategoryRootActivity;
import com.google.gson.Gson;

public class SplashActivity extends AppCompatActivity {
    private Activity act;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        Gson gson = new Gson();
        com.app.frimline.databinding.ActivitySplashBinding binding = DataBindingUtil.setContentView(act, R.layout.activity_splash);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        final ObjectAnimator scaleAnimatiorXX = ObjectAnimator.ofFloat(binding.logo, "scaleX", 0, 1f);
        ObjectAnimator scaleAnimatiorYX = ObjectAnimator.ofFloat(binding.logo, "scaleY", 0, 1f);
        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.playTogether(scaleAnimatiorXX, scaleAnimatiorYX);
        animatorSet1.setDuration(3000);



        new Handler().postDelayed(() -> {
            Intent intent = new Intent(act, CategoryRootActivity.class);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }, 1000);

    }


}