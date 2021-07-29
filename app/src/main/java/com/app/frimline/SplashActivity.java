package com.app.frimline;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.app.frimline.databinding.ActivitySplashBinding;
import com.app.frimline.screens.BillingAddressActivity;
import com.app.frimline.screens.CategoryRootActivity;
import com.app.frimline.screens.CheckoutAddressActivity;
import com.app.frimline.screens.ProductDetailActivity;

public class SplashActivity extends BaseActivity {
    private AnimatorSet animatorSet1;
    private ActivitySplashBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.blue));
        }


        final ObjectAnimator scaleAnimatiorXX = ObjectAnimator.ofFloat(binding.logo, "scaleX", 0, 1f);
        ObjectAnimator scaleAnimatiorYX = ObjectAnimator.ofFloat(binding.logo, "scaleY", 0, 1f);
        animatorSet1 = new AnimatorSet();
        animatorSet1.playTogether(scaleAnimatiorXX, scaleAnimatiorYX);
        animatorSet1.setDuration(3000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(act, CategoryRootActivity.class);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                finish();
            }
        }, 1000);

    }


}