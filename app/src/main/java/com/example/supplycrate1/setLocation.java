package com.example.supplycrate1;

public class setLocation {
    private String finaladrress,finalpostalcode,finallocality;
    private Double finallat,finallong;

    public setLocation() {
    }

    public setLocation(String finaladrress, String finalpostalcode, String finallocality, Double finallat, Double finallong) {
        this.finaladrress = finaladrress;
        this.finalpostalcode = finalpostalcode;
        this.finallocality = finallocality;
        this.finallat = finallat;
        this.finallong = finallong;
    }

    public String getFinaladrress() {
        return finaladrress;
    }

    public void setFinaladrress(String finaladrress) {
        this.finaladrress = finaladrress;
    }

    public String getFinalpostalcode() {
        return finalpostalcode;
    }

    public void setFinalpostalcode(String finalpostalcode) {
        this.finalpostalcode = finalpostalcode;
    }

    public String getFinallocality() {
        return finallocality;
    }

    public void setFinallocality(String finallocality) {
        this.finallocality = finallocality;
    }

    public Double getFinallat() {
        return finallat;
    }

    public void setFinallat(Double finallat) {
        this.finallat = finallat;
    }

    public Double getFinallong() {
        return finallong;
    }

    public void setFinallong(Double finallong) {
        this.finallong = finallong;
    }
}
