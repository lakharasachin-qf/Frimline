package com.app.frimline.models.roomModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.app.frimline.models.HomeFragements.Attribute;
import com.app.frimline.models.HomeFragements.Tags;

import java.util.ArrayList;

@Entity(tableName = "cart")
public class ProductEntity {
    @PrimaryKey(autoGenerate = true)
    private int cartId;

    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "slug")
    private String slug;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "shortDescription")
    private String shortDescription;

    @ColumnInfo(name = "price")
    private String price;

    @ColumnInfo(name = "regularPrice")
    private String regularPrice;

    @ColumnInfo(name = "priceHtml")
    private String priceHtml;

    @ColumnInfo(name = "stockStatus")
    private String stockStatus;

    @TypeConverters(TagConverter.class)
    @ColumnInfo(name = "tags")
    private ArrayList<Tags> tagsModel;

    @TypeConverters(StringConverter.class)
    @ColumnInfo(name = "imageList")
    private ArrayList<String> productImagesList;

    @ColumnInfo(name = "categoryId")
    private String categoryId;

    @ColumnInfo(name = "categoryName")
    private String categoryName;

    @TypeConverters(AttributeConverter.class)
    @ColumnInfo(name = "attribute")
    private Attribute attribute;

    @ColumnInfo(name = "qty")
    private String qty;


    @ColumnInfo(name = "calculatedAmount")
    private String calculatedAmount;

    @ColumnInfo(name = "rating")
    private String rating;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


    public String getCalculatedAmount() {
        return calculatedAmount;
    }

    public void setCalculatedAmount(String calculatedAmount) {
        this.calculatedAmount = calculatedAmount;
    }


    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public Attribute getAttribute() {
        return attribute;
    }


    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
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


    private boolean isAddedToCart = false;

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
