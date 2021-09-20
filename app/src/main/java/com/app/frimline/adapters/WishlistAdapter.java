package com.app.frimline.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databaseHelper.CartRoomDatabase;
import com.app.frimline.databinding.ItemOrderHistoryLayoutBinding;
import com.app.frimline.databinding.ItemWishlistLayoutBinding;
import com.app.frimline.models.OrderModel;
import com.app.frimline.models.roomModels.ProductEntity;
import com.app.frimline.models.roomModels.WishlistEntity;
import com.app.frimline.screens.OrderHistoryViewActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    private final ArrayList<WishlistEntity> frameItems;
    private Activity activity;
    public interface actionInterface{
        void onDelete(int position,WishlistEntity entity);
        void onView(int position,WishlistEntity entity);
        void addToCart(int position,WishlistEntity entity);
    }
    private final CartRoomDatabase cartRoomDatabase;
    private actionInterface anInterface;

    public void setAnInterface(actionInterface anInterface) {
        this.anInterface = anInterface;
    }

    public WishlistAdapter(ArrayList<WishlistEntity> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
        cartRoomDatabase = CartRoomDatabase.getAppDatabase(activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWishlistLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_wishlist_layout, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final WishlistEntity model = frameItems.get(position);
        if (CONSTANT.API_MODE) {
            holder.binding.price.setText(activity.getString(R.string.Rs)+model.getPrice());
            holder.binding.productName.setText(model.getProductName());
            holder.binding.qty.setText("Qty : "+model.getQuantity());
            holder.binding.actionDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    anInterface.onDelete(position,model);
                }
            });
            holder.binding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    anInterface.onView(position,model);
                }
            });

            //check cart db
            ProductEntity entity = cartRoomDatabase.productEntityDao().findProductByProductId(model.getProductId());
            model.setAddedToCart(entity != null);

            if (model.isAddedToCart()) {
                holder.binding.addCart.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getThemeColor())));
            }else{
                holder.binding.addCart.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
            }

            holder.binding.addCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!model.isAddedToCart()) {
                        model.setAddedToCart(true);

                        holder.binding.addCart.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getThemeColor())));
                        anInterface.addToCart(position,model);
                    } else {
                        model.setAddedToCart(false);
                        Toast.makeText(activity, "Removed from cart", Toast.LENGTH_SHORT).show();
                        holder.binding.addCart.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
                        cartRoomDatabase.productEntityDao().deleteProduct(model.getProductId());
                    }
                    HELPER.changeCartCounter(activity);

                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return frameItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemWishlistLayoutBinding binding;

        public ViewHolder(@NonNull ItemWishlistLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }


}
