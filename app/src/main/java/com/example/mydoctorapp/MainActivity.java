package com.example.mydoctorapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout cardContainer = findViewById(R.id.linearLayout);
        LayoutInflater inflater = LayoutInflater.from(this);
        showLoadingPopup();
        fetchDoctors(cardContainer, inflater);
    }

    private void fetchDoctors(LinearLayout cardContainer, LayoutInflater inflater) {
        List<Doctor> doctors = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("doctors").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Doctor doctor = document.toObject(Doctor.class);
                    doctors.add(doctor);
                }
                populateDoctors(doctors, cardContainer, inflater);
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
            dismissLoadingPopup();
        });
    }

    private void populateDoctors(List<Doctor> doctors, LinearLayout cardContainer, LayoutInflater inflater) {
        for (int i = 0; i < doctors.size(); i++) {
            Doctor doctor = doctors.get(i);
            View cardView = inflater.inflate(R.layout.doctor_card, cardContainer, false);
            TextView doctorName = cardView.findViewById(R.id.tvDoctorName);
            doctorName.setText(doctor.getName());
            TextView doctorQualification = cardView.findViewById(R.id.tvDoctorQualification);
            doctorQualification.setText(doctor.getQualification());
            Button doctorSpeciality = cardView.findViewById(R.id.btnSpecialization);
            doctorSpeciality.setText(doctor.getSpeciality());
            TextView doctorFee = cardView.findViewById(R.id.tvFee);
            doctorFee.setText(doctor.getFee());
            ImageView doctorImage = cardView.findViewById(R.id.doctorImage);
            int imageId = getResources().getIdentifier("doctor" + (i + 1), "drawable", getPackageName());
            doctorImage.setImageResource(imageId);
            cardContainer.addView(cardView);
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