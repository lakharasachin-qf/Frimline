package com.app.frimline.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityBlogDetailsBinding;

public class BlogDetailsActivity extends BaseActivity {
    private ActivityBlogDetailsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_blog_details);
        setStatusBarTransparent();

        binding.toolbar.title.setText("Blog");
        binding.toolbar.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS_ANIM(act);
            }
        });

    }
}