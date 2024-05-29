package com.example.mydoctorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PaymentPopupActivity extends AppCompatActivity {
    private EditText bkashNumber;
    private TextView amount;
    private Button payNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_popup);

        bkashNumber = findViewById(R.id.editTextPhoneNumber);
        amount = findViewById(R.id.textViewAmount);
        payNow = findViewById(R.id.buttonPayNow);
    }
}