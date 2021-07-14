package com.app.frimline.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.frimline.BaseActivity;
import com.app.frimline.R;

public class BlogDetailsActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);
        setStatusBarTransparent();

    }
}