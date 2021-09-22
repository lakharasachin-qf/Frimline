package com.app.frimline.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.models.BlogModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.app.frimline.screens.BlogDetailsActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecentBlogViewAdapter extends PagerAdapter {

    private final Activity context;
    private final List<BlogModel> sliderImg;


    public RecentBlogViewAdapter(List<BlogModel> sliderImg, Activity context) {
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
        BlogModel model = sliderImg.get(position);
        LayoutInflater layoutInflater;
        if (model.getLayoutType() == LAYOUT_TYPE.LAYOUT_ONE_BLOG) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.item_blog_recent_layout_only_one, null);
            setTheme1(view);
            Chip chip1 = view.findViewById(R.id.chip1);
            chip1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CONSTANT.API_MODE) {
                        Intent i = new Intent(context, BlogDetailsActivity.class);
                        i.putExtra("model", new Gson().toJson(model.getBlogList().get(0)));
                        context.startActivity(i);
                        context.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
                        context.finish();
                    } else {
                        HELPER.SIMPLE_ROUTE(context, BlogDetailsActivity.class);
                    }
                }
            });

            TextView title = view.findViewById(R.id.productName);
            ImageView productImage = view.findViewById(R.id.productImage);
            TextView description = view.findViewById(R.id.description);
            if (CONSTANT.API_MODE) {
                HELPER.LOAD_HTML(title, model.getBlogList().get(0).getTitle());
                HELPER.LOAD_HTML(description, model.getBlogList().get(0).getShortContent());

                Glide.with(context).load(model.getBlogList().get(0).getBlogImage())
                        .placeholder(R.drawable.ic_square_place_holder)
                        .error(R.drawable.ic_square_place_holder)
                        .into(productImage);
            }

            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);
            return view;

        } else {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.item_blog_recent_layout, null);
            setTheme(view);
            Chip chip1 = view.findViewById(R.id.chip1);
            chip1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CONSTANT.API_MODE) {
                        Intent i = new Intent(context, BlogDetailsActivity.class);
                        i.putExtra("model", new Gson().toJson(model.getBlogList().get(0)));
                        context.startActivity(i);
                        context.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
                        context.finish();
                    } else {
                        HELPER.SIMPLE_ROUTE(context, BlogDetailsActivity.class);
                    }
                }
            });
            Chip chip2 = view.findViewById(R.id.chip2);
            chip2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CONSTANT.API_MODE) {
                        Intent i = new Intent(context, BlogDetailsActivity.class);
                        i.putExtra("model", new Gson().toJson(model.getBlogList().get(1)));
                        context.startActivity(i);
                        context.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
                        context.finish();
                    } else {
                        HELPER.SIMPLE_ROUTE(context, BlogDetailsActivity.class);
                    }
                }
            });

            TextView title = view.findViewById(R.id.productName);
            TextView title2 = view.findViewById(R.id.productName2);
            TextView description = view.findViewById(R.id.description);
            TextView description2 = view.findViewById(R.id.description2);

            ImageView productImage = view.findViewById(R.id.productImage);
            ImageView productImage2 = view.findViewById(R.id.productImage2);
            if (CONSTANT.API_MODE) {
                HELPER.LOAD_HTML(title, model.getBlogList().get(0).getTitle());
                HELPER.LOAD_HTML(description, model.getBlogList().get(0).getShortContent());
                HELPER.LOAD_HTML(title2, model.getBlogList().get(1).getTitle());
                HELPER.LOAD_HTML(description2, model.getBlogList().get(1).getShortContent());


                Glide.with(context).load(model.getBlogList().get(0).getBlogImage())
                        .placeholder(R.drawable.ic_square_place_holder)
                        .error(R.drawable.ic_square_place_holder)
                        .into(productImage);
                Glide.with(context).load(model.getBlogList().get(1).getBlogImage())
                        .placeholder(R.drawable.ic_square_place_holder)
                        .error(R.drawable.ic_square_place_holder)
                        .into(productImage2);
            }

            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);
            return view;

        }

    }

    public void setTheme1(View view) {
        Chip chip1 = view.findViewById(R.id.chip1);

        chip1.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(context).getThemeColor())));
        chip1.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(new PREF(context).getThemeColor())));

        View view3 = view.findViewById(R.id.view3);
        view3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(context).getThemeColor())));

    }

    public void setTheme(View view) {
        Chip chip1 = view.findViewById(R.id.chip1);
        Chip chip2 = view.findViewById(R.id.chip2);
        chip1.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(context).getThemeColor())));
        chip1.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(new PREF(context).getThemeColor())));
        chip2.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(context).getThemeColor())));
        chip2.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(new PREF(context).getThemeColor())));
        View view3 = view.findViewById(R.id.view3);
        View view2 = view.findViewById(R.id.view2);
        view3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(context).getThemeColor())));
        view2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(context).getThemeColor())));

    }
   /* @Override
    public float getPageWidth(int position) {
        return 0.5f;

    }*/

    @Override
    public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}