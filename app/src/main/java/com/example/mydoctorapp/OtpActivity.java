package com.example.mydoctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    String phoneNumber, verificationCode;
    PhoneAuthProvider.ForceResendingToken otpResendingToken;
    Long timeOutSeconds = 60L;
    private EditText etInput1, etInput2, etInput3, etInput4, etInput5, etInput6;
    private TextView otpCodeSent, tvResendOtp;

    private Button btnConfirmOtp;

    private ProgressDialog progressDialog;

    private CountDownTimer countDownTimer;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        phoneNumber = Objects.requireNonNull(getIntent().getExtras()).getString("phone");

        etInput1 = findViewById(R.id.etInput1);
        etInput2 = findViewById(R.id.etInput2);
        etInput3 = findViewById(R.id.etInput3);
        etInput4 = findViewById(R.id.etInput4);
        etInput5 = findViewById(R.id.etInput5);
        etInput6 = findViewById(R.id.etInput6);

        btnConfirmOtp = findViewById(R.id.btnConfirmOtp);
        tvResendOtp = findViewById(R.id.tvResendOtp);

        otpCodeSent = findViewById(R.id.tvOtpCodeSent);
        otpCodeSent.setText("OTP code was sent to: " + phoneNumber);

        setupEditTexts();

        showLoadingPopup();

        sendOtp(phoneNumber, false);

        btnConfirmOtp.setOnClickListener(v -> {
            String i1 = etInput1.getText().toString();
            String i2 = etInput2.getText().toString();
            String i3 = etInput3.getText().toString();
            String i4 = etInput4.getText().toString();
            String i5 = etInput5.getText().toString();
            String i6 = etInput6.getText().toString();
            String userOtp = i1 + i2 + i3 + i4 + i5 + i6;
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, userOtp);
            signIn(credential);
        });

       tvResendOtp.setOnClickListener(v -> {
            sendOtp(phoneNumber, true);
            showLoadingPopup();
        });
    }

    private void disableResendOtp() {
        tvResendOtp.setEnabled(false);
        tvResendOtp.setTextColor(R.color.grey);

        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvResendOtp.setText("Resend OTP (" + millisUntilFinished / 1000 + "s)");
            }

            public void onFinish() {
                tvResendOtp.setEnabled(true);
                tvResendOtp.setTextColor(R.color.dark_green);
                tvResendOtp.setText("Resend OTP");
            }
        }.start();
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

    private void setupEditTexts() {
        etInput1.addTextChangedListener(new GenericTextWatcher(etInput1, etInput2));
        etInput2.addTextChangedListener(new GenericTextWatcher(etInput2, etInput3));
        etInput3.addTextChangedListener(new GenericTextWatcher(etInput3, etInput4));
        etInput4.addTextChangedListener(new GenericTextWatcher(etInput4, etInput5));
        etInput5.addTextChangedListener(new GenericTextWatcher(etInput5, etInput6));
        etInput6.addTextChangedListener(new GenericTextWatcher(etInput6, null));
    }

    private class GenericTextWatcher implements TextWatcher {

        private final EditText currentView;
        private final EditText nextView;

        public GenericTextWatcher(EditText currentView, EditText nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No action needed here
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 1 && nextView != null) {
                nextView.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            // No action needed here
        }
    }

    private void sendOtp(String phoneNumber, boolean isResend){
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(timeOutSeconds, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signIn(phoneAuthCredential);
                                dismissLoadingPopup();

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(getApplicationContext(), "OTP Verification Failed", Toast.LENGTH_LONG).show();
                                dismissLoadingPopup();
                                disableResendOtp();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                otpResendingToken = forceResendingToken;
                                Toast.makeText(getApplicationContext(), "OTP Sent Successfully", Toast.LENGTH_LONG).show();
                                dismissLoadingPopup();
                                disableResendOtp();
                            }
                        });
        if (isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(otpResendingToken).build());
        } else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }

    }

    void signIn(PhoneAuthCredential phoneAuthCredential){
        showLoadingPopup();
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dismissLoadingPopup();
                if(task.isSuccessful()){
                    Intent intent = new Intent(OtpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(), "OTP Verification Failed", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

}