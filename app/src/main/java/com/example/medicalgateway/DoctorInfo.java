package com.example.medicalgateway;

import java.util.List;

public class DoctorInfo {
    private List<Integer> available;
    private String dob;
    private String doctorID;
    private String emailAddress;
    private String gender;
    private String name;
    private String phone;
    private String photoURL;
    private String residentialAddress;
    private String speciality;
    
    public DoctorInfo() {
        //Required for Firebase
    }

    public DoctorInfo(List<Integer> available, String dob, String doctorID, String emailAddress,
                      String gender, String name, String phone, String photoURL,
                      String residentialAddress, String speciality) {
        this.available = available;
        this.dob = dob;
        this.doctorID = doctorID;
        this.emailAddress = emailAddress;
        this.gender = gender;
        this.name = name;
        this.phone = phone;
        this.photoURL = photoURL;
        this.residentialAddress = residentialAddress;
        this.speciality = speciality;
    }

    public List<Integer> getAvailable() {
        return available;
    }

    public void setAvailable(List<Integer> available) {
        this.available = available;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

}
