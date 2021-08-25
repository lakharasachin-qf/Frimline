package com.app.frimline.models;

import java.util.ArrayList;

public class BlogModel {
    private int layoutType;
    private String id;
    private String date;
    private String slug;
    private String title;
    private String content;
    private String shortContent;
    private String blogImage;

    private ArrayList<BlogModel> blogList;

    public String getBlogImage() {
        return blogImage;
    }

    public void setBlogImage(String blogImage) {
        this.blogImage = blogImage;
    }

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
