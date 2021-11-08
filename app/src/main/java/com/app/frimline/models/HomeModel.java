package com.app.frimline.models;

import com.app.frimline.models.categoryRootFragments.CategorySingleModel;
import com.app.frimline.models.homeFragments.BannerModel;
import com.app.frimline.models.homeFragments.CouponCodeModel;
import com.app.frimline.models.homeFragments.TradingStoriesModel;

import java.util.ArrayList;

public class HomeModel {
    private int layoutType;
    private int layoutIndex;
    private String layoutName;
    private String name;
    private String apiUrl;
    private ArrayList<ProductModel> productModels;
    private boolean isAddedToCart = false;
    private ArrayList<OutCategoryModel> productList = new ArrayList<>();
    private ArrayList<HomeModel> categoryProduct = new ArrayList<>();
    private ArrayList<CategorySingleModel> categoryArrayList = new ArrayList<>();
    /**
     * API MODE VARIABLES
     */
    private ArrayList<com.app.frimline.models.homeFragments.ProductModel> apiProductModel;
    private ArrayList<TradingStoriesModel> tradingStoriesList;
    private ArrayList<BannerModel> bannerList;
    private ArrayList<CouponCodeModel> couponCodeBannerList;

    public boolean isAddedToCart() {
        return isAddedToCart;
    }

    public void setAddedToCart(boolean addedToCart) {
        isAddedToCart = addedToCart;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    public int getLayoutIndex() {
        return layoutIndex;
    }

    public void setLayoutIndex(int layoutIndex) {
        this.layoutIndex = layoutIndex;
    }

    public ArrayList<ProductModel> getProductModels() {
        return productModels;
    }

    public void setProductModels(ArrayList<ProductModel> productModels) {
        this.productModels = productModels;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<OutCategoryModel> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<OutCategoryModel> productList) {
        this.productList = productList;
    }

    public ArrayList<HomeModel> getCategoryProduct() {
        return categoryProduct;
    }

    public void setCategoryProduct(ArrayList<HomeModel> categoryProduct) {
        this.categoryProduct = categoryProduct;
    }

    public ArrayList<CategorySingleModel> getCategoryArrayList() {
        return categoryArrayList;
    }

    public void setCategoryArrayList(ArrayList<CategorySingleModel> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }

    public ArrayList<com.app.frimline.models.homeFragments.ProductModel> getApiProductModel() {
        return apiProductModel;
    }

    public void setApiProductModel(ArrayList<com.app.frimline.models.homeFragments.ProductModel> apiProductModel) {
        this.apiProductModel = apiProductModel;
    }

    public ArrayList<TradingStoriesModel> getTradingStoriesList() {
        return tradingStoriesList;
    }

    public void setTradingStoriesList(ArrayList<TradingStoriesModel> tradingStoriesList) {
        this.tradingStoriesList = tradingStoriesList;
    }

    public ArrayList<BannerModel> getBannerList() {
        return bannerList;
    }

    public void setBannerList(ArrayList<BannerModel> bannerList) {
        this.bannerList = bannerList;
    }

    public ArrayList<CouponCodeModel> getCouponCodeBannerList() {
        return couponCodeBannerList;
    }

    public void setCouponCodeBannerList(ArrayList<CouponCodeModel> couponCodeBannerList) {
        this.couponCodeBannerList = couponCodeBannerList;
    }
}
