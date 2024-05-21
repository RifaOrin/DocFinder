package com.example.mydoctorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private EditText etPhone, etPassword;
    private Button btnLogin, btnForgotPass, btnLoginPhone;
    private CheckBox cbRemMe;
    private PreferenceManager preferenceManager;
    private TextInputLayout loginPassInputLayout;
    private boolean isPasswordVisible = false;

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
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid phone number or password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No saved credentials found", Toast.LENGTH_SHORT).show();
        }

    }
}