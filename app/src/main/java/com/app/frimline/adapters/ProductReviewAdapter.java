package com.app.frimline.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.common.CONSTANT;
import com.app.frimline.common.HELPER;
import com.app.frimline.R;
import com.app.frimline.databinding.ItemReviewLayoutBinding;
import com.app.frimline.models.categoryRootFragments.ReviewRootModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductReviewAdapter extends RecyclerView.Adapter<ProductReviewAdapter.ViewHolder> {
    private final ArrayList<ReviewRootModel.Review> frameItems;
    Activity activity;


    public ProductReviewAdapter(ArrayList<ReviewRootModel.Review> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReviewLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_review_layout, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ReviewRootModel.Review model = frameItems.get(position);
        if (CONSTANT.API_MODE) {
            Glide.with(activity)
                    .load(model.getUserAvatar())
                    .circleCrop()
                    .into(holder.binding.circleView);
            HELPER.LOAD_HTML(holder.binding.reviewTxt, model.getReview());
            HELPER.LOAD_HTML(holder.binding.userNameTxt, model.getReviewerName());

            holder.binding.reviewAddedData.setText("- " + HELPER.convertDate(model.getDate()));
            if (model.getRating().isEmpty()) {
                holder.binding.rate.setRating(0);
            } else {
                holder.binding.rate.setRating(Float.parseFloat(model.getRating()));
            }

        }
    }

    @Override
    public int getItemCount() {
        return frameItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemReviewLayoutBinding binding;

        public ViewHolder(@NonNull ItemReviewLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }


}
