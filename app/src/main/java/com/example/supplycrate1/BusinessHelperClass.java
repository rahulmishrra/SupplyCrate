package com.example.supplycrate1;

public class BusinessHelperClass {
    String bName,Name,Email,Phone,Pass,MerchantImageUrl;

    public BusinessHelperClass() {
    }

    public BusinessHelperClass(String bName, String name, String email, String phone, String pass, String merchantImageUrl) {
        this.bName = bName;
        Name = name;
        Email = email;
        Phone = phone;
        Pass = pass;
        MerchantImageUrl = merchantImageUrl;
    }

    public String getMerchantImageUrl() {
        return MerchantImageUrl;
    }

    public void setMerchantImageUrl(String merchantImageUrl) {
        MerchantImageUrl = merchantImageUrl;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }
}
