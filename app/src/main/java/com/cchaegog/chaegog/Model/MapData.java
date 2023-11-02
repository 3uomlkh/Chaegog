package com.cchaegog.chaegog.Model;

import java.util.ArrayList;

public class MapData {
    private String name;
    private String address;
    private String time;
    private String dayoff;
    private String menu;
    private String phone;
    private String category;
    private String imageUrl;
    private String itemKeyList;
    private ArrayList<String> bookmarkIdList;

    public MapData(String name, String address, String category, String image, String dayoff, String time, String menu, String phone) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.imageUrl = image;
        this.dayoff = dayoff;
        this.time = time;
        this.menu = menu;
        this.phone = phone;
    }
    public MapData(String name, String address, String category, String image) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.imageUrl = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDayoff() {
        return dayoff;
    }

    public void setDayoff(String dayoff) {
        this.dayoff = dayoff;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
