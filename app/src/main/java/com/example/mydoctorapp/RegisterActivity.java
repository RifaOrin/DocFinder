package com.example.mydoctorapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout passInputLayout, confirmPassInputLayout;
    private EditText firstName, lastName, email, password, confirmPassword;
    private Button btnRegister;
    private AutoCompleteTextView tvDay, tvMonth, tvYear, tvGender, tvDistrict, tvDivision;
    private boolean isPasswordVisible = false;
    private String errMsg = "";
    private PreferenceManager preferenceManager;
    private String phoneNumber;
    private UserInfoDB userInfoDB;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userInfoDB = new UserInfoDB(this);
        db = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(this);

        firstName = findViewById(R.id.etFirstName);
        lastName = findViewById(R.id.etLastName);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        confirmPassword = findViewById(R.id.etConfirmPass);
        btnRegister = findViewById(R.id.btnRegister);
        tvDay = findViewById(R.id.Day);
        tvMonth = findViewById(R.id.Month);
        tvYear = findViewById(R.id.Year);
        tvGender =findViewById(R.id.Gender);
        tvDistrict = findViewById(R.id.District);
        tvDivision = findViewById(R.id.Division);

        passInputLayout = findViewById(R.id.password);
        confirmPassInputLayout = findViewById(R.id.confirmPassword);

        phoneNumber = getIntent().getExtras().getString("phone");
        passInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Hide password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPasswordVisible = false;
                } else {
                    // Show password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isPasswordVisible = true;
                }
                // Move the cursor to the end of the text
                password.setSelection(password.getText().length());
            }
        });

        confirmPassInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Hide password
                    confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPasswordVisible = false;
                } else {
                    // Show password
                    confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isPasswordVisible = true;
                }
                // Move the cursor to the end of the text
                confirmPassword.setSelection(confirmPassword.getText().length());
            }
        });

        setAdapter(tvDay, getDays());
        setAdapter(tvMonth, getMonths());
        setAdapter(tvYear, getYears());
        setAdapter(tvGender, new String[]{"Male", "Female"});
        setAdapter(tvDistrict, getDistricts());
        setAdapter(tvDivision, new String[]{"Barishal", "Chattogram", "Dhaka", "Khulna", "Rajshahi", "Rangpur", "Mymensingh", "Sylhet"});

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void setAdapter(AutoCompleteTextView view, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        view.setAdapter(adapter);
    }

    private String[] getDays() {
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) {
            days[i] = String.valueOf(i + 1);
        }
        return days;
    }

    private String[] getMonths() {
        return new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    }

    // Function to generate an array of years
    private String[] getYears() {
        String[] years = new String[101];
        for (int i = 0; i <= 100; i++) {
            years[i] = String.valueOf(2024 - i);
        }
        return years;
    }

    private String[] getDistricts() {
        List<String> districts = new ArrayList<>();
        try {
            InputStream is = getResources().openRawResource(R.raw.districts);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                districts.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return districts.toArray(new String[0]);
    }

    private void registerUser(){
        String userFirstName = firstName.getText().toString();
        String userLastName = lastName.getText().toString();
        String userDay = tvDay.getText().toString();
        String userMonth = tvMonth.getText().toString();
        String userYear = tvYear.getText().toString();
        String userGender = tvGender.getText().toString();
        String userDistrict = tvDistrict.getText().toString();
        String userDivision = tvDivision.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        String userConfirmPassword = confirmPassword.getText().toString();

        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(userEmail);
        if(!matcher.matches()){
            errMsg += "Invalid Email Address\n";
        }

        if(userPassword.length()<6){
            errMsg+= "Password should be at least six characters\n";
        }

        if(!userPassword.equals(userConfirmPassword)){
            errMsg+= "Password and Confirm Password does not match\n";
        }

        if (errMsg.length()>0){
            showErrorDialog(errMsg);
            return;
        }

        String dateOfBirth = userDay + "/" + userMonth + "/" + userYear;

        new StoreUserDataTask().execute(phoneNumber, userFirstName, userLastName, dateOfBirth, userGender, userDistrict, userDivision, userEmail, userPassword);
    }

    private class StoreUserDataTask extends AsyncTask<String, Void, Integer> {
        private static final int SUCCESS = 1;
        private static final int FAILURE = 0;

        @Override
        protected Integer doInBackground(String... params) {
            String phoneNumber = params[0];
            String firstName = params[1];
            String lastName = params[2];
            String dateOfBirth = params[3];
            String gender = params[4];
            String district = params[5];
            String division = params[6];
            String email = params[7];
            String password = params[8];

            // Insert into local database
            long localResult = userInfoDB.insertUser(phoneNumber, firstName, lastName, dateOfBirth, gender, division, district, email, password);
            preferenceManager.saveUser(phoneNumber, password);

            if (localResult == -1) {
                return FAILURE;
            }

            // Insert into Firestore
            Map<String, Object> userData = new HashMap<>();
            userData.put("phone_number", phoneNumber);
            userData.put("first_name", firstName);
            userData.put("last_name", lastName);
            userData.put("date_of_birth", dateOfBirth);
            userData.put("gender", gender);
            userData.put("district", district);
            userData.put("division", division);
            userData.put("email", email);
            userData.put("password", password);

            try {
                db.collection("users").document(phoneNumber).set(userData);
                return SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                return FAILURE;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == SUCCESS) {
                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_LONG).show();
                preferenceManager.clearUser();
            }
        }
    }

    private void showErrorDialog(String errMsg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errMsg);
        builder.setTitle("Error");
        builder.setCancelable(true);
        builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}