package com.app.frimline.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.R;
import com.app.frimline.models.OrderedProductModel;
import com.app.frimline.screens.OrderProductDetailActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OrderImageSliderAdpater extends PagerAdapter {

    private Activity context;
    private LayoutInflater layoutInflater;
    private ArrayList<OrderedProductModel> sliderImg;


    public OrderImageSliderAdpater(ArrayList<OrderedProductModel> sliderImg, Activity context) {
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
        View view = layoutInflater.inflate(R.layout.item_order_product_layout, null);
        ImageView productImages = view.findViewById(R.id.productImage);
        LinearLayout itemLayout = view.findViewById(R.id.itemLayout);
        if (CONSTANT.API_MODE) {
            Glide.with(context)
                    .load(sliderImg.get(position).getProductImage())
                    .placeholder(R.drawable.ic_square_place_holder)
                    .error(R.drawable.ic_square_place_holder)
                    .into(productImages);

            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, OrderProductDetailActivity.class);
                    i.putExtra("themeColor","themeColor");
                    i.putExtra("productId", sliderImg.get(position).getProductId());
                    context.startActivity(i);
                    context.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
                }
            });

            TextView productName = view.findViewById(R.id.productName);
            TextView qty = view.findViewById(R.id.qty);
            HELPER.LOAD_HTML(productName, sliderImg.get(position).getName());
            HELPER.LOAD_HTML(qty, "Quantity : " + sliderImg.get(position).getQty());
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