package com.example.medicalgateway;

public class UserInfo {
    private String name, phone, DOB, password, emailAddress, residentialAddress;

    public UserInfo(String name, String phone, String DOB, String password, String emailAddress, String residentialAddress) {
        this.name = name;
        this.phone = phone;
        this.DOB = DOB;
        this.password = password;
        this.emailAddress = emailAddress;
        this.residentialAddress = residentialAddress;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
