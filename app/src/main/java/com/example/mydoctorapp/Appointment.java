package com.example.mydoctorapp;

public class Appointment {
    private String patient, doctor, date, time;

    public Appointment() {
        // Default constructor required for Firestore
    }

    public Appointment(String patient, String doctor, String date, String time) {
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
    }

    // Getter methods
    public String getPatient() {
        return patient;
    }

    public String getDoctor() {
        return doctor;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    // Setter methods
    public void setPatient(String patient) {
        this.patient = patient;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
