package com.app.frimline.models;

import java.util.ArrayList;

public class HomeModel {
    private int layoutType;
    private int layoutIndex;
    private String layoutName;
    private String name;
    private String apiUrl;
    private ArrayList<ProductModel> productModels;


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
}
