package com.cchaegog.chaegog.Model;

import java.util.ArrayList;

public class ProductData {
    private String productName;
    private String productCompany;
    private String productImg;
    private String itemKey;
    private ArrayList<String> bookmarkIdList;

    public ProductData(String name, String company, String image, String itemKey) {
        this.productName = name;
        this.productCompany = company;
        this.productImg = image;
        this.itemKey = itemKey;
    }

    public ProductData(String name, String company, String image) {
        this.productName = name;
        this.productCompany = company;
        this.productImg = image;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCompany() {
        return productCompany;
    }

    public void setProductCompany(String productCompany) {
        this.productCompany = productCompany;
    }

    public ArrayList<String> getBookmarkIdList() {
        return bookmarkIdList;
    }

    public void setBookmarkIdList(ArrayList<String> bookmarkIdList) {
        this.bookmarkIdList = bookmarkIdList;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }
}
