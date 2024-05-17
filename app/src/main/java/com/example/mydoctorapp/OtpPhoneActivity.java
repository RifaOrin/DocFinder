package com.example.mydoctorapp;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;




public class OtpPhoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_phone);
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