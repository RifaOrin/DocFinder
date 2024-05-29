package com.example.mydoctorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PatientProfileActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private UserInfoDB userInfoDB;
    private PreferenceManager preferenceManager;
    private TextView patientName, patientPhone, patientEmail, patientBirthday, patientGender, patientDistrict, patientDivision;
    private AppCompatImageButton backToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        userInfoDB = new UserInfoDB(this);
        preferenceManager = new PreferenceManager(this);
        populateUserDatabase();

        patientName = findViewById(R.id.patientName);
        patientPhone = findViewById(R.id.patientPhone);
        patientEmail = findViewById(R.id.patientEmail);
        patientBirthday = findViewById(R.id.patientBirthday);
        patientGender = findViewById(R.id.patientGender);
        patientDistrict = findViewById(R.id.patientDistrict);
        patientDivision = findViewById(R.id.patientDivision);

        backToMain = findViewById(R.id.backButton);
        backToMain.setOnClickListener(v -> {
            finish();
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        showLoadingPopup();
        new FetchUserDataTask().execute(preferenceManager.getPhoneNumber());
    }
    private void populateUserDatabase() {
        new PopulateUserDatabaseTask().execute();
    }
    private class PopulateUserDatabaseTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            return userInfoDB.hasUsers();
        }

        @Override
        protected void onPostExecute(Boolean hasUsers) {
            if (!hasUsers) {
                fetchAndInsertUserData();
            }
        }
    }

    private void fetchAndInsertUserData() {
        String phoneNumber = preferenceManager.getPhoneNumber();
        if (phoneNumber.isEmpty()) {
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(phoneNumber).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String firstName = document.getString("first_name");
                    String lastName = document.getString("last_name");
                    String email = document.getString("email");
                    String password = document.getString("password");
                    String dateOfBirth = document.getString("date_of_birth");
                    String gender = document.getString("gender");
                    String division = document.getString("division");
                    String district = document.getString("district");

                    new InsertUserTask().execute(phoneNumber, firstName, lastName, dateOfBirth, gender, division, district, email, password);
                }
            }
        });
    }

    private class InsertUserTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String phoneNumber = params[0];
            String firstName = params[1];
            String lastName = params[2];
            String dateOfBirth = params[3];
            String gender = params[4];
            String division = params[5];
            String district = params[6];
            String email = params[7];
            String password = params[8];

            long localResult = userInfoDB.insertUser(phoneNumber, firstName, lastName, dateOfBirth, gender, division, district, email, password);
            if (localResult != -1) {
                Log.e("MainActivity", "Uesr Insertion Successful");
            }
            return null;
        }
    }

    private class FetchUserDataTask extends AsyncTask<String, Void, Cursor> {
        @Override
        protected Cursor doInBackground(String... params) {
            String phoneNumber = params[0];
            return userInfoDB.selectUser(phoneNumber);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.moveToFirst()) {
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phone_number"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow("date_of_birth"));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                String district = cursor.getString(cursor.getColumnIndexOrThrow("district"));
                String division = cursor.getString(cursor.getColumnIndexOrThrow("division"));
                district = district.trim();

                patientName.setText(firstName + " " + lastName);
                patientPhone.setText(phoneNumber);
                patientEmail.setText(email);
                patientBirthday.setText(dateOfBirth);
                patientGender.setText(gender);
                patientDistrict.setText(district);
                patientDivision.setText(division);
                dismissLoadingPopup();

                cursor.close();
            }
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