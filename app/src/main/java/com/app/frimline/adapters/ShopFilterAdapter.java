package com.app.frimline.adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.models.CategoryRootFragments.CategorySingleModel;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class ShopFilterAdapter extends RecyclerView.Adapter<ShopFilterAdapter.ViewHolder> {
    private final ArrayList<CategorySingleModel> frameItems;

    Activity activity;


    public ShopFilterAdapter(ArrayList<CategorySingleModel> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shop_filter_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (CONSTANT.API_MODE){
            holder.chip.setText(frameItems.get(position).getCategoryName());
            holder.chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (frameItems.get(position).isActive()){
                        frameItems.get(position).setActive(false);
                        holder.chip.setTextColor(ContextCompat.getColor(activity, R.color.colorToolbarHeader));
                        holder.chip.setChipBackgroundColor(ColorStateList.valueOf(Color.WHITE));
                        holder.chip.setChipStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(activity,R.color.cardViewBorder)));
                    }else {
                        frameItems.get(position).setActive(true);
                        holder.chip.setTextColor(Color.WHITE);
                        holder.chip.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getThemeColor())));
                        holder.chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getThemeColor())));
                    }
//                    if (holder.chip.getTag().toString().equalsIgnoreCase("Y")) {
//                        holder.chip.setTag("N");
//                        holder.chip.setTextColor(ContextCompat.getColor(activity, R.color.colorToolbarHeader));
//                        holder.chip.setChipBackgroundColor(ColorStateList.valueOf(Color.WHITE));
//                        holder.chip.setChipStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(activity,R.color.cardViewBorder)));
//                    } else {
//                        holder.chip.setTag("Y");
//                        holder.chip.setTextColor(Color.WHITE);
//                        holder.chip.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getThemeColor())));
//                        holder.chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getThemeColor())));
//                    }

                }
            });
        }else {
            holder.chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.chip.getTag().toString().equalsIgnoreCase("Y")) {
                        holder.chip.setTag("N");
                        holder.chip.setTextColor(ContextCompat.getColor(activity, R.color.colorToolbarHeader));
                        holder.chip.setChipBackgroundColor(ColorStateList.valueOf(Color.WHITE));
                        holder.chip.setChipStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.cardViewBorder)));
                    } else {
                        holder.chip.setTag("Y");
                        holder.chip.setTextColor(Color.WHITE);
                        holder.chip.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getThemeColor())));
                        holder.chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getThemeColor())));
                    }

                }
            });
        }

    }




    @Override
    public int getItemCount() {
        return frameItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.chip);
            chip.setChipStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(activity,R.color.cardViewBorder)));
        }
    }


}
