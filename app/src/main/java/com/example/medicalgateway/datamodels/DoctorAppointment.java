package com.example.medicalgateway.datamodels;

public class DoctorAppointment {
    private String dateAppoint;
    private String patientID;
    private String problemDesc;
    private boolean appointmentFulfilled;

    public DoctorAppointment() {

    }

    public DoctorAppointment(String dateAppoint, String patientID, String problemDesc, boolean appointmentFulfilled) {
        this.dateAppoint = dateAppoint;
        this.patientID = patientID;
        this.problemDesc = problemDesc;
        this.appointmentFulfilled = appointmentFulfilled;
    }

    public boolean isAppointmentFulfilled() {
        return appointmentFulfilled;
    }

    public void setAppointmentFulfilled(boolean appointmentFulfilled) {
        this.appointmentFulfilled = appointmentFulfilled;
    }

    public String getDateAppoint() {
        return dateAppoint;
    }

    public void setDateAppoint(String dateAppoint) {
        this.dateAppoint = dateAppoint;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getProblemDesc() {
        return problemDesc;
    }

    public void setProblemDesc(String problemDesc) {
        this.problemDesc = problemDesc;
    }
}
