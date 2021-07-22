package com.app.frimline.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.adapters.RecentBlogViewAdapter;
import com.app.frimline.databinding.ActivityBlogDetailsBinding;
import com.app.frimline.models.OutCategoryModel;

import java.util.ArrayList;
import java.util.List;

public class BlogDetailsActivity extends BaseActivity {
    private ActivityBlogDetailsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_blog_details);
        //setStatusBarTransparent();
        changeStatusBarColor(ContextCompat.getColor(act, R.color.colorScreenBackground));
        binding.toolbar.title.setText("Blog");
        binding.toolbar.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS_ANIM(act);
            }
        });

        List<OutCategoryModel> outCategoryModels =new ArrayList<>();
        outCategoryModels.add(new OutCategoryModel());
        outCategoryModels.add(new OutCategoryModel());
        outCategoryModels.add(new OutCategoryModel());
        outCategoryModels.add(new OutCategoryModel());
        RecentBlogViewAdapter sliderAdapter = new RecentBlogViewAdapter(outCategoryModels, act);
        binding.viewPager.setAdapter(sliderAdapter);
        binding.dotsIndicator.setViewPager(binding.viewPager);

        changeTheme();
    }

    public void changeTheme() {
        binding.view1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        binding.view2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        binding.view3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        binding.view4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));

        binding.chip1.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        binding.chip1.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
      //  binding.chipC.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
       // binding.chipC.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));

        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
    }
}