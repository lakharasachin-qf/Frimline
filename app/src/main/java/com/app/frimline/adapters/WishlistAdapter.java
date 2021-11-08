package com.app.frimline.adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.common.CONSTANT;
import com.app.frimline.common.HELPER;
import com.app.frimline.common.PREF;
import com.app.frimline.R;
import com.app.frimline.databaseHelper.CartRoomDatabase;
import com.app.frimline.databinding.ItemWishlistLayoutBinding;
import com.app.frimline.models.roomModels.ProductEntity;
import com.app.frimline.models.roomModels.WishlistEntity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    private final ArrayList<WishlistEntity> frameItems;
    private final Activity activity;

    public interface actionInterface {
        void onDelete(int position, WishlistEntity entity);

        void onView(int position, WishlistEntity entity);

        void addToCart(int position, WishlistEntity entity);
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
            Glide.with(activity)
                    .load(model.getImage())
                    .placeholder(R.drawable.ic_square_place_holder)
                    .error(R.drawable.ic_square_place_holder)
                    .into(holder.binding.productImage);
            holder.binding.price.setText(activity.getString(R.string.Rs) + model.getPrice());
            holder.binding.productName.setText(model.getProductName());
            holder.binding.qty.setText("Qty : " + model.getQuantity());

            holder.binding.actionDelete.setOnClickListener(v -> anInterface.onDelete(position, model));
            holder.binding.itemLayout.setOnClickListener(v -> anInterface.onView(position, model));

            ProductEntity entity = cartRoomDatabase.productEntityDao().findProductByProductId(model.getProductId());
            model.setAddedToCart(entity != null);

            if (model.isAddedToCart()) {
                holder.binding.addCart.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getThemeColor())));
            } else {
                holder.binding.addCart.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
            }

            holder.binding.addCart.setOnClickListener(v -> {
                if (!model.isAddedToCart()) {
                    model.setAddedToCart(true);

                    holder.binding.addCart.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getThemeColor())));
                    anInterface.addToCart(position, model);
                } else {
                    model.setAddedToCart(false);
                    Toast.makeText(activity, "Removed from cart", Toast.LENGTH_SHORT).show();
                    holder.binding.addCart.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
                    cartRoomDatabase.productEntityDao().deleteProduct(model.getProductId());
                }
                HELPER.changeCartCounter(activity);

            });

        }
    }


    @Override
    public int getItemCount() {
        return frameItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemWishlistLayoutBinding binding;

        public ViewHolder(@NonNull ItemWishlistLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }


}
