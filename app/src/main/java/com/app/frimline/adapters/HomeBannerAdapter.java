package com.app.frimline.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.R;
import com.app.frimline.models.HomeFragements.BannerModel;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeBannerAdapter extends PagerAdapter {

    private final Activity context;
    private final List<BannerModel> sliderImg;


    public HomeBannerAdapter(List<BannerModel> sliderImg, Activity context) {
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

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_home_banner_child_layout, null);
        if (CONSTANT.API_MODE) {
            ImageView productImages = view.findViewById(R.id.banner);
            Glide.with(context).load(sliderImg.get(position).getUrl()).placeholder(R.drawable.ic_banner_place_holder).error(R.drawable.ic_banner_place_holder).into(productImages);
            //Glide.with(context).load(R.drawable.ic_banner_place_holder).placeholder(R.drawable.ic_banner_place_holder).error(R.drawable.ic_banner_place_holder).into(productImages);
        } else {

            LinearLayout dummyContainer = view.findViewById(R.id.dummyContainer);
            dummyContainer.setVisibility(View.VISIBLE);

        }
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }
//
//    @Override
//    public float getPageWidth(int position) {
//        return 0.5f;
//
//    }

    @Override
    public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}