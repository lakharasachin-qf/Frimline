package com.app.frimline.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.ItemLeftBlogLayoutBinding;
import com.app.frimline.databinding.ItemRightBlogLayoutBinding;
import com.app.frimline.models.BlogModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.app.frimline.screens.BlogDetailsActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.ArrayList;


public class BlogsAdapter extends RecyclerView.Adapter {
    private ArrayList<BlogModel> dashBoardItemList;
    private final Gson gson;
    Activity activity;

    public BlogsAdapter(ArrayList<BlogModel> dashBoardItemList, Activity activity) {
        this.dashBoardItemList = dashBoardItemList;
        this.activity = activity;
        gson = new Gson();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case LAYOUT_TYPE.LAYOUT_LEFT_BLOG:
                ItemLeftBlogLayoutBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_left_blog_layout, viewGroup, false);
                return new LeftBlog(layoutBinding);
            case LAYOUT_TYPE.LAYOUT_RIGHT_BLOG:
                ItemRightBlogLayoutBinding twoLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_right_blog_layout, viewGroup, false);
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
        final BlogModel model = dashBoardItemList.get(position);
        if (model != null) {
            switch (model.getLayoutType()) {
                case LAYOUT_TYPE.LAYOUT_LEFT_BLOG:
                    if (CONSTANT.API_MODE) {
                        HELPER.LOAD_HTML(((LeftBlog) holder).binding.description, model.getShortContent());
                        HELPER.LOAD_HTML(((LeftBlog) holder).binding.title, model.getTitle());
                        Glide.with(activity).load(model.getBlogImage())
                                .placeholder(R.drawable.ic_square_place_holder)
                                .error(R.drawable.ic_square_place_holder)
                                .into(((LeftBlog) holder).binding.blogImage);
                    }
                    ((LeftBlog) holder).binding.layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent i = new Intent(activity, BlogDetailsActivity.class);
                            i.putExtra("model", gson.toJson(model));
                            activity.startActivity(i);
                            activity.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);

                        }
                    });
                    ((LeftBlog) holder).binding.exploreMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ((LeftBlog) holder).binding.layout.performClick();
                        }
                    });

                    break;
                case LAYOUT_TYPE.LAYOUT_RIGHT_BLOG:
                    if (CONSTANT.API_MODE) {
                        HELPER.LOAD_HTML(((RightBlog) holder).binding.description, model.getShortContent());
                        HELPER.LOAD_HTML(((RightBlog) holder).binding.title, model.getTitle());
                        Glide.with(activity).load(model.getBlogImage())
                                .placeholder(R.drawable.ic_square_place_holder)
                                .error(R.drawable.ic_square_place_holder)
                                .into(((RightBlog) holder).binding.blogImage);
                    }

                    ((RightBlog) holder).binding.layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(activity, BlogDetailsActivity.class);
                            i.putExtra("model", gson.toJson(model));
                            activity.startActivity(i);
                            activity.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
                        }
                    });
                    ((RightBlog) holder).binding.exploreMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((RightBlog) holder).binding.layout.performClick();
                        }
                    });

                    break;

            }
        }
    }


    public class LeftBlog extends RecyclerView.ViewHolder {
        ItemLeftBlogLayoutBinding binding;

        public LeftBlog(ItemLeftBlogLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            changeColor();
        }

        public void changeColor() {
            PREF pref = new PREF(activity);
            binding.view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
            binding.exploreMore.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
            binding.exploreMore.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        }
    }


    public class RightBlog extends RecyclerView.ViewHolder {
        ItemRightBlogLayoutBinding binding;

        public RightBlog(ItemRightBlogLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            changeColor();
        }

        public void changeColor() {
            PREF pref = new PREF(activity);
            binding.view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
            binding.exploreMore.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
            binding.exploreMore.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));

        }
    }


}

