package com.example.mydoctorapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AppointmentListActivity extends AppCompatActivity {
    private PreferenceManager preferenceManager;
    private String patientPhoneNumber;
    private ProgressDialog progressDialog;
    private AppCompatImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);
        preferenceManager = new PreferenceManager(this);
        patientPhoneNumber = preferenceManager.getPhoneNumber();
        LinearLayout cardContainer = findViewById(R.id.card_container);
        LayoutInflater inflater = LayoutInflater.from(this);
        fetchAppointments(cardContainer, inflater);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish();
        });
    }
    private void fetchAppointments(LinearLayout cardContainer, LayoutInflater inflater){
        List<Appointment> appointmentList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("appointments").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    System.out.println("Firestore Data for Document ID " + document.getId() + ": " + document.getData());
                    Appointment appointment = document.toObject(Appointment.class);
                    appointmentList.add(appointment);

                    System.out.println("Doctor Name: " + appointment.getDoctor());
                    System.out.println("Appointment Date: " + appointment.getDate());
                    System.out.println("Appointment Time: " + appointment.getTime());
                }
                System.out.println("AppointmentListActivity appointmentList size: " + appointmentList.size());
                populateAppointments(appointmentList, cardContainer, inflater);

            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }

        });
    }
    private void populateAppointments(List<Appointment> appointmentList, LinearLayout cardContainer, LayoutInflater inflater){
        for(int i=0; i<appointmentList.size(); i++){
            System.out.println("Here in PopulateAppointments");
            Appointment appointment = appointmentList.get(i);
            View cardView = inflater.inflate(R.layout.activity_appointment_card, cardContainer, false);
            TextView doctorName = cardView.findViewById(R.id.tvDoctorName);
            System.out.println("Doctor Name: " + appointment.getDoctor());
            doctorName.setText(appointment.getDoctor());
            TextView appointmentDate = cardView.findViewById(R.id.tvDate);
            System.out.println("Appointment Date: " + appointment.getDate());
            appointmentDate.setText(appointment.getDate());
            System.out.println("Appointment Time: " + appointment.getTime());
            TextView appointmentTime = cardView.findViewById(R.id.tvTime);
            appointmentTime.setText(appointment.getTime());
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