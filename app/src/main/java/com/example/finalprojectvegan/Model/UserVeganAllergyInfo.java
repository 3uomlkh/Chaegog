package com.example.finalprojectvegan.Model;

public class UserVeganAllergyInfo {

    private String userAllergy;
    private String similarAllergy;

    public UserVeganAllergyInfo(String userAllergy, String similarAllergy) {
        this.userAllergy = userAllergy;
        this.similarAllergy = similarAllergy;
    }

    public String getuserAllergy() {
        return userAllergy;
    }

    public void setuserAllergy(String userAllergy) {
        this.userAllergy = userAllergy;
    }

}
