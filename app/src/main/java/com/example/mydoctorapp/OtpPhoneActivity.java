package com.example.mydoctorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OtpPhoneActivity extends AppCompatActivity {

    EditText etPhone;
    Button btnNext, btnLoginPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_phone);
        termsAndConditions();

        etPhone = findViewById(R.id.etPhone);
        btnNext = findViewById(R.id.btnNext);
        btnLoginPass = findViewById(R.id.btnLoginPass);

        btnNext.setOnClickListener(v -> {
            String phoneNumber = etPhone.getText().toString().trim();
            if (phoneNumber.length() == 10 && phoneNumber.startsWith("1")) {
                String fullPhoneNumber = "+880" + phoneNumber;
                Intent intent = new Intent(OtpPhoneActivity.this, OtpActivity.class);
                intent.putExtra("phone", fullPhoneNumber);
                startActivity(intent);
            } else {
                Toast.makeText(OtpPhoneActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
            }
        });

        btnLoginPass.setOnClickListener(v -> {
            Intent i = new Intent(OtpPhoneActivity.this, LoginActivity.class);
            startActivity(i);
        });
    }

    void termsAndConditions(){
        TextView termsTextView = findViewById(R.id.termsTextView);

        String text = "You agree to our Terms, Conditions and Privacy Policy";
        SpannableString spannableString = new SpannableString(text);

        // Define the positions of the green text
        int termsStart = text.indexOf("Terms");
        int termsEnd = termsStart + "Terms".length();
        int conditionsStart = text.indexOf("Conditions");
        int conditionsEnd = conditionsStart + "Conditions".length();
        int privacyStart = text.indexOf("Privacy Policy");
        int privacyEnd = privacyStart + "Privacy Policy".length();

        // Set the color to green
        int greenColor = getResources().getColor(R.color.green);

        // Apply the color spans
        spannableString.setSpan(new ForegroundColorSpan(greenColor), termsStart, termsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(greenColor), conditionsStart, conditionsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(greenColor), privacyStart, privacyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsTextView.setText(spannableString);

        // Apply the underline spans
        spannableString.setSpan(new UnderlineSpan(), termsStart, termsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), conditionsStart, conditionsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), privacyStart, privacyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    }
}