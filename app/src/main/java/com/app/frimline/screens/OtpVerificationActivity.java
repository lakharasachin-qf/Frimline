package com.app.frimline.screens;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityOtpVerificationBinding;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;



public class OtpVerificationActivity extends BaseActivity {
    private ActivityOtpVerificationBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_otp_verification);

        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 30) {
            setStatusBarTransparent();
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= 30) {
            HELPER.hideStatusBarAPI30(act);
        }

        changeTheme();

    }

    public void changeTheme() {
        PREF pref = new PREF(act);



        binding.includeBtn.button.setText("Verify & Proceed");
        binding.includeBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));

        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        binding.backgroundLayar2.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        binding.otpIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        binding.otpTitle.setTextColor((Color.parseColor(pref.getCategoryColor())));

        VectorChildFinder vector = new VectorChildFinder(act, R.drawable.ic_mobile_password, binding.otpIcon);
        VectorDrawableCompat.VFullPath path1 = vector.findPathByName("colorGreen");
        path1.setFillColor(Color.parseColor(new PREF(act).getCategoryColor()));
        path1 = vector.findPathByName("colorGreen1");
        path1.setFillColor(Color.parseColor(new PREF(act).getCategoryColor()));

        changeStyle();
    }

    public void changeStyle() {
        // The attributes you want retrieved
        int[] attrs = {R.attr.otp_box_background_active};
        TypedArray ta = obtainStyledAttributes(R.style.otpViewStyleForLarge, attrs);
        String text = ta.getString(0);

        // Fetching the colors defined in your style
        Drawable textColor = ta.getDrawable(0);
        textColor.setTint(Color.RED);
        textColor = ContextCompat.getDrawable(act, R.drawable.shape_otp_focued);
        ta.recycle();

    }
}