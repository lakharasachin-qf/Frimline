package com.app.frimline.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.app.frimline.R;
import com.app.frimline.models.OutCategoryModel;
import com.google.gson.Gson;

import java.util.List;

public class SlideOffersBannerrAdapter extends RecyclerView.Adapter<SlideOffersBannerrAdapter.SliderViewHolder> {
    private List<OutCategoryModel> sliderItems;
    private ViewPager2 viewPager2;
    Activity activity;


    Gson gson;

    public SlideOffersBannerrAdapter(List<OutCategoryModel> sliderItems, Activity activity) {
        this.sliderItems = sliderItems;
        this.activity = activity;

        gson = new Gson();

    }

    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trending_product_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setLayout();

    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder {
        private TextView priceForPay;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        void setLayout() {


        }
    }
}
