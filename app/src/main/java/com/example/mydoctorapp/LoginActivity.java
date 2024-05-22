package com.example.mydoctorapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText etPhone, etPassword;
    private Button btnLogin, btnForgotPass, btnLoginPhone;
    private CheckBox cbRemMe;
    private PreferenceManager preferenceManager;
    private TextInputLayout loginPassInputLayout;
    private boolean isPasswordVisible = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferenceManager = new PreferenceManager(this);

        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnForgotPass = findViewById(R.id.btnForgotPass);
        btnLoginPhone = findViewById(R.id.btnLoginPhone);
        cbRemMe = findViewById(R.id.cbRemMe);
        loginPassInputLayout = findViewById(R.id.password);

        cbRemMe.setChecked(preferenceManager.getRememberMe());
        if(preferenceManager.getRememberMe()){
            String spPhoneNumber = preferenceManager.getPhoneNumber();
            etPhone.setText(spPhoneNumber);
        }

        loginPassInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Hide password
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPasswordVisible = false;
                } else {
                    // Show password
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isPasswordVisible = true;
                }
                // Move the cursor to the end of the text
                etPassword.setSelection(etPassword.getText().length());
            }
        });

        btnLogin.setOnClickListener(v -> {
            showLoadingPopup();
            processLogin();
        });

        btnLoginPhone.setOnClickListener(v -> {
            finish();
        });
    }

    private void processLogin(){
        String userPhone = etPhone.getText().toString();
        String userPassword = etPassword.getText().toString();
        boolean userRememberMe = cbRemMe.isChecked();

        if(userRememberMe){
            preferenceManager.rememberMe(userRememberMe);
        }

        if (preferenceManager.hasPhoneNumber() && preferenceManager.hasPassword()) {
            String savedPhone = preferenceManager.getPhoneNumber();
            String savedPassword = preferenceManager.getPassword();

            if (userPhone.equals(savedPhone) && userPassword.equals(savedPassword)) {
                dismissLoadingPopup();
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                dismissLoadingPopup();
                Toast.makeText(this, "Invalid phone number or password", Toast.LENGTH_SHORT).show();
            }
        } else {
            new FetchCredentialsTask().execute(userPhone, userPassword);
        }

    }

    private class FetchCredentialsTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String userPhone = params[0];
            String userPassword = params[1];

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(userPhone);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String phone_number = document.getString("phone_number");
                            String password = document.getString("password");

                            if (userPhone.equals(phone_number) && userPassword.equals(password)) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        dismissLoadingPopup();
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        dismissLoadingPopup();
                                        Toast.makeText(LoginActivity.this, "Invalid phone number or password", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    dismissLoadingPopup();
                                    Toast.makeText(LoginActivity.this, "No saved credentials found", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        dismissLoadingPopup();
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

            return null;
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