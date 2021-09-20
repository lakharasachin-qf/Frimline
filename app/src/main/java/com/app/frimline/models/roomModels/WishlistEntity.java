package com.app.frimline.models.roomModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.app.frimline.models.HomeFragements.Attribute;
import com.app.frimline.models.HomeFragements.Tags;

import java.util.ArrayList;

@Entity(tableName = "wishlist")
public class WishlistEntity {
    @PrimaryKey(autoGenerate = true)
    private int tableId;

    @ColumnInfo(name = "product_name")
    private String productName;

    @ColumnInfo(name = "ID")
    private String ID;

    @ColumnInfo(name = "prod_id")
    private String productId;

    @ColumnInfo(name = "quantity")
    private String quantity;

    @ColumnInfo(name = "user_id")
    private String userId;

    @ColumnInfo(name = "wishlist_id")
    private String wishlistId;

    @ColumnInfo(name = "original_price")
    private String price;


    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(String wishlistId) {
        this.wishlistId = wishlistId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
