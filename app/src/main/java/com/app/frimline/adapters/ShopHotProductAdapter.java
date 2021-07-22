package com.app.frimline.adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.models.OutCategoryModel;

import java.util.ArrayList;

public class ShopHotProductAdapter extends RecyclerView.Adapter<ShopHotProductAdapter.ViewHolder> {
    private final ArrayList<OutCategoryModel> frameItems;
    Activity activity;


    public ShopHotProductAdapter(ArrayList<OutCategoryModel> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shop_hot_product_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OutCategoryModel model = frameItems.get(position);
    }

    @Override
    public int getItemCount() {
        return frameItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ImageView imageView = itemView.findViewById(R.id.actionAddCart);
            imageView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getCategoryColor())));
        }
    }


}
