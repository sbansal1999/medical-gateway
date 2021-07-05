package com.example.medicalgateway.datamodels;

public class PatientAppointment {
    private String patientID;
    private String problemDesc;
    private String prefDoctor;
    private String prefDoctorID;
    private String dateAppoint;
    private boolean appointmentFulfilled;

    public PatientAppointment() {
        //Required for Firebase
    }

    public PatientAppointment(String patientID, String problemDesc, String prefDoctor,
                              String prefDoctorID, String dateAppoint, boolean appointmentFulfilled) {
        this.patientID = patientID;
        this.problemDesc = problemDesc;
        this.prefDoctor = prefDoctor;
        this.prefDoctorID = prefDoctorID;
        this.dateAppoint = dateAppoint;
        this.appointmentFulfilled = appointmentFulfilled;
    }

    public String getPrefDoctorID() {
        return prefDoctorID;
    }

    public void setPrefDoctorID(String prefDoctorID) {
        this.prefDoctorID = prefDoctorID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public boolean isAppointmentFulfilled() {
        return appointmentFulfilled;
    }

    public void setAppointmentFulfilled(boolean appointmentFulfilled) {
        this.appointmentFulfilled = appointmentFulfilled;
    }

    public String getProblemDesc() {
        return problemDesc;
    }

    public void setProblemDesc(String problemDesc) {
        this.problemDesc = problemDesc;
    }

    public String getPrefDoctor() {
        return prefDoctor;
    }

    public void setPrefDoctor(String prefDoctor) {
        this.prefDoctor = prefDoctor;
    }

    public String getDateAppoint() {
        return dateAppoint;
    }

    public void setDateAppoint(String dateAppoint) {
        this.dateAppoint = dateAppoint;
    }
}
