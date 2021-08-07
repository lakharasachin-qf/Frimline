package com.app.frimline.models.HomeFragements;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductModel {
    private String id;
    private String name;
    private String slug;
    private String description;
    private String shortDescription;
    private String price;
    private String regularPrice;
    private String priceHtml;
    private String stockStatus;
    private ArrayList<Tags> tagsModel;
    private ArrayList<String> productImagesList;
    private String categoryId;
    private String categoryName;

     private Attribute attribute;

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    //for cart
    private boolean isAddedToCart=false;

    public boolean isAddedToCart() {
        return isAddedToCart;
    }

    public void setAddedToCart(boolean addedToCart) {
        isAddedToCart = addedToCart;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(String regularPrice) {
        this.regularPrice = regularPrice;
    }

    public String getPriceHtml() {
        return priceHtml;
    }

    public void setPriceHtml(String priceHtml) {
        this.priceHtml = priceHtml;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public ArrayList<Tags> getTagsModel() {
        return tagsModel;
    }

    public void setTagsModel(ArrayList<Tags> tagsModel) {
        this.tagsModel = tagsModel;
    }

    public ArrayList<String> getProductImagesList() {
        return productImagesList;
    }

    public void setProductImagesList(ArrayList<String> productImagesList) {
        this.productImagesList = productImagesList;
    }


}

