package com.app.frimline.adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.models.OutCategoryModel;
import com.app.frimline.models.QAModel;

import java.util.ArrayList;

public class ProductQNAAdapter extends RecyclerView.Adapter<ProductQNAAdapter.ViewHolder> {
    private final ArrayList<QAModel> frameItems;
    Activity activity;

    private boolean isThemeColor = false;

    public void setThemeColor(boolean themeColor) {
        isThemeColor = themeColor;
    }

    public ProductQNAAdapter(ArrayList<QAModel> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_q_a_a_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final QAModel model = frameItems.get(position);
    }

    @Override
    public int getItemCount() {
        return frameItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TextView view = itemView.findViewById(R.id.qLogo);
            if (isThemeColor) {
                view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getThemeColor())));
            } else {
                view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getCategoryColor())));
            }

        }
    }


}
