package com.cchaegog.chaegog.Model;

public class UserVeganAllergyInfo {

    private String userAllergy;
    private String similarAllergy;

    public UserVeganAllergyInfo(String userAllergy, String similarAllergy) {
        this.userAllergy = userAllergy;
        this.similarAllergy = similarAllergy;
    }

    public UserVeganAllergyInfo(String userAllergy) {
        this.userAllergy = userAllergy;
    }

    public String getuserAllergy() {
        return userAllergy;
    }

    public void setuserAllergy(String userAllergy) {
        this.userAllergy = userAllergy;
    }

}
