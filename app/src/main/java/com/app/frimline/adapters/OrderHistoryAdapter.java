package com.app.frimline.adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.ItemOrderHistoryLayoutBinding;
import com.app.frimline.models.OrderModel;
import com.app.frimline.screens.OrderHistoryViewActivity;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private final ArrayList<OrderModel> frameItems;
    Activity activity;


    public OrderHistoryAdapter(ArrayList<OrderModel> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderHistoryLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_order_history_layout, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OrderModel model = frameItems.get(position);
        if (CONSTANT.API_MODE) {
//            if (model.getStatus().equalsIgnoreCase("")) {
//                holder.binding.deliveredTxt.setText("Canceled");
//                holder.binding.deliveredTxt.setTextColor(ContextCompat.getColor(activity, R.color.deliveryFaild));
//                holder.binding.delivaryIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.deliveryFaild)));
//                holder.binding.delivaryIcon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_cancel_black_24dp));
//            }
//            if (position == 1 || position == 5) {
//                holder.binding.deliveredTxt.setText("Pending");
//                holder.binding.deliveredTxt.setTextColor(ContextCompat.getColor(activity, R.color.deliveryPending));
//                holder.binding.delivaryIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.deliveryPending)));
//                holder.binding.delivaryIcon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_pending_icon));
//            }
//
//            if (position == 2) {
//                holder.binding.deliveredTxt.setText("Delivered");
//                holder.binding.deliveredTxt.setTextColor(ContextCompat.getColor(activity, R.color.deliverySuccess));
//                holder.binding.delivaryIcon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_delivery_icon));
//                holder.binding.delivaryIcon.setImageTintList(null);
//            }
            holder.binding.rate.setVisibility(View.GONE);
            HELPER.LOAD_HTML(holder.binding.orderId,"Order Id:" + model.getOrderKey());
            HELPER.LOAD_HTML(holder.binding.price,activity.getString(R.string.Rs)+model.getTotal());

        } else {
            holder.binding.viewDetailsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HELPER.SIMPLE_ROUTE(activity, OrderHistoryViewActivity.class);
                }
            });

            if (position == 0 || position == 4) {
                holder.binding.deliveredTxt.setText("Canceled");
                holder.binding.deliveredTxt.setTextColor(ContextCompat.getColor(activity, R.color.deliveryFaild));
                holder.binding.delivaryIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.deliveryFaild)));
                holder.binding.delivaryIcon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_cancel_black_24dp));
            }
            if (position == 1 || position == 5) {
                holder.binding.deliveredTxt.setText("Pending");
                holder.binding.deliveredTxt.setTextColor(ContextCompat.getColor(activity, R.color.deliveryPending));
                holder.binding.delivaryIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.deliveryPending)));
                holder.binding.delivaryIcon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_pending_icon));
            }

            if (position == 2) {
                holder.binding.deliveredTxt.setText("Delivered");
                holder.binding.deliveredTxt.setTextColor(ContextCompat.getColor(activity, R.color.deliverySuccess));
                holder.binding.delivaryIcon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_delivery_icon));
                holder.binding.delivaryIcon.setImageTintList(null);
            }
        }
    }

    public void loadData() {

    }

    @Override
    public int getItemCount() {
        return frameItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemOrderHistoryLayoutBinding binding;

        public ViewHolder(@NonNull ItemOrderHistoryLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            PREF pref = new PREF(activity);
            binding.viewDetailsBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
            //  deliveryIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
            // deliveryTxt.setTextColor(Color.parseColor(pref.getCategoryColor()));
        }
    }


}
