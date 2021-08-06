package com.app.frimline.models.HomeFragements;

import java.util.ArrayList;

public class HomeRootModel {
    private int layoutIndex;
    private ArrayList<SectionPositionModel> layoutsPositionsList;
    private ArrayList<String> bannerList;
    private ArrayList<String> trendingStoriesList;
    private ArrayList<ProductModel> topRattedList;
    private ArrayList<ProductModel> productList;
    private ArrayList<CategoryModel> categoryList;


    public ArrayList<SectionPositionModel> getLayoutsPositionsList() {
        return layoutsPositionsList;
    }

    public void setLayoutsPositionsList(ArrayList<SectionPositionModel> layoutsPositionsList) {
        this.layoutsPositionsList = layoutsPositionsList;
    }

    public ArrayList<String> getBannerList() {
        return bannerList;
    }

    public void setBannerList(ArrayList<String> bannerList) {
        this.bannerList = bannerList;
    }

    public ArrayList<String> getTrendingStoriesList() {
        return trendingStoriesList;
    }

    public void setTrendingStoriesList(ArrayList<String> trendingStoriesList) {
        this.trendingStoriesList = trendingStoriesList;
    }

    public ArrayList<ProductModel> getTopRattedList() {
        return topRattedList;
    }

    public void setTopRattedList(ArrayList<ProductModel> topRattedList) {
        this.topRattedList = topRattedList;
    }

    public ArrayList<ProductModel> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ProductModel> productList) {
        this.productList = productList;
    }

    public ArrayList<CategoryModel> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<CategoryModel> categoryList) {
        this.categoryList = categoryList;
    }
}
