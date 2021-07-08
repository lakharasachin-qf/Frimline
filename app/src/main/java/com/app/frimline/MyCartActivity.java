package com.app.frimline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MyCartActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        setStatusBarTransparent();
    }
}