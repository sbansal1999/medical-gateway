package com.example.medicalgateway.datamodels;

public class MedicineInfo {
    private String category;
    private String mfgBy;
    private String name;
    private String photoURL;
    private String price;
    private String quantity;
    private String unit;

    public MedicineInfo() {
        //Required for Firebase
    }

    public MedicineInfo(String category, String mfgBy, String name, String photoURL, String price,
                        String quantity, String unit) {
        this.category = category;
        this.mfgBy = mfgBy;
        this.name = name;
        this.photoURL = photoURL;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMfgBy() {
        return mfgBy;
    }

    public void setMfgBy(String mfgBy) {
        this.mfgBy = mfgBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

