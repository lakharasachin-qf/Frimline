package com.app.frimline.screens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.app.frimline.BaseActivity;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityProductDetailBinding;

public class ProductDetailActivity extends BaseActivity {



    private boolean redShoeVisible = true;
    private ActivityProductDetailBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_product_detail);

        setStatusBarTransparent();

        ViewTreeObserver viewTreeObserver = binding.scrollView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //
            }
        });

    }



}