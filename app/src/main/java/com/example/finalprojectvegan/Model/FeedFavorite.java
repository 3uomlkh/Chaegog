package com.example.finalprojectvegan.Model;

import java.util.ArrayList;
import java.util.Map;

public class FeedFavorite {
    private int favoriteCount;
    private Map<String, Boolean> favoriteUser;
//    private ArrayList<String> favoriteUserList;

    public FeedFavorite(int favoriteCount, Map<String, Boolean> favoriteUser) {
        this.favoriteCount = favoriteCount;
        this.favoriteUser = favoriteUser;
//        this.favoriteUserList = favoriteUserList;
    }

    public FeedFavorite() {

    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int count) {
        this.favoriteCount = favoriteCount;
    }

    public Map<String, Boolean> getFavoriteUser() {
        return favoriteUser;
    }

    public void setFavoriteUser(Map<String, Boolean> favoriteUser) {
        this.favoriteUser = favoriteUser;
    }

    //    public ArrayList<String> getFavoriteUserList() {
//        return favoriteUserList;
//    }
//
//    public void setFavoriteUserList(ArrayList<String> favoriteUserList) {
//        this.favoriteUserList = favoriteUserList;
//    }
}
