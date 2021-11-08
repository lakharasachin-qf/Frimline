package com.app.frimline.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.common.HELPER;
import com.app.frimline.R;
import com.app.frimline.databinding.ItemCatRootTodaysLeftLayoutBinding;
import com.app.frimline.databinding.ItemCatRootTodaysRightLayoutBinding;
import com.app.frimline.models.categoryRootFragments.TodaysModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.google.gson.Gson;

import java.util.ArrayList;


public class TodaysTomorrowAdapter extends RecyclerView.Adapter {
    Activity activity;
    private final ArrayList<TodaysModel> dashBoardItemList;

    public TodaysTomorrowAdapter(ArrayList<TodaysModel> dashBoardItemList, Activity activity) {
        this.dashBoardItemList = dashBoardItemList;
        this.activity = activity;
        Gson gson = new Gson();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case LAYOUT_TYPE.LAYOUT_LEFT_BLOG:
                ItemCatRootTodaysLeftLayoutBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_cat_root_todays_left_layout, viewGroup, false);
                return new LeftBlog(layoutBinding);
            case LAYOUT_TYPE.LAYOUT_RIGHT_BLOG:
                ItemCatRootTodaysRightLayoutBinding twoLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_cat_root_todays_right_layout, viewGroup, false);
                return new RightBlog(twoLayoutBinding);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (dashBoardItemList.get(position).getLayoutType()) {
            case 0:
                return LAYOUT_TYPE.LAYOUT_LEFT_BLOG;
            case 1:
                return LAYOUT_TYPE.LAYOUT_RIGHT_BLOG;

            default:
                return -1;
        }

    }

    @Override
    public int getItemCount() {
        return dashBoardItemList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final TodaysModel model = dashBoardItemList.get(position);
        if (model != null) {
            switch (model.getLayoutType()) {
                case LAYOUT_TYPE.LAYOUT_LEFT_BLOG:
                    HELPER.LOAD_HTML(((LeftBlog) holder).binding.message, model.getMessage());
                    ((LeftBlog) holder).binding.message.setText(model.getMessage());

                    break;
                case LAYOUT_TYPE.LAYOUT_RIGHT_BLOG:
                    HELPER.LOAD_HTML(((RightBlog) holder).binding.message, model.getMessage());
                    break;

            }
        }
    }


    public static class LeftBlog extends RecyclerView.ViewHolder {
        ItemCatRootTodaysLeftLayoutBinding binding;

        public LeftBlog(ItemCatRootTodaysLeftLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }


    }


    public static class RightBlog extends RecyclerView.ViewHolder {
        ItemCatRootTodaysRightLayoutBinding binding;

        public RightBlog(ItemCatRootTodaysRightLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }

    }


}

