package com.example.medicalgateway;

public class HomeDataModel {
    private String textMedName;
    private int imageMed;

    public String getMed_name() {
        return textMedName;
    }

    public void setMed_name(String med_name) {
        textMedName = med_name;
    }



    public int getImg_name() {
        return imageMed;
    }

    public void setImg_name(int img_name) {
        this.imageMed = img_name;
    }
}
