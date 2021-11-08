package com.app.frimline.adapters;

import android.annotation.SuppressLint;
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

import com.app.frimline.common.CONSTANT;
import com.app.frimline.common.PREF;
import com.app.frimline.R;
import com.app.frimline.models.categoryRootFragments.CategorySingleModel;
import com.app.frimline.screens.CategoryLandingActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

public class OurProductAdapter extends RecyclerView.Adapter<OurProductAdapter.ViewHolder> {
    private final ArrayList<CategorySingleModel> frameItems;
    Activity activity;
    PREF pref;

    public OurProductAdapter(ArrayList<CategorySingleModel> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
        pref = new PREF(activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_our_product_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final CategorySingleModel model = frameItems.get(position);
        if (CONSTANT.API_MODE) {
            holder.textView.setText(model.getCategoryName());
            holder.product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new PREF(activity).setConfiguration(pref.getThemeColor(), model.getCatColor());
                    Intent i = new Intent(activity, CategoryLandingActivity.class);
                    i.putExtra("targetCategory", "yes");
                    i.putExtra("fragment", "Home");
                    i.putExtra("model", new Gson().toJson(model));

                    activity.startActivity(i);
                    activity.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
                    activity.finish();
                }
            });

            Glide.with(activity)
                    .load(model.getImage())
                    .circleCrop()
                    .into(holder.image);
        } else {
            if (position == 0) {
                holder.textView.setText("Oral Hygiene");
            } else if (position == 1) {
                holder.textView.setText("Skin Care");
            } else if (position == 2) {
                holder.textView.setText("Health Supplements");
            }
            Glide.with(activity)
                    .load(R.drawable.product_one)
                    .circleCrop()
                    .into(holder.image);
            holder.product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 0) {
                        new PREF(activity).setConfiguration("#EF7F1A", "#12C0DD");
                    } else if (position == 1) {
                        //new PREF(activity).setConfiguration("#EF7F1A", "#81B533");
                        new PREF(activity).setConfiguration("#EF7F1A", "#12C0DD");
                    } else if (position == 2) {
                        //new PREF(activity).setConfiguration("#EF7F1A", "#E8AE21");
                        new PREF(activity).setConfiguration("#EF7F1A", "#12C0DD");

                    }
                    Intent i = new Intent(activity, CategoryLandingActivity.class);
                    i.putExtra("targetCategory", "yes");
                    i.putExtra("fragment", "Home");
                    activity.startActivity(i);
                    activity.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
                    activity.finish();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return frameItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout product;
        TextView textView;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product = itemView.findViewById(R.id.product);
            image = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.text);

        }
    }


}
