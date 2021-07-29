package com.app.frimline.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.models.OutCategoryModel;
import com.app.frimline.screens.BlogDetailsActivity;
import com.google.android.material.chip.Chip;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecentBlogViewAdapter extends PagerAdapter {

    private Activity context;
    private LayoutInflater layoutInflater;
    private List<OutCategoryModel> sliderImg;


    public RecentBlogViewAdapter(List<OutCategoryModel> sliderImg, Activity context) {
        this.sliderImg = sliderImg;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderImg.size();
    }

    @Override
    public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(@NotNull ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_blog_recent_layout, null);
        Chip chip1 = view.findViewById(R.id.chip1);
        chip1.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(context).getThemeColor())));
        chip1.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(new PREF(context).getThemeColor())));
        View view3 = view.findViewById(R.id.view3);
        view3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(context).getThemeColor())));


        chip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(context, BlogDetailsActivity.class);
                context.finish();
            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public float getPageWidth(int position) {
        return 0.5f;

    }

    @Override
    public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}