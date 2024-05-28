package com.example.mydoctorapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeptViewActivity extends AppCompatActivity {
    private String doctorSpeciality;
    private ProgressDialog progressDialog;
    private AppCompatImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dept_view);

        doctorSpeciality = Objects.requireNonNull(getIntent().getExtras()).getString("DocSpec");
        System.out.println("doctor speciality from DeptView: " + doctorSpeciality);
        LinearLayout cardContainer = findViewById(R.id.card_container);
        LayoutInflater inflater = LayoutInflater.from(this);
        showLoadingPopup();
        fetchDoctors(cardContainer, inflater);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish();
        });
    }
    private void fetchDoctors(LinearLayout cardContainer, LayoutInflater inflater) {
        List<Doctor> doctors = new ArrayList<>();
        DoctorsDB dbHelper = new DoctorsDB(this);
        Cursor cursor = dbHelper.selectDoctorBySpeciality(doctorSpeciality);

        if (cursor != null) {
            try {
                Log.d("DeptViewActivity", "Cursor count: " + cursor.getCount());
                while (cursor.moveToNext()) {
                    String id = cursor.getString(0);
                    String name = cursor.getString(1);
                    String qualification = cursor.getString(2);
                    String speciality = cursor.getString(3);
                    String fee = cursor.getString(4);
                    String work = cursor.getString(5);
                    String experience = cursor.getString(6);
                    Log.d("DeptViewActivity", "Fetched doctor: " + name);
                    Doctor doctor = new Doctor(id, name, qualification, speciality, fee, work, experience);
                    doctors.add(doctor);
                }
            } catch (Exception e) {
                Log.e("DeptViewActivity", "Error reading from cursor", e);
            } finally {
                Log.d("DeptViewActivity", "Cursor closed");
                cursor.close();
            }
        } else {
            Log.e("DeptViewActivity", "Cursor is null");
        }

        // Now you have the list of doctors, you can populate your UI
        populateDoctors(doctors, cardContainer, inflater);

        // Dismiss the loading popup
        dismissLoadingPopup();
    }

    private void populateDoctors(List<Doctor> doctors, LinearLayout cardContainer, LayoutInflater inflater) {
        if (doctors == null || doctors.isEmpty()) {
            Log.e("DeptViewActivity", "No doctors available");
            return;
        }
        System.out.println("In DeptView PopulateDoctors");
        Log.d("DeptViewActivity", "Populating doctors, count: " + doctors.size());
        for (int i = 0; i < doctors.size(); i++) {
            Doctor doctor = doctors.get(i);
            Log.d("DeptViewActivity", "Inflating card for doctor: " + doctor.getName());
            View cardView = inflater.inflate(R.layout.activity_dept_card_view, cardContainer, false);
            TextView doctorName = cardView.findViewById(R.id.tvDoctorName);
            doctorName.setText(doctor.getName());
            TextView doctorQualification = cardView.findViewById(R.id.tvDoctorQualification);
            doctorQualification.setText(doctor.getQualification());
            Button doctorSpeciality = cardView.findViewById(R.id.btnSpecialization);
            doctorSpeciality.setText(doctor.getSpeciality());
            TextView doctorFee = cardView.findViewById(R.id.tvFee);
            doctorFee.setText(doctor.getFee());
            TextView doctorWork = cardView.findViewById(R.id.tvWork);
            doctorWork.setText(doctor.getWork());
            TextView doctorExperience = cardView.findViewById(R.id.tvExperience);
            doctorExperience.setText(doctor.getExperience());
            ImageView doctorImage = cardView.findViewById(R.id.doctorImage);
            String doctorID = doctor.getId();
            System.out.println("DeptViewActivity: " + doctorID);
            //int imageId = getResources().getIdentifier("doctor" + (i + 1), "drawable", getPackageName());
            int resourceID = getResources().getIdentifier(doctorID, "drawable", getPackageName());
            if (resourceID != 0) {
                doctorImage.setImageResource(resourceID);
            } else {
                doctorImage.setImageResource(R.drawable.doctor1);
            }
            cardContainer.addView(cardView);
            MaterialCardView materialCardView = cardView.findViewById(R.id.materialCardView);
            materialCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DeptViewActivity.this, DoctorProfileActivity.class);
                    intent.putExtra("name", doctor.getName());
                    intent.putExtra("qualification", doctor.getQualification());
                    intent.putExtra("speciality", doctor.getSpeciality());
                    intent.putExtra("experience", doctor.getExperience());
                    intent.putExtra("fee", doctor.getFee());
                    intent.putExtra("work", doctor.getWork());
                    intent.putExtra("imageId", resourceID);
                    startActivity(intent);
                }
            });
        }
    }



    private void showLoadingPopup() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissLoadingPopup() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}