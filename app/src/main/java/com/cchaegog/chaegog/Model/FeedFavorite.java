package com.cchaegog.chaegog.Model;

import java.util.HashMap;
import java.util.Map;

public class FeedFavorite {
    public int favoriteCount;
    public Map<String, Boolean> favorites = new HashMap<>();
//    private ArrayList<String> favoriteUserList;

    public FeedFavorite(int favoriteCount, Map<String, Boolean> favorites) {
        this.favoriteCount = favoriteCount;
        this.favorites = favorites;
//        this.favoriteUserList = favoriteUserList;
    }

    public FeedFavorite() {

    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Map<String, Boolean> getFavorites() {
        return favorites;
    }

    public void setFavorites(Map<String, Boolean> favorites) {
        this.favorites = favorites;
    }

    //    public ArrayList<String> getFavoriteUserList() {
//        return favoriteUserList;
//    }
//
//    public void setFavoriteUserList(ArrayList<String> favoriteUserList) {
//        this.favoriteUserList = favoriteUserList;
//    }
}
