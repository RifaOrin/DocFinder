package com.example.mydoctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DoctorAppointmentActivity extends AppCompatActivity {
    private PreferenceManager preferenceManager;
    private AppCompatImageButton backToOverview;
    private TextView tvPatientName, tvPatientNum;
    private EditText etDate, etTime;
    private Button btnSetAppointment;
    private String appointedDoctorName, patientNumber;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointment);
        preferenceManager = new PreferenceManager(this);
        backToOverview = findViewById(R.id.backButton);
        tvPatientName = findViewById(R.id.tvPatientName);
        tvPatientNum = findViewById(R.id.tvPatientNum);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        btnSetAppointment = findViewById(R.id.btnSetAppointment);

        patientNumber = preferenceManager.getPhoneNumber();
        tvPatientNum.setText(patientNumber);
        appointedDoctorName = getIntent().getExtras().getString("doctorName");

    }

    @Override
    protected void onStart(){
        super.onStart();
        new GetUserNameTask().execute(preferenceManager.getPhoneNumber());
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        backToOverview.setOnClickListener(v -> {
            finish();
        });

        btnSetAppointment.setOnClickListener(v -> {
            addAppointment();
        });
    }

    private void showTimePicker() {
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H) // or TimeFormat.CLOCK_24H
                .build();

        materialTimePicker.show(getSupportFragmentManager(), "time_picker_tag");
        materialTimePicker.addOnPositiveButtonClickListener(dialog -> {
            int selectedHour = materialTimePicker.getHour();
            int selectedMinute = materialTimePicker.getMinute();
            String amPm = materialTimePicker.getHour() >= 12 ? "PM" : "AM";

            if (selectedHour > 12) {
                selectedHour -= 12;
            } else if (selectedHour == 0) {
                selectedHour = 12; // Midnight (12:00 AM)
            }

            // Update your EditText (etTime) with the selected time
            String formattedTime = String.format("%02d:%02d %s", selectedHour, selectedMinute, amPm);
            etTime.setText(formattedTime);
        });

    }

    private void showDatePicker(){
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        datePicker.show(getSupportFragmentManager(), "datePickerTag");
        datePicker.addOnPositiveButtonClickListener(selection -> {
            // Update etDate with the selected date
            etDate.setText(datePicker.getHeaderText());
        });

    }

    private class GetUserNameTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            String phoneNumber = params[0];
            final String[] names = new String[2]; // Array to store first name and last name

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(phoneNumber).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        names[0] = document.getString("first_name");
                        names[1] = document.getString("last_name");
                    }
                }
            });

            try {
                Thread.sleep(2000); // Adjust the sleep time if needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return names;
        }

        @Override
        protected void onPostExecute(String[] names) {
            if (names != null) {
                String fullName = names[0] + " " + names[1];
                tvPatientName.setText(fullName);
            } else {
                System.out.println("DoctorAppointmentActivity: User's name not found.");
            }
        }
    }

    private void addAppointment(){
        showLoadingPopup();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference appointmentRef = db.collection("appointments").document(patientNumber);
        Map<String, Object> appointment = new HashMap<>();
        appointment.put("patient", patientNumber);
        appointment.put("doctor", appointedDoctorName);
        appointment.put("date", etDate.getText().toString());
        appointment.put("time", etTime.getText().toString());
        appointmentRef.set(appointment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dismissLoadingPopup();
                        Toast.makeText(DoctorAppointmentActivity.this, "Appointment Taken", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dismissLoadingPopup();
                        Toast.makeText(DoctorAppointmentActivity.this, "Failed Taking Appointment", Toast.LENGTH_SHORT).show();
                    }
                });

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