package com.app.frimline.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.R;
import com.app.frimline.databinding.ItemSearchFirstContainerLayoutBinding;
import com.app.frimline.databinding.ItemShopFilterChipContainerLayoutBinding;
import com.app.frimline.models.CategoryRootFragments.CategorySingleModel;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.app.frimline.models.OutCategoryModel;
import com.app.frimline.models.ProductModel;
import com.app.frimline.models.SearchModel;
import com.google.gson.Gson;

import java.util.ArrayList;


public class SearchAdapter extends RecyclerView.Adapter {
    private ArrayList<SearchModel> dashBoardItemList;
    private final Gson gson;
    Activity activity;

    public SearchAdapter(ArrayList<SearchModel> dashBoardItemList, Activity activity) {
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
                ItemSearchFirstContainerLayoutBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_search_first_container_layout, viewGroup, false);
                return new TopProductContainer(layoutBinding);
            case LAYOUT_TYPE
                    .LAYOUT_FILTER_CHIP:
                ItemShopFilterChipContainerLayoutBinding twoLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_shop_filter_chip_container_layout, viewGroup, false);
                return new FilterHolder(twoLayoutBinding);
            case LAYOUT_TYPE
                    .LAYOUT_HOT_PRODUCT:
                ItemSearchFirstContainerLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_search_first_container_layout, viewGroup, false);
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
        final SearchModel model = dashBoardItemList.get(position);
        if (model != null) {
            switch (model.getLayoutType()) {
                case LAYOUT_TYPE.LAYOUT_TOP_PRODUCT:
                    MultiViewAdapterForSearch productAdapte3r;
                    if (CONSTANT.API_MODE) {
                        productAdapte3r = new MultiViewAdapterForSearch(model.getTopProduct(), activity);
                        productAdapte3r.setApplyThemeColor(true);
                        productAdapte3r.setPosition(0);
                        SnapHelper startSnapHelper = new PagerSnapHelper();
                        ((TopProductContainer) holder).binding.productRecycler.setOnFlingListener(null);
                        startSnapHelper.attachToRecyclerView(((TopProductContainer) holder).binding.productRecycler);

                    } else {

                        productAdapte3r = new MultiViewAdapterForSearch(setAdapterForProduct(), activity);
                        productAdapte3r.setPosition(0);
                        productAdapte3r.setApplyThemeColor(true);
                        SnapHelper startSnapHelper = new PagerSnapHelper();
                        ((TopProductContainer) holder).binding.productRecycler.setOnFlingListener(null);
                        startSnapHelper.attachToRecyclerView(((TopProductContainer) holder).binding.productRecycler);
                    }
                    ((TopProductContainer) holder).binding.productRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    ((TopProductContainer) holder).binding.productRecycler.setAdapter(productAdapte3r);
                    break;
                case LAYOUT_TYPE.LAYOUT_FILTER_CHIP:
                    if (CONSTANT.API_MODE) {
                        SearchFilterAdapter shopFilterAdapter = new SearchFilterAdapter(model.getCategoryList(), activity);
                        shopFilterAdapter.setCategoryFilter(selectedCategory);
                        ((FilterHolder) holder).binding.filterRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                        ((FilterHolder) holder).binding.filterRecycler.setAdapter(shopFilterAdapter);
                    } else {
                        ArrayList<CategorySingleModel> hitModel2s = new ArrayList<>();
                        hitModel2s.add(new CategorySingleModel());
                        hitModel2s.add(new CategorySingleModel());
                        hitModel2s.add(new CategorySingleModel());
                        hitModel2s.add(new CategorySingleModel());
                        SearchFilterAdapter shopFilterAdapter = new SearchFilterAdapter(hitModel2s, activity);
                        ((FilterHolder) holder).binding.filterRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                        ((FilterHolder) holder).binding.filterRecycler.setAdapter(shopFilterAdapter);
                    }
                    break;
                case LAYOUT_TYPE.LAYOUT_HOT_PRODUCT:
                    if (CONSTANT.API_MODE) {
                        SearchHotAdapter shopHotProductAdapter = new SearchHotAdapter(model.getHotProduct(), activity);
                        ((HotProductHolder) holder).binding.productRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                        ((HotProductHolder) holder).binding.productRecycler.setAdapter(shopHotProductAdapter);
                    } else {
                        ArrayList<com.app.frimline.models.HomeFragements.ProductModel> hitModels = new ArrayList<>();
                        hitModels.add(new com.app.frimline.models.HomeFragements.ProductModel());
                        hitModels.add(new com.app.frimline.models.HomeFragements.ProductModel());
                        hitModels.add(new com.app.frimline.models.HomeFragements.ProductModel());
                        hitModels.add(new com.app.frimline.models.HomeFragements.ProductModel());

                        SearchHotAdapter shopHotProductAdapter = new SearchHotAdapter(hitModels, activity);
                        ((HotProductHolder) holder).binding.productRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                        ((HotProductHolder) holder).binding.productRecycler.setAdapter(shopHotProductAdapter);
                    }
                    break;
            }
        }
    }

    public ArrayList<HomeModel> setAdapterForProduct() {
        ArrayList<HomeModel> productArray3 = new ArrayList<>();
        ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
        ProductModel productModel = new ProductModel();
        productModelArrayList.add(productModel);
        productModelArrayList.add(productModel);
        productModelArrayList.add(productModel);

        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_THREE_PRODUCT);
        homeModel.setProductModels(productModelArrayList);
        ArrayList<OutCategoryModel> innerDataList = new ArrayList<>();
        OutCategoryModel outCategoryModel = new OutCategoryModel();
        outCategoryModel.setName("");
        innerDataList.add(outCategoryModel);
        outCategoryModel = new OutCategoryModel();
        outCategoryModel.setName("");
        innerDataList.add(outCategoryModel);
        outCategoryModel = new OutCategoryModel();
        outCategoryModel.setName("");
        innerDataList.add(outCategoryModel);
        homeModel.setProductList(innerDataList);

        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_THREE_PRODUCT);
        homeModel.setProductModels(productModelArrayList);


        productArray3.add(homeModel);
        productArray3.add(homeModel);
        productArray3.add(homeModel);


        return productArray3;
    }
    CategorySingleModel selectedCategory;
    public void setCategoryFilter(CategorySingleModel selectedCategory) {
        this.selectedCategory=selectedCategory;
    }

    public class TopProductContainer extends RecyclerView.ViewHolder {
        ItemSearchFirstContainerLayoutBinding binding;

        public TopProductContainer(ItemSearchFirstContainerLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

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
        ItemSearchFirstContainerLayoutBinding binding;

        public HotProductHolder(ItemSearchFirstContainerLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }

    }


}

