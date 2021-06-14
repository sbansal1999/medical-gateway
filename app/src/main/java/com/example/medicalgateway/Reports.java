package com.example.medicalgateway;

public class Reports {
    private String reportDate;
    private String reportURL;

    public Reports() {
        //Required for Firebase
    }

    public Reports(String reportDate, String reportURL) {
        this.reportDate = reportDate;
        this.reportURL = reportURL;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportURL() {
        return reportURL;
    }

    public void setReportURL(String reportURL) {
        this.reportURL = reportURL;
    }
}
