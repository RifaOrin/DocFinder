package com.example.mydoctorapp;

public class Doctor {

    private String id, name, qualification, speciality, fee, work, experience;

    public Doctor() {
        // Default constructor required for calls to DataSnapshot.getValue(Doctor.class)
    }

    public Doctor(String id, String name, String qualification, String speciality, String fee, String work, String experience) {
        this.id = id;
        this.name = name;
        this.qualification = qualification;
        this.speciality = speciality;
        this.fee = fee;
        this.work = work;
        this.experience = experience;
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
    public String getId() {return id;}

    public String getWork() {return work;}
    public String getExperience() {return experience;}
}