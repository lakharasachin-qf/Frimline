package com.app.frimline.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityCheckoutAddressBinding;

public class CheckoutAddressActivity extends BaseActivity {
    private ActivityCheckoutAddressBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act,R.layout.activity_checkout_address);
        changeStatusBarColor(ContextCompat.getColor(act,R.color.colorScreenBackground));
        binding.toolbarNavigation.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS_ANIM(act);
            }
        });

        binding.toolbarNavigation.title.setText("Checkout");

        binding.shipToDiffCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.otherInfo.setVisibility(View.VISIBLE);
                }else{
                    binding.otherInfo.setVisibility(View.GONE);
                }
            }
        });
        binding.includeBtn.button.setText("Proceed");
        changeTheme();
    }

    public void changeTheme(){
        HELPER.SET_STYLE(act, binding.nameEdt, binding.nameEdtLayout);
        HELPER.SET_STYLE(act, binding.lnameEdt, binding.lnameEdtLayout);
        HELPER.SET_STYLE(act, binding.companyEdt, binding.companyEdtLayout);
        HELPER.SET_STYLE(act, binding.countryEdt, binding.countryEdtLayout);
        HELPER.SET_STYLE(act, binding.streetEdt, binding.streetEdtLayout);
        HELPER.SET_STYLE(act, binding.cityEdt, binding.cityEdtLayout);
        HELPER.SET_STYLE(act, binding.postalCodeEdt, binding.postalCodeEdtLayout);
        HELPER.SET_STYLE(act, binding.phoneNoEdt, binding.phoneNoEdtLayout);
        HELPER.SET_STYLE(act, binding.emailEdt, binding.emailEdtLayout);
        binding.otherInfo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    binding.otherNoteEdtLayout.setBackground(ContextCompat.getDrawable(act, R.drawable.shape_editext_background_active));
                    GradientDrawable drawable = (GradientDrawable)binding.otherNoteEdtLayout.getBackground();
                    drawable.setStroke(2, Color.parseColor(new PREF(act).getCategoryColor()));
                } else {
                    binding.otherNoteEdtLayout.setBackground(ContextCompat.getDrawable(act, R.drawable.shape_editext_background));
                }
            }
        });
        binding.shipToDiffCheck.setButtonTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        binding.includeBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
    }
}