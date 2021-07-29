package com.app.frimline.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.models.OutCategoryModel;
import com.app.frimline.screens.CategoryLandingActivity;

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
        if (position == 0) {
            holder.textView.setText("Oral Hygiene");
        } else if (position == 1) {
            holder.textView.setText("Skin Care");
        } else if (position == 2) {
            holder.textView.setText("Health Supplements");
        }
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

    @Override
    public int getItemCount() {
        return frameItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout product;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product = itemView.findViewById(R.id.product);
            textView = itemView.findViewById(R.id.text);

        }
    }


}
