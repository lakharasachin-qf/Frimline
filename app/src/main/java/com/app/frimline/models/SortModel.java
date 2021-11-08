package com.app.frimline.models;

import com.app.frimline.models.categoryRootFragments.CategorySingleModel;

public class SortModel {
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    private String orderBy;
    private String order;
    private CategorySingleModel selectedCategory;
    private String minPrice;
    private String maxPrice;

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public CategorySingleModel getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(CategorySingleModel selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }
}
