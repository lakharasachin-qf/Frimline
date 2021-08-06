package com.app.frimline.models;

import com.app.frimline.models.CategoryRootFragments.CategorySingleModel;
import com.app.frimline.models.HomeFragements.BannerModel;
import com.app.frimline.models.HomeFragements.TradingStoriesModel;

import java.util.ArrayList;

public class HomeModel {
    private int layoutType;
    private int layoutIndex;
    private String layoutName;
    private String name;
    private String apiUrl;
    private ArrayList<ProductModel> productModels;
    private boolean isAddedToCart=false;

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


    private ArrayList<OutCategoryModel> productList=new ArrayList<>();

    public ArrayList<OutCategoryModel> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<OutCategoryModel> productList) {
        this.productList = productList;
    }


    private ArrayList<HomeModel> categoryProduct=new ArrayList<>();

    public ArrayList<HomeModel> getCategoryProduct() {
        return categoryProduct;
    }

    public void setCategoryProduct(ArrayList<HomeModel> categoryProduct) {
        this.categoryProduct = categoryProduct;
    }



    private ArrayList<CategorySingleModel> categoryArrayList=new ArrayList<>();

    public ArrayList<CategorySingleModel> getCategoryArrayList() {
        return categoryArrayList;
    }

    public void setCategoryArrayList(ArrayList<CategorySingleModel> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }

    /**
     * API MODE VARIABLES
     * */
    private ArrayList<com.app.frimline.models.HomeFragements.ProductModel> apiProductModel;

    public ArrayList<com.app.frimline.models.HomeFragements.ProductModel> getApiProductModel() {
        return apiProductModel;
    }

    public void setApiProductModel(ArrayList<com.app.frimline.models.HomeFragements.ProductModel> apiProductModel) {
        this.apiProductModel = apiProductModel;
    }
    private ArrayList<TradingStoriesModel> tradingStoriesList;

    public ArrayList<TradingStoriesModel> getTradingStoriesList() {
        return tradingStoriesList;
    }

    public void setTradingStoriesList(ArrayList<TradingStoriesModel> tradingStoriesList) {
        this.tradingStoriesList = tradingStoriesList;
    }
    private ArrayList<BannerModel> bannerList;

    public ArrayList<BannerModel> getBannerList() {
        return bannerList;
    }

    public void setBannerList(ArrayList<BannerModel> bannerList) {
        this.bannerList = bannerList;
    }
}
