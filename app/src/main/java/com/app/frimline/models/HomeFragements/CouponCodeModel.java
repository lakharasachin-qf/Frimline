package com.app.frimline.models.HomeFragements;

import java.util.ArrayList;

public class CouponCodeModel {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String couponCode;

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    private ArrayList<String> productIds = new ArrayList<>();
    private ArrayList<String> excludeProductIds = new ArrayList<>();

    private ArrayList<String> categoryIds = new ArrayList<>();
    private ArrayList<String> excludeCategoryIds = new ArrayList<>();
    private String minAmount;
    private String maxAmount;
    private ArrayList<String> emailIds = new ArrayList<>();

    public ArrayList<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(ArrayList<String> productIds) {
        this.productIds = productIds;
    }

    public ArrayList<String> getExcludeProductIds() {
        return excludeProductIds;
    }

    public void setExcludeProductIds(ArrayList<String> excludeProductIds) {
        this.excludeProductIds = excludeProductIds;
    }

    public ArrayList<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(ArrayList<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public ArrayList<String> getExcludeCategoryIds() {
        return excludeCategoryIds;
    }

    public void setExcludeCategoryIds(ArrayList<String> excludeCategoryIds) {
        this.excludeCategoryIds = excludeCategoryIds;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public ArrayList<String> getEmailIds() {
        return emailIds;
    }

    public void setEmailIds(ArrayList<String> emailIds) {
        this.emailIds = emailIds;
    }
}
