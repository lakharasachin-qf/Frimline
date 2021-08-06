package com.app.frimline.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.models.OutCategoryModel;
import com.app.frimline.screens.BlogDetailsActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.chip.Chip;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProductImageSliderAdpater extends PagerAdapter {

    private Activity context;
    private LayoutInflater layoutInflater;
    private ArrayList<String> sliderImg;


    public ProductImageSliderAdpater(ArrayList<String> sliderImg, Activity context) {
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
        View view = layoutInflater.inflate(R.layout.item_product_image_layout, null);
        ImageView productImages = view.findViewById(R.id.productImage);
        if (CONSTANT.API_MODE){
            Glide.with(context)
                    .load(sliderImg.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.ic_square_place_holder)
                    .error(R.drawable.ic_square_place_holder)
                    .into(productImages);

        }
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }


    @Override
    public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}