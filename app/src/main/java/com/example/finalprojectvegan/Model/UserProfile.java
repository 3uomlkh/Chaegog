package com.example.finalprojectvegan.Model;

import java.util.ArrayList;

public class UserProfile {
    private String userId;
    private String userEmail;
    private String userPw;
    private ArrayList<String> userVeganReason;
    private String userVeganType;
    private ArrayList<String> userAllergy;
    private String userProfileImg;
    private String userToken;

    public UserProfile() {

    }

    public UserProfile(String userId, String userEmail, String userPw, ArrayList<String> userVeganReason, String userVeganType, ArrayList<String> userAllergy, String userProfileImg, String userToken) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userPw = userPw;
        this.userVeganReason = userVeganReason;
        this.userVeganType = userVeganType;
        this.userAllergy = userAllergy;
        this.userProfileImg = userProfileImg;
        this.userToken = userToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPw() {
        return userPw;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }

    public ArrayList<String> getUserVeganReason() {
        return userVeganReason;
    }

    public void setUserVeganReason(ArrayList<String> userVeganReason) {
        this.userVeganReason = userVeganReason;
    }

    public String getUserVeganType() {
        return userVeganType;
    }

    public void setUserVeganType(String userVeganType) {
        this.userVeganType = userVeganType;
    }

    public ArrayList<String> getUserAllergy() {
        return userAllergy;
    }

    public void setUserAllergy(ArrayList<String> userAllergy) {
        this.userAllergy = userAllergy;
    }

    public String getUserProfileImg() {
        return userProfileImg;
    }

    public void setUserProfileImg(String userProfileImg) {
        this.userProfileImg = userProfileImg;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}