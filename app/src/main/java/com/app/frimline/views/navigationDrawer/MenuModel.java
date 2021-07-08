package com.app.frimline.views.navigationDrawer;

import android.graphics.drawable.Drawable;

public class MenuModel {
    public String menuName, url;
    public boolean hasChildren, isGroup;
    public Drawable icon;

    public MenuModel(String menuName, boolean isGroup, boolean hasChildren, String url, Drawable icon) {

        this.menuName = menuName;
        this.url = url;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
        this.icon = icon;
    }
}