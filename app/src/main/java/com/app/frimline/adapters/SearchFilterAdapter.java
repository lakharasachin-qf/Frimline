package com.app.frimline.adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.FRIMLINE;
import com.app.frimline.Common.ObserverActionID;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.models.CategoryRootFragments.CategorySingleModel;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SearchFilterAdapter extends RecyclerView.Adapter<SearchFilterAdapter.ViewHolder> {
    private final ArrayList<CategorySingleModel> frameItems;

    Activity activity;
    CategorySingleModel selectedCategory;

    public SearchFilterAdapter(ArrayList<CategorySingleModel> frameItems, Activity activity) {
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
        if (CONSTANT.API_MODE) {
            holder.chip.setText(frameItems.get(position).getCategoryName());
            holder.chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (frameItems.get(position).isActive()) {
                        frameItems.get(position).setActive(false);
                        holder.chip.setTextColor(ContextCompat.getColor(activity, R.color.colorToolbarHeader));
                        holder.chip.setChipBackgroundColor(ColorStateList.valueOf(Color.WHITE));
                        FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.CATEGORY_FILTER_REMOVE, new Gson().toJson(frameItems.get(position)));
                        holder.chip.setChipStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.cardViewBorder)));
                    } else {
                        frameItems.get(position).setActive(true);
                        FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.CATEGORY_FILTER, new Gson().toJson(frameItems.get(position)));
                        holder.chip.setTextColor(Color.WHITE);
                        holder.chip.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getThemeColor())));
                        holder.chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getThemeColor())));
                    }
                }
            });
            Log.e("Selected", new Gson().toJson(selectedCategory));
            Log.e("frameItems", new Gson().toJson(frameItems.get(position)));
            if (selectedCategory != null) {

                if (selectedCategory.getCategoryId().equalsIgnoreCase(frameItems.get(position).getCategoryId())) {
                    Log.e("Selected", new Gson().toJson(selectedCategory));
                    Log.e("frameItems", new Gson().toJson(frameItems.get(position)));
                    frameItems.get(position).setActive(true);
                    // FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.CATEGORY_FILTER, new Gson().toJson(frameItems.get(position)));
                    holder.chip.setTextColor(Color.WHITE);
                    holder.chip.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getThemeColor())));
                    holder.chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getThemeColor())));
                }
            }
        } else {
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

    public void setCategoryFilter(CategorySingleModel selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.chip);
            chip.setChipStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.cardViewBorder)));
        }
    }


}
