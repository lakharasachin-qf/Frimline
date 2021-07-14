package com.app.frimline.screens;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.app.frimline.BaseActivity;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivitySearchBinding;

public class SearchActivity extends BaseActivity {
    private ActivitySearchBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_search);
        setStatusBarTransparent();

        binding.nameEdtLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.nameEdt.requestFocus();

            }
        });
    }
}