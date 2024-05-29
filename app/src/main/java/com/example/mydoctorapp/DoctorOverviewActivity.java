package com.example.mydoctorapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DoctorOverviewActivity extends AppCompatActivity {
    private AppCompatImageButton backToDoctorProfile;
    private ImageView doctorImage, appointmentButton;
    private TextView doctorName, doctorQualification, doctorSpeciality, tvPatientNum, tvConsultTaka, tvVatTaka, tvPlatformTaka, tvNetAmountTaka;
    private Button btnPayNow;
    private PreferenceManager preferenceManager;
    private String consultTaka, toSendDoctorName, netAmountString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_overview);
        backToDoctorProfile = findViewById(R.id.backButton);
        doctorImage = findViewById(R.id.doctorImage);
        appointmentButton = findViewById(R.id.tvAppointmentTimeDate);
        doctorName = findViewById(R.id.doctorName);
        doctorQualification = findViewById(R.id.doctorQualification);
        doctorSpeciality = findViewById(R.id.doctorSpeciality);
        tvPatientNum = findViewById(R.id.tvPatientNum);
        tvConsultTaka = findViewById(R.id.tvConsultTaka);
        tvVatTaka = findViewById(R.id.tvVatTaka);
        tvPlatformTaka = findViewById(R.id.tvPlatformTaka);
        tvNetAmountTaka = findViewById(R.id.tvNetAmountTaka);
        btnPayNow = findViewById(R.id.btnPayNow);
        preferenceManager = new PreferenceManager(this);

        int imageID = getIntent().getIntExtra("imageId", 1);
        doctorImage.setImageResource(imageID);

        toSendDoctorName = getIntent().getExtras().getString("name");
        doctorName.setText(toSendDoctorName);
        doctorQualification.setText(getIntent().getExtras().getString("qualification"));
        doctorSpeciality.setText(getIntent().getExtras().getString("speciality"));

        consultTaka = getIntent().getExtras().getString("fee");
        tvConsultTaka.setText(consultTaka);
        tvPatientNum.setText(preferenceManager.getPhoneNumber());

        int taka = Integer.parseInt(consultTaka.replace("Tk.", "").trim());
        int vat = (int) (taka * 0.15);
        String vatString = Integer.toString(vat);
        vatString = "Tk." + vatString;
        tvVatTaka.setText(vatString);
        int netAmount = taka + vat + 20;
        netAmountString = Integer.toString(netAmount);
        netAmountString = "Tk." + netAmountString;
        tvNetAmountTaka.setText(netAmountString);


        backToDoctorProfile.setOnClickListener(v -> {
            finish();
        });

        appointmentButton.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorOverviewActivity.this, DoctorAppointmentActivity.class);
            intent.putExtra("doctorName", toSendDoctorName);
            startActivity(intent);
        });

        btnPayNow.setOnClickListener(v -> {
            showPaymentPopup();
        });

    }
    private void showPaymentPopup() {
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.activity_payment_popup, null);

        TextView amount = popupView.findViewById(R.id.textViewAmount);
        amount.setText(netAmountString);
        EditText bkashNumber = popupView.findViewById(R.id.editTextPhoneNumber);
        Button payNow = popupView.findViewById(R.id.buttonPayNow);
        Button viewAppointments = popupView.findViewById(R.id.popupViewAppointments);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);
        AlertDialog dialog = builder.create();

        payNow.setOnClickListener(v -> {
            String paymentMessage = "Paid " + netAmountString;
            Toast.makeText(this, paymentMessage, Toast.LENGTH_SHORT).show();
        });

        viewAppointments.setOnClickListener(v -> {
            Intent appointmentIntent = new Intent(this, AppointmentListActivity.class);
            startActivity(appointmentIntent);
        });
        dialog.show();
    }
}