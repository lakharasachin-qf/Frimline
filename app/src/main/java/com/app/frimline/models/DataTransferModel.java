package com.app.frimline.models;

public class DataTransferModel {
    private String productPosition;
    private String layoutType;
    private String itemPosition;
    private String adapterPosition;
    private String productId;

    private String cartAddedId;
    private String cartRemovedId;

    public String getCartAddedId() {
        return cartAddedId;
    }

    public void setCartAddedId(String cartAddedId) {
        this.cartAddedId = cartAddedId;
    }

    public String getCartRemovedId() {
        return cartRemovedId;
    }

    public void setCartRemovedId(String cartRemovedId) {
        this.cartRemovedId = cartRemovedId;
    }

    public String getAdapterPosition() {
        return adapterPosition;
    }

    public void setAdapterPosition(String adapterPosition) {
        this.adapterPosition = adapterPosition;
    }

    public String getProductPosition() {
        return productPosition;
    }

    public void setProductPosition(String productPosition) {
        this.productPosition = productPosition;
    }

    public String getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
    }

    public String getItemPosition() {
        return itemPosition;
    }

    public void setItemPosition(String itemPosition) {
        this.itemPosition = itemPosition;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
