package com.app.frimline.models.CategoryRootFragments;

import java.util.ArrayList;

public class CategoryRootModel {
    private String title;
    private String messages;
    private String themeColor;
    private ArrayList<CategorySingleModel> categoryList;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public ArrayList<CategorySingleModel> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<CategorySingleModel> categoryList) {
        this.categoryList = categoryList;
    }
}
