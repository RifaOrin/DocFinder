package com.example.mydoctorapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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

        MaterialCardView deptMedicine= findViewById(R.id.deptMedicine);
        MaterialCardView deptPediatrics= findViewById(R.id.deptPediatrics);
        MaterialCardView deptDermatology= findViewById(R.id.deptDermatology);
        MaterialCardView deptGastrology= findViewById(R.id.deptGastrology);
        MaterialCardView deptNeurology= findViewById(R.id.deptNeurology);
        MaterialCardView deptOrthopedics= findViewById(R.id.deptOrthopedics);
        MaterialCardView deptCardiology= findViewById(R.id.deptCardiology);
        MaterialCardView deptGynae= findViewById(R.id.deptGynae);

        setupCardClickListener(deptMedicine, "Medicine Specialist");
        setupCardClickListener(deptPediatrics, "Pediatrician");
        setupCardClickListener(deptDermatology, "Dermatologist");
        setupCardClickListener(deptGastrology, "Gastroenterologist");
        setupCardClickListener(deptNeurology, "Neurologist");
        setupCardClickListener(deptOrthopedics, "Orthopedist");
        setupCardClickListener(deptCardiology, "Cardiologist");
        setupCardClickListener(deptGynae, "Gynecologist");

        printAllDoctors();
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
                System.out.println("MainActivity doctors size: " + doctors.size());
                new PopulateDatabaseTask(this).execute(doctors);
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
            dismissLoadingPopup();
        });
    }

    private void populateDoctors(List<Doctor> doctors, LinearLayout cardContainer, LayoutInflater inflater) {
        for (int i = 0; i < 5; i++) {
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
            System.out.println("imageId from MainActivity" + imageId);
            doctorImage.setImageResource(imageId);
            cardContainer.addView(cardView);

            Button btnSeeDoctor = cardView.findViewById(R.id.btnSeeDoctor);
            btnSeeDoctor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, DoctorProfileActivity.class);
                    intent.putExtra("name", doctor.getName());
                    intent.putExtra("qualification", doctor.getQualification());
                    intent.putExtra("speciality", doctor.getSpeciality());
                    intent.putExtra("experience", doctor.getExperience());
                    intent.putExtra("fee", doctor.getFee());
                    intent.putExtra("work", doctor.getWork());
                    intent.putExtra("imageId", imageId);
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

    private void setupCardClickListener(MaterialCardView cardView, String name) {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, DeptViewActivity.class);
                System.out.println("Card name from Main: " + name);
                i.putExtra("DocSpec", name);
                startActivity(i);
            }
        });
    }

    private static class PopulateDatabaseTask extends AsyncTask<List<Doctor>, Void, Void> {
        private final Context context;

        public PopulateDatabaseTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(List<Doctor>... params) {
            List<Doctor> doctors = params[0];
            DoctorsDB dbHelper = new DoctorsDB(context);

            // Check if the database already contains data
            System.out.println("MainActivity PopulateDatabase doctors size: " + doctors.size());
            if (dbHelper.getDoctorCount() == doctors.size()) {
                return null; // No need to insert
            }

            // Insert data into the database
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            for (Doctor doctor : doctors) {
                boolean result = dbHelper.insertDoctor(doctor);
                if(result){
                    System.out.println("MainActivity: Insertion Successful");
                }
                else{System.out.println("MainActivity: Insertion not Successful");}
            }
            dbHelper.close();

            return null;
        }
    }

    private void printAllDoctors() {
        DoctorsDB dbHelper = new DoctorsDB(this);
        Cursor cursor = dbHelper.selectAllDoctors();

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndexOrThrow("ID"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String qualification = cursor.getString(cursor.getColumnIndexOrThrow("qualification"));
                    String speciality = cursor.getString(cursor.getColumnIndexOrThrow("speciality"));
                    String fee = cursor.getString(cursor.getColumnIndexOrThrow("fee"));
                    String work = cursor.getString(cursor.getColumnIndexOrThrow("work"));
                    String experience = cursor.getString(cursor.getColumnIndexOrThrow("experience"));
                    Log.d("DeptViewActivity", "Doctor: " + name + ", Speciality: " + speciality + ", Qualification: " + qualification + ", Fee: " + fee
                            + ", Work: " + work + ", Experience: " + experience);
                }
            } catch (Exception e) {
                Log.e("DeptViewActivity", "Error reading from cursor", e);
            } finally {
                cursor.close();
            }
        } else {
            Log.e("DeptViewActivity", "Cursor is null");
        }
    }


}