package com.app.frimline.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.ObserverActionID;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.ItemProductSectionOneLayoutBinding;
import com.app.frimline.databinding.ItemShopTopProductLayoutBinding;
import com.app.frimline.models.HomeFragements.ProductModel;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.OutCategoryModel;
import com.app.frimline.screens.ProductDetailActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ShopTopProductAdapter extends RecyclerView.Adapter<ShopTopProductAdapter.ViewHolder> {
    private final ArrayList<ProductModel> frameItems;
    Activity activity;
    private int parentPosition;


    public void setParentPosition(int parentPosition) {
        this.parentPosition = parentPosition;
    }

    public ShopTopProductAdapter(ArrayList<ProductModel> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemShopTopProductLayoutBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_shop_top_product_layout, parent, false);
         return new ViewHolder(layoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProductModel model = frameItems.get(position);
        String colorCode = new PREF(activity).getThemeColor();

        if (CONSTANT.API_MODE)
            loadDataForOneLayout(holder.binding,model,position);

    }

    String colorCode = "";

    private void loadDataForOneLayout(ItemShopTopProductLayoutBinding binding, ProductModel model, int position) {
        colorCode = new PREF(activity).getThemeColor();

        Log.e("DATA",new Gson().toJson(model));
        if (model.isAddedToCart()) {
            binding.actionAddCart.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
        }

        Glide.with(activity).load(model.getProductImagesList().get(0))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_square_place_holder)
                .error(R.drawable.ic_square_place_holder)
                .into(binding.productImage);
        HELPER.LOAD_HTML(binding.productName, model.getName());
        HELPER.LOAD_HTML(binding.productPrice, activity.getString(R.string.Rs) + model.getPrice());
        //first product
        binding.product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ProductDetailActivity.class);
                i.putExtra("productPosition", "0");
                i.putExtra("themeColor", "0");
                i.putExtra("layoutType", String.valueOf("0"));
                i.putExtra("itemPosition", String.valueOf(parentPosition));
                i.putExtra("adapterPosition", String.valueOf(position));
                i.putExtra("model", new Gson().toJson(model));
                i.putExtra("addToCartID", String.valueOf(ObserverActionID.SHOP_ADDED_TO_CART));
                i.putExtra("removeCartID", String.valueOf(ObserverActionID.SHOP_REMOVE_FROM_CART));
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
            }
        });
        binding.actionAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!model.isAddedToCart()) {
                    model.setAddedToCart(true);
                    Toast.makeText(activity, "Added to cart", Toast.LENGTH_SHORT).show();
                    binding.actionAddCart.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
                } else {
                    model.setAddedToCart(false);
                    Toast.makeText(activity, "Removed from cart", Toast.LENGTH_SHORT).show();
                    binding.actionAddCart.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return frameItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemShopTopProductLayoutBinding binding;
        public ViewHolder(@NonNull ItemShopTopProductLayoutBinding binding) {
            super(binding.getRoot());
            this.binding =binding;
            binding.actionAddCart.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
            binding.underLine.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getThemeColor())));
        }
    }


}
