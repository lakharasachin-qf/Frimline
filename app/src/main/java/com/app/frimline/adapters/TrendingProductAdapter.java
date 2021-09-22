package com.app.frimline.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.R;
import com.app.frimline.models.HomeFragements.TradingStoriesModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TrendingProductAdapter extends RecyclerView.Adapter<TrendingProductAdapter.ViewHolder> {
    private final ArrayList<TradingStoriesModel> frameItems;
    Activity activity;


    public TrendingProductAdapter(ArrayList<TradingStoriesModel> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trending_product_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TradingStoriesModel model = frameItems.get(position);
        if (CONSTANT.API_MODE) {
            Glide.with(activity)
                    .load(model.getUrl())
                    .placeholder(R.drawable.ic_square_place_holder)
                    .error(R.drawable.ic_square_place_holder).into(holder.posterImage);
        }
    }

    @Override
    public int getItemCount() {
        return frameItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.image);
        }
    }


}
