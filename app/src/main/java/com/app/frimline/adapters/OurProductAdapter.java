package com.app.frimline.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.R;
import com.app.frimline.models.OutCategoryModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class OurProductAdapter extends RecyclerView.Adapter<OurProductAdapter.ViewHolder> {
    private final ArrayList<OutCategoryModel> frameItems;
    Activity activity;


    public OurProductAdapter(ArrayList<OutCategoryModel> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_our_product_layout, parent, false);
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
        }
    }


}
