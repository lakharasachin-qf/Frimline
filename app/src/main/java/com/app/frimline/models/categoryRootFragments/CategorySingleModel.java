package com.app.frimline.models.categoryRootFragments;

public class CategorySingleModel {
    private String categoryId;
    private String categoryName;
    private String slug;
    private String parents;
    private String descriptions;
    private String display;
    private String image;
    private String menuOrder;
    private String count;
    private String catColor;
    private String detailImage;
    private String longDescription;
    private String categorySliderImage;

    public String getCategorySliderImage() {
        return categorySliderImage;
    }

    public void setCategorySliderImage(String categorySliderImage) {
        this.categorySliderImage = categorySliderImage;
    }

    private boolean selectedModelForShopFiler=false;

    public boolean isSelectedModelForShopFiler() {
        return selectedModelForShopFiler;
    }

    public void setSelectedModelForShopFiler(boolean selectedModelForShopFiler) {
        this.selectedModelForShopFiler = selectedModelForShopFiler;
    }

    private boolean isActive = false;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getParents() {
        return parents;
    }

    public void setParents(String parents) {
        this.parents = parents;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(String menuOrder) {
        this.menuOrder = menuOrder;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCatColor() {
        return catColor;
    }

    public void setCatColor(String catColor) {
        this.catColor = catColor;
    }

    public String getDetailImage() {
        return detailImage;
    }

    public void setDetailImage(String detailImage) {
        this.detailImage = detailImage;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }
}
