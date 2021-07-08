package com.app.frimline.screens.cart;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.app.frimline.BaseActivity;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityBillingAddressBinding;

public class BillingAddressActivity extends BaseActivity {
    private ActivityBillingAddressBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_billing_address);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
              window.setStatusBarColor(this.getResources().getColor(R.color.colorGreen));
        }
        EditText nameEdt =findViewById(R.id.nameEdt);
        LinearLayout nameEdtLayout =findViewById(R.id.nameEdtLayout);
        nameEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    nameEdtLayout.setBackground(ContextCompat.getDrawable(BillingAddressActivity.this,R.drawable.shape_editext_background_active));
                }else{
                    nameEdtLayout.setBackground(ContextCompat.getDrawable(BillingAddressActivity.this,R.drawable.shape_editext_background));
                }
            }
        });

        binding.lnameEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                binding.lnameEdtLayout.setBackground(ContextCompat.getDrawable(BillingAddressActivity.this,R.drawable.shape_editext_background_active));
                }else{
                    binding.lnameEdtLayout.setBackground(ContextCompat.getDrawable(BillingAddressActivity.this,R.drawable.shape_editext_background));
                }
            }
        });
    }
}