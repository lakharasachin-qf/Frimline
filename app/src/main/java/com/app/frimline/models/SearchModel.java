package com.app.frimline.models;

import com.app.frimline.models.CategoryRootFragments.CategorySingleModel;
import com.app.frimline.models.HomeFragements.ProductModel;

import java.util.ArrayList;

public class SearchModel {
    private ArrayList<HomeModel> topProduct;
    private ArrayList<ProductModel> hotProduct;
    private ArrayList<CategorySingleModel> categoryList;
    private int layoutType;

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public ArrayList<HomeModel> getTopProduct() {
        return topProduct;
    }

    public void setTopProduct(ArrayList<HomeModel> topProduct) {
        this.topProduct = topProduct;
    }

    public ArrayList<ProductModel> getHotProduct() {
        return hotProduct;
    }

    public void setHotProduct(ArrayList<ProductModel> hotProduct) {
        this.hotProduct = hotProduct;
    }

    public ArrayList<CategorySingleModel> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<CategorySingleModel> categoryList) {
        this.categoryList = categoryList;
    }
}
