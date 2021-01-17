package com.example.medicalgateway;

public class PharmacyDataModel
{
    private String Med_name,Price_change,Mfg_change,Qty_change;
    private int img_name;

    public String getMed_name() {
        return Med_name;
    }

    public void setMed_name(String med_name) {
        Med_name = med_name;
    }

    public String getPrice_change() {
        return Price_change;
    }

    public void setPrice_change(String price_change) {
        Price_change = price_change;
    }

    public String getMfg_change() {
        return Mfg_change;
    }

    public void setMfg_change(String mfg_change) {
        Mfg_change = mfg_change;
    }

    public String getQty_change() {
        return Qty_change;
    }

    public void setQty_change(String qty_change) {
        Qty_change = qty_change;
    }

    public int getImg_name() {
        return img_name;
    }

    public void setImg_name(int img_name) {
        this.img_name = img_name;
    }
}
