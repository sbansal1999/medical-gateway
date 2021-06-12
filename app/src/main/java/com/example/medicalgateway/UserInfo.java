package com.example.medicalgateway;

public class UserInfo {
    private String name;
    private String phone;
    private String DOB;
    private String emailAddress;
    private String residentialAddress;
    private String patientID;

    public UserInfo() {
    }

    public UserInfo(String name, String phone, String DOB, String emailAddress, String residentialAddress, String patientID) {
        this.name = name;
        this.phone = phone;
        this.DOB = DOB;
        this.emailAddress = emailAddress;
        this.residentialAddress = residentialAddress;
        this.patientID = patientID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

}
