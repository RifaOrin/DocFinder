package com.example.mydoctorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MoreActivity extends AppCompatActivity {
    private AppCompatImageButton backToMain;
    private Button profile, appointment, changePassword, updateProfile, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        backToMain = findViewById(R.id.backButton);
        profile = findViewById(R.id.profile);
        appointment = findViewById(R.id.appointments);
        changePassword = findViewById(R.id.changePassword);
        updateProfile = findViewById(R.id.updateProfile);
        btnLogout = findViewById(R.id.logout);

        backToMain.setOnClickListener(v -> {
            finish();
        });
        profile.setOnClickListener(v -> {
            Intent i = new Intent(MoreActivity.this, PatientProfileActivity.class);
            startActivity(i);
        });
        appointment.setOnClickListener(v -> {
            Intent i = new Intent(MoreActivity.this, AppointmentListActivity.class);
            startActivity(i);
        });
        updateProfile.setOnClickListener(v -> {
            Intent i = new Intent(MoreActivity.this, UpdatePatientProfileActivity.class);
            startActivity(i);
        });

        updateProfile.setOnClickListener(v -> {
            Intent i = new Intent(MoreActivity.this, UpdatePatientProfileActivity.class);
            startActivity(i);
        });
        changePassword.setOnClickListener(v -> {
            Intent i = new Intent(MoreActivity.this, UpdatePasswordActivity.class);
            startActivity(i);
        });
        btnLogout.setOnClickListener(v -> {
            Intent i = new Intent(MoreActivity.this, OtpPhoneActivity.class);
            startActivity(i);
            finish();
        });
    }
}