package com.app.frimline.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.app.frimline.BaseActivity;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityCheckoutAddressBinding;

public class CheckoutAddressActivity extends BaseActivity {
    private ActivityCheckoutAddressBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act,R.layout.activity_checkout_address);
        setStatusBarTransparent();
        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
    }
}