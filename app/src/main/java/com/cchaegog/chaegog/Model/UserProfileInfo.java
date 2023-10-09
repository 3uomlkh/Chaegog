package com.cchaegog.chaegog.Model;

public class UserProfileInfo {

    private String userEmail;
    private String userName;
    private String userCategory;
    private String userAllergy;
    private String userProfileImage;

    public UserProfileInfo(String userEmail, String userName) {
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public String getUserAllergy() {
        return userAllergy;
    }

    public void setUserAllergy(String userAllergy) {
        this.userAllergy = userAllergy;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }
}
