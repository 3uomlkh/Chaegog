package com.example.finalprojectvegan.Model;

import java.util.ArrayList;

public class RecipeData {
    private String title;
    private String imageUrl;
    private String clickUrl;
    private String itemKeyList;
    private ArrayList<String> bookmarkIdList;

    public RecipeData() {
        this.title = title;
        this.imageUrl = imageUrl;
        this.clickUrl = clickUrl;
        this.itemKeyList = itemKeyList;
    }

    public RecipeData(String imageUrl, String title, String clickUrl) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.clickUrl = clickUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getItemKeyList() {
        return itemKeyList;
    }

    public void setItemKeyList(String itemKeyList) {
        this.itemKeyList = itemKeyList;
    }

    public ArrayList<String> getBookmarkIdList() {
        return bookmarkIdList;
    }

    public void setBookmarkIdList(ArrayList<String> bookmarkIdList) {
        this.bookmarkIdList = bookmarkIdList;
    }
}
