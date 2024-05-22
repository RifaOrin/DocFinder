package com.example.mydoctorapp;

public class Doctor {
    private String name;
    private String qualification;
    private String speciality;
    private String fee;

    public Doctor() {
        // Default constructor required for calls to DataSnapshot.getValue(Doctor.class)
    }

    public Doctor(String name, String qualification, String speciality, String fee) {
        this.name = name;
        this.qualification = qualification;
        this.speciality = speciality;
        this.fee = fee;
    }

    public String getName() {
        return name;
    }

    public String getQualification() {
        return qualification;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getFee() {
        return fee;
    }
}