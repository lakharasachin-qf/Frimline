package com.app.frimline.models;

import com.app.frimline.models.CategoryRootFragments.CategorySingleModel;
import com.app.frimline.models.HomeFragements.BannerModel;
import com.app.frimline.models.HomeFragements.TradingStoriesModel;

import java.util.ArrayList;

public class BlogModel {
    private int layoutType;
    private String id;
    private String date;
    private String slug;
    private String title;
    private String content;
    private String shortContent;

    private ArrayList<BlogModel> blogList;

    public ArrayList<BlogModel> getBlogList() {
        return blogList;
    }

    public void setBlogList(ArrayList<BlogModel> blogList) {
        this.blogList = blogList;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShortContent() {
        return shortContent;
    }

    public void setShortContent(String shortContent) {
        this.shortContent = shortContent;
    }
}