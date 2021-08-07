package com.app.frimline.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.ObserverActionID;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.ItemTopRattesProductLayoutBinding;
import com.app.frimline.models.HomeFragements.ProductModel;
import com.app.frimline.screens.ProductDetailActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

public class TopRattedProductAdapter extends RecyclerView.Adapter<TopRattedProductAdapter.ViewHolder> {
    private final ArrayList<ProductModel> frameItems;
    Activity activity;


    public TopRattedProductAdapter(ArrayList<ProductModel> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTopRattesProductLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_top_rattes_product_layout, parent, false);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_top_rattes_product_layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProductModel model = frameItems.get(position);
        if (CONSTANT.API_MODE) {
            String imageUrl = "";
            if (model.getProductImagesList().size() != 0) {
                imageUrl = model.getProductImagesList().get(0);
            }
            holder.binding.image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            Glide.with(activity)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_square_place_holder)
                    .error(R.drawable.ic_square_place_holder).into(holder.binding.image);

            holder.binding.productNameTxt.setText(model.getName());
            HELPER.LOAD_HTML(holder.binding.descriptionTxt, model.getDescription());
            holder.binding.price.setText(activity.getString(R.string.Rs) + model.getPrice());

            holder.binding.ExploreMoreTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, ProductDetailActivity.class);
                    i.putExtra("model", new Gson().toJson(model));
                    activity.startActivity(i);
                    activity.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
                }
            });

        } else {
            holder.binding.ExploreMoreTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, ProductDetailActivity.class);
                    activity.startActivity(i);
                    activity.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return frameItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemTopRattesProductLayoutBinding binding;

        public ViewHolder(ItemTopRattesProductLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.underLine.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getCategoryColor())));

            this.binding.ExploreMoreTxt.setTextColor(Color.parseColor(new PREF(activity).getCategoryColor()));


        }
    }


}
