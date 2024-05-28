package com.example.mydoctorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.os.Bundle;
import android.widget.Button;

public class DoctorOverviewActivity extends AppCompatActivity {
    private AppCompatImageButton backToDoctorProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_overview);
        backToDoctorProfile = findViewById(R.id.backButton);
        backToDoctorProfile.setOnClickListener(v -> {
            finish();
        });
    }
}