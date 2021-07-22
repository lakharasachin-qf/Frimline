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

import com.app.frimline.R;
import com.app.frimline.databinding.ItemSearchFirstContainerLayoutBinding;
import com.app.frimline.databinding.ItemShopFilterChipContainerLayoutBinding;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.app.frimline.models.OutCategoryModel;
import com.app.frimline.models.ProductModel;
import com.google.gson.Gson;

import java.util.ArrayList;


public class SearchAdapter extends RecyclerView.Adapter {
    private ArrayList<HomeModel> dashBoardItemList;
    private final Gson gson;
    Activity activity;

    public SearchAdapter(ArrayList<HomeModel> dashBoardItemList, Activity activity) {
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
        final HomeModel model = dashBoardItemList.get(position);
        if (model != null) {
            switch (model.getLayoutType()) {
                case LAYOUT_TYPE.LAYOUT_TOP_PRODUCT:


                    MultiViewAdapterForHomeFragment productAdapte3r = new MultiViewAdapterForHomeFragment(setAdapterForProduct(), activity);
                    SnapHelper startSnapHelper = new PagerSnapHelper();
                    ((TopProductContainer) holder).binding.productRecycler.setOnFlingListener(null);
                    startSnapHelper.attachToRecyclerView(((TopProductContainer) holder).binding.productRecycler);

                    ((TopProductContainer) holder).binding.productRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    ((TopProductContainer) holder).binding.productRecycler.setAdapter(productAdapte3r);
                    break;
                case LAYOUT_TYPE.LAYOUT_FILTER_CHIP:
                    ArrayList<String> strings = new ArrayList<>();
                    strings.add("Skin care");
                    strings.add("Oral Hygine");
                    strings.add("Health Supplement");
                    ShopFilterAdapter shopFilterAdapter = new ShopFilterAdapter(strings, activity);
                    ((FilterHolder) holder).binding.filterRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    ((FilterHolder) holder).binding.filterRecycler.setAdapter(shopFilterAdapter);

                    break;
                case LAYOUT_TYPE.LAYOUT_HOT_PRODUCT:
                    ArrayList<OutCategoryModel> hitModels = new ArrayList<>();
                    hitModels.add(new OutCategoryModel());
                    hitModels.add(new OutCategoryModel());
                    hitModels.add(new OutCategoryModel());
                    hitModels.add(new OutCategoryModel());

                    ShopTopProductAdapter shopHotProductAdapter = new ShopTopProductAdapter(hitModels, activity);
                    ((HotProductHolder) holder).binding.productRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    ((HotProductHolder) holder).binding.productRecycler.setAdapter(shopHotProductAdapter);
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

        productArray3.add(homeModel);
        productArray3.add(homeModel);
        productArray3.add(homeModel);


//        MultiViewAdapterForHomeFragment productAdapter = new MultiViewAdapterForHomeFragment(productArray, getActivity());
//        binding.categoryProductRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        binding.categoryProductRecycler.setHasFixedSize(true);
//        SnapHelper startSnapHelper = new PagerSnapHelper();
//        binding.categoryProductRecycler.setOnFlingListener(null);
//        startSnapHelper.attachToRecyclerView(binding.categoryProductRecycler);
//        binding.categoryProductRecycler.setAdapter(productAdapter);

        return productArray3;
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

