package com.app.frimline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.ImageLoader;


import com.app.frimline.R;
import com.app.frimline.models.OutCategoryModel;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecentBlogViewAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<OutCategoryModel> sliderImg;



    public RecentBlogViewAdapter(List<OutCategoryModel> sliderImg, Context context) {
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

        OutCategoryModel utils = sliderImg.get(position);



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