package com.app.frimline.adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.models.OutCategoryModel;
import com.app.frimline.screens.OrderHistoryViewActivity;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private final ArrayList<OutCategoryModel> frameItems;
    Activity activity;


    public OrderHistoryAdapter(ArrayList<OutCategoryModel> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OutCategoryModel model = frameItems.get(position);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(activity, OrderHistoryViewActivity.class);
            }
        });
    }
    @Override
    public int getItemCount() {
        return frameItems.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView itemLayout;
        AppCompatButton viewDetails;
        TextView deliveryTxt;
        ImageView deliveryIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.itemLayout);
            viewDetails = itemView.findViewById(R.id.viewDetailsBtn);
            deliveryTxt = itemView.findViewById(R.id.deliveredTxt);
            deliveryIcon = itemView.findViewById(R.id.delivaryIcon);

            PREF pref=new PREF(activity);
            viewDetails.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
          //  deliveryIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
           // deliveryTxt.setTextColor(Color.parseColor(pref.getCategoryColor()));
        }
    }


}