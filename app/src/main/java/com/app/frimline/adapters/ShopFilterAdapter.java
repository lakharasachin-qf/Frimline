package com.app.frimline.adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.models.OutCategoryModel;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class ShopFilterAdapter extends RecyclerView.Adapter<ShopFilterAdapter.ViewHolder> {
    private final ArrayList<String> frameItems;
    Activity activity;


    public ShopFilterAdapter(ArrayList<String> frameItems, Activity activity) {
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
        holder.chip.setText(frameItems.get(position));
    }
    @Override
    public int getItemCount() {
        return frameItems.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chip=itemView.findViewById(R.id.chip);
            chip.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getCategoryColor())));
        }
    }


}
