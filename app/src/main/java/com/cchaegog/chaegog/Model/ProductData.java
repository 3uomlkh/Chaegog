package com.cchaegog.chaegog.Model;

import java.util.ArrayList;

public class ProductData {
    private String productName;
    private String productCompany;
    private ArrayList<String> bookmarkIdList;

    public ProductData(String name, String company) {
        this.productName = name;
        this.productCompany = company;
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
}
