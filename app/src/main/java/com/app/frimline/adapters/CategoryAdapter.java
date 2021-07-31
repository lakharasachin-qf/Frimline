package com.app.frimline.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.models.CategoryRootFragments.CategorySingleModel;
import com.app.frimline.screens.CategoryLandingActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private final ArrayList<CategorySingleModel> frameItems;
    Activity activity;
    private PREF pref;

    public CategoryAdapter(ArrayList<CategorySingleModel> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
        pref = new PREF(activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cat_root_category_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CategorySingleModel model = frameItems.get(position);
        holder.textView.setText(model.getCategoryName());
        Glide.with(activity)
                .load(model.getDetailImage())
                .circleCrop()
                .into(holder.product);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PREF(activity).setConfiguration(pref.getThemeColor(), model.getCatColor());
                Intent i = new Intent(activity, CategoryLandingActivity.class);
                i.putExtra("model",new Gson().toJson(model));
                activity.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return frameItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView product;
        LinearLayout item;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            product = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.title);

        }
    }


}
