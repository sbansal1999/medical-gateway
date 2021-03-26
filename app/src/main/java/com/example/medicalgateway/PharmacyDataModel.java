package com.example.medicalgateway;

public class PharmacyDataModel
{
    private String textMedName,textMedPrice,textMedMfg,textMedQty;
    private int imageMed;

    public String getMed_name() {
        return textMedName;
    }

    public void setMed_name(String med_name) {
        textMedName = med_name;
    }

    public String getPrice_change() {
        return textMedPrice;
    }

    public void setPrice_change(String price_change) {
        textMedPrice = price_change;
    }

    public String getMfg_change() {
        return textMedMfg;
    }

    public void setMfg_change(String mfg_change) {
        textMedMfg = mfg_change;
    }

    public String getQty_change() {
        return textMedQty;
    }

    public void setQty_change(String qty_change) {
        textMedQty = qty_change;
    }

    public int getImg_name() {
        return imageMed;
    }

    public void setImg_name(int img_name) {
        this.imageMed = img_name;
    }
}
