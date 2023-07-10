package com.example.finalprojectvegan.Model;

import java.util.ArrayList;

public class UserProfile {
    private String userId;
    private String userEmail;
    private String userPw;
    private String userVeganReason;
    private String userVeganType;
    private ArrayList<String> userAllergy;
    private String userProfileImg;

    public UserProfile() {

    }

    public UserProfile(String userId, String userEmail, String userPw, String userVeganReason, String userVeganType, ArrayList<String> userAllergy, String userProfileImg) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userPw = userPw;
        this.userVeganReason = userVeganReason;
        this.userVeganType = userVeganType;
        this.userAllergy = userAllergy;
        this.userProfileImg = userProfileImg;
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

    public String getUserVeganReason() {
        return userVeganReason;
    }

    public void setUserVeganReason(String userVeganReason) {
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
}