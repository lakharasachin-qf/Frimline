package com.app.frimline.adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.ItemProductSectionOneLayoutBinding;
import com.app.frimline.databinding.ItemProductSectionThreeLayoutBinding;
import com.app.frimline.databinding.ItemProductSectionTwoLayoutBinding;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.app.frimline.screens.ProductDetailActivity;
import com.google.gson.Gson;

import java.util.ArrayList;


public class MultiViewAdapterForHomeFragment extends RecyclerView.Adapter {
    private ArrayList<HomeModel> dashBoardItemList;
    private final Gson gson;
    Activity activity;
    private boolean applyThemeColor=false;

    public void setApplyThemeColor(boolean applyThemeColor) {
        this.applyThemeColor = applyThemeColor;
    }

    public MultiViewAdapterForHomeFragment(ArrayList<HomeModel> dashBoardItemList, Activity activity) {
        this.dashBoardItemList = dashBoardItemList;
        this.activity = activity;
        gson = new Gson();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case LAYOUT_TYPE
                    .LAYOUT_ONE_PRODUCT:
                ItemProductSectionOneLayoutBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_product_section_one_layout, viewGroup, false);
                return new OneProductViewHolder(layoutBinding);
            case LAYOUT_TYPE
                    .LAYOUT_TWO_PRODUCT:
                ItemProductSectionTwoLayoutBinding twoLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_product_section_two_layout, viewGroup, false);
                return new TwoProductViewHolder(twoLayoutBinding);
            case LAYOUT_TYPE
                    .LAYOUT_THREE_PRODUCT:
                ItemProductSectionThreeLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_product_section_three_layout, viewGroup, false);
                return new ThreeProductViewHolder(binding);

        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (dashBoardItemList.get(position).getLayoutType()) {
            case 0:
                return LAYOUT_TYPE
                        .LAYOUT_ONE_PRODUCT;
            case 1:
                return LAYOUT_TYPE
                        .LAYOUT_TWO_PRODUCT;

            case 2:
                return LAYOUT_TYPE
                        .LAYOUT_THREE_PRODUCT;
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
                case LAYOUT_TYPE.LAYOUT_ONE_PRODUCT:


                    break;
                case LAYOUT_TYPE.LAYOUT_TWO_PRODUCT:


                    break;
                case LAYOUT_TYPE.LAYOUT_THREE_PRODUCT:

                    loadDataForLayoutThree(holder, position);

                    break;
            }
        }
    }
    String colorCode= "";
    public void loadDataForLayoutThree(RecyclerView.ViewHolder holder, int position) {

        //first product
        ((ThreeProductViewHolder) holder).binding.productLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(activity, ProductDetailActivity.class);
            }
        });
        ((ThreeProductViewHolder) holder).binding.productLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(activity, ProductDetailActivity.class);
            }
        });
        ((ThreeProductViewHolder) holder).binding.productLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(activity, ProductDetailActivity.class);
            }
        });

        if (applyThemeColor){
            colorCode=new PREF(activity).getThemeColor();
        }else {
            colorCode=new PREF(activity).getCategoryColor();
        }
        ((ThreeProductViewHolder) holder).binding.addCart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dashBoardItemList.get(position).getProductList().get(0).isAddedToCart()) {
                    dashBoardItemList.get(position).getProductList().get(0).setAddedToCart(true);
                    Toast.makeText(activity, "Added to cart", Toast.LENGTH_SHORT).show();
                    ((ThreeProductViewHolder) holder).binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
                } else {
                    dashBoardItemList.get(position).getProductList().get(0).setAddedToCart(false);
                    Toast.makeText(activity, "Removed from cart", Toast.LENGTH_SHORT).show();
                    ((ThreeProductViewHolder) holder).binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
                }


            }
        });

        ((ThreeProductViewHolder) holder).binding.addCart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dashBoardItemList.get(position).getProductList().get(1).isAddedToCart()) {
                    dashBoardItemList.get(position).getProductList().get(1).setAddedToCart(true);
                    Toast.makeText(activity, "Added to cart", Toast.LENGTH_SHORT).show();
                    ((ThreeProductViewHolder) holder).binding.addCart2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
                } else {
                    dashBoardItemList.get(position).getProductList().get(1).setAddedToCart(false);
                    Toast.makeText(activity, "Removed from cart", Toast.LENGTH_SHORT).show();
                    ((ThreeProductViewHolder) holder).binding.addCart2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
                }


            }
        });

        ((ThreeProductViewHolder) holder).binding.addCart3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dashBoardItemList.get(position).getProductList().get(2).isAddedToCart()) {
                    dashBoardItemList.get(position).getProductList().get(2).setAddedToCart(true);
                    ((ThreeProductViewHolder) holder).binding.addCart3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
                    Toast.makeText(activity, "Added to cart", Toast.LENGTH_SHORT).show();
                } else {
                    dashBoardItemList.get(position).getProductList().get(2).setAddedToCart(false);
                    Toast.makeText(activity, "Removed from cart", Toast.LENGTH_SHORT).show();
                    ((ThreeProductViewHolder) holder).binding.addCart3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
                }


            }
        });


    }


    public class OneProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductSectionOneLayoutBinding binding;

        public OneProductViewHolder(ItemProductSectionOneLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            changeColor();
        }

        public void changeColor() {
            PREF pref = new PREF(activity);
            binding.underLine.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
            binding.addToCartAction.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
        }
    }


    public class TwoProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductSectionTwoLayoutBinding binding;

        public TwoProductViewHolder(ItemProductSectionTwoLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public class ThreeProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductSectionThreeLayoutBinding binding;

        public ThreeProductViewHolder(ItemProductSectionThreeLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            changeColor();
        }

        public void changeColor() {
            PREF pref = new PREF(activity);
            String colorCode= "";
            if (applyThemeColor){
                colorCode=pref.getThemeColor();
            }else {
                colorCode=pref.getCategoryColor();
            }
            binding.underLineRight1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
            binding.underLineRight.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
            binding.underLine1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
            binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
            binding.addCart2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
            binding.addCart3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
        }
    }


}

