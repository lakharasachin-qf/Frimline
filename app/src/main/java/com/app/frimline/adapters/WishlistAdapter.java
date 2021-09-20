package com.app.frimline.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.ItemOrderHistoryLayoutBinding;
import com.app.frimline.models.OrderModel;
import com.app.frimline.models.roomModels.WishlistEntity;
import com.app.frimline.screens.OrderHistoryViewActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    private final ArrayList<WishlistEntity> frameItems;
    Activity activity;


    public WishlistAdapter(ArrayList<WishlistEntity> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderHistoryLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_order_history_layout, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final WishlistEntity model = frameItems.get(position);
        if (CONSTANT.API_MODE) {

        }
    }


    @Override
    public int getItemCount() {
        return frameItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemOrderHistoryLayoutBinding binding;

        public ViewHolder(@NonNull ItemOrderHistoryLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            PREF pref = new PREF(activity);
            binding.viewDetailsBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        }
    }


}
