package com.app.frimline.adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.ItemShopFilterChipContainerLayoutBinding;
import com.app.frimline.databinding.ItemShopHotProductContainerLayoutBinding;
import com.app.frimline.databinding.ItemShopTopProductContainerLayoutBinding;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.app.frimline.models.OutCategoryModel;
import com.google.gson.Gson;

import java.util.ArrayList;


public class ShopAdapter extends RecyclerView.Adapter {
    private ArrayList<HomeModel> dashBoardItemList;
    private final Gson gson;
    Activity activity;

    public ShopAdapter(ArrayList<HomeModel> dashBoardItemList, Activity activity) {
        this.dashBoardItemList = dashBoardItemList;
        this.activity = activity;
        gson = new Gson();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case LAYOUT_TYPE
                    .LAYOUT_TOP_PRODUCT:
                ItemShopTopProductContainerLayoutBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_shop_top_product_container_layout, viewGroup, false);
                return new TopProductContainer(layoutBinding);
            case LAYOUT_TYPE
                    .LAYOUT_FILTER_CHIP:
                ItemShopFilterChipContainerLayoutBinding twoLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_shop_filter_chip_container_layout, viewGroup, false);
                return new FilterHolder(twoLayoutBinding);
            case LAYOUT_TYPE
                    .LAYOUT_HOT_PRODUCT:
                ItemShopHotProductContainerLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_shop_hot_product_container_layout, viewGroup, false);
                return new HotProductHolder(binding);

        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (dashBoardItemList.get(position).getLayoutType()) {
            case 0:
                return LAYOUT_TYPE
                        .LAYOUT_TOP_PRODUCT;
            case 1:
                return LAYOUT_TYPE
                        .LAYOUT_FILTER_CHIP;

            case 2:
                return LAYOUT_TYPE
                        .LAYOUT_HOT_PRODUCT;
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
        final HomeModel model = dashBoardItemList.get(position);
        if (model != null) {
            switch (model.getLayoutType()) {
                case LAYOUT_TYPE.LAYOUT_TOP_PRODUCT:
                    ArrayList<OutCategoryModel> data=new ArrayList<>();
                    data.add(new OutCategoryModel());
                    data.add(new OutCategoryModel());
                    data.add(new OutCategoryModel());
                    data.add(new OutCategoryModel());
                    ShopTopProductAdapter productAdapte3r = new ShopTopProductAdapter(data, activity);
                    ((TopProductContainer) holder).binding.productRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    ((TopProductContainer) holder).binding.productRecycler.setAdapter(productAdapte3r);
                    break;
                case LAYOUT_TYPE.LAYOUT_FILTER_CHIP:
                    ArrayList<String> strings=new ArrayList<>();
                    strings.add("Skin care");
                    strings.add("Oral Hygine");
                    strings.add("Health Supplement");
                    ShopFilterAdapter shopFilterAdapter = new ShopFilterAdapter(strings, activity);
                    ((FilterHolder) holder).binding.filterRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    ((FilterHolder) holder).binding.filterRecycler.setAdapter(shopFilterAdapter);

                    break;
                case LAYOUT_TYPE.LAYOUT_HOT_PRODUCT:
                    ArrayList<OutCategoryModel> hitModels=new ArrayList<>();
                    hitModels.add(new OutCategoryModel());
                    hitModels.add(new OutCategoryModel());
                    hitModels.add(new OutCategoryModel());
                    hitModels.add(new OutCategoryModel());
                    ShopHotProductAdapter shopHotProductAdapter = new ShopHotProductAdapter(hitModels, activity);
                    ((HotProductHolder) holder).binding.productRecycler.setLayoutManager(new GridLayoutManager(activity,2));
                    ((HotProductHolder) holder).binding.productRecycler.setAdapter(shopHotProductAdapter);
                    break;
            }
        }
    }



    public class TopProductContainer extends RecyclerView.ViewHolder {
        ItemShopTopProductContainerLayoutBinding binding;

        public TopProductContainer(ItemShopTopProductContainerLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            changeColor();
        }
        public void changeColor(){
            PREF pref=new PREF(activity);
            binding.view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));

        }
    }


    public class FilterHolder extends RecyclerView.ViewHolder {
        ItemShopFilterChipContainerLayoutBinding binding;

        public FilterHolder(ItemShopFilterChipContainerLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public class HotProductHolder extends RecyclerView.ViewHolder {
        ItemShopHotProductContainerLayoutBinding binding;

        public HotProductHolder(ItemShopHotProductContainerLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            changeColor();
        }
        public void changeColor(){
            PREF pref=new PREF(activity);
            binding.view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));

        }
    }


}

