package com.app.frimline.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.R;
import com.app.frimline.models.OutCategoryModel;

import java.util.ArrayList;

public class TopRattedProductAdapter extends RecyclerView.Adapter<TopRattedProductAdapter.ViewHolder> {
    private final ArrayList<OutCategoryModel> frameItems;
    Activity activity;


    public TopRattedProductAdapter(ArrayList<OutCategoryModel> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_top_rattes_product_layout, parent, false);
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
