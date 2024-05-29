package com.example.mydoctorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DoctorProfileActivity extends AppCompatActivity {
    private ImageView doctorImage;
    private TextView doctorName, doctorQualification, doctorExperience, doctorFee, doctorWork;
    private Button doctorSpeciality, btnNext;
    private AppCompatImageButton backButton;

    private String extraName, extraQualification, designation, extraExperience, feeAmount, work;
    private int extraImageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        doctorImage = findViewById(R.id.doctorImage);
        doctorName = findViewById(R.id.doctorName);
        doctorQualification = findViewById(R.id.doctorQualification);
        doctorSpeciality = findViewById(R.id.doctorSpeciality);
        doctorExperience = findViewById(R.id.tvExperience);
        doctorFee = findViewById(R.id.feeAmount);
        doctorWork = findViewById(R.id.tvHospital);
        btnNext = findViewById(R.id.btnNext);
        backButton = findViewById(R.id.backButton);

        extraImageId = getIntent().getIntExtra("imageId", 1);
        System.out.println("Image ID from Doctor's Profile: " + extraImageId);

        doctorImage.setImageResource(extraImageId);

        extraName = getIntent().getExtras().getString("name");
        doctorName.setText(extraName);

        extraQualification = getIntent().getExtras().getString("qualification");
        doctorQualification.setText(extraQualification);

        designation = getIntent().getExtras().getString("speciality");
        doctorSpeciality.setText(designation);

        extraExperience = getIntent().getExtras().getString("experience");
        doctorExperience.setText(extraExperience);

        feeAmount = getIntent().getExtras().getString("fee");
        doctorFee.setText(feeAmount);

        work = getIntent().getExtras().getString("work");
        doctorWork.setText(work);

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);

        Bundle bundle = new Bundle();
        bundle.putString("feeAmount", feeAmount);
        InfoFragment infoFragment = new InfoFragment();
        infoFragment.setArguments(bundle);

        Bundle bundle1 = new Bundle();
        bundle1.putString("designation", designation);
        bundle1.putString("work", work);
        ExperienceFragment experienceFragment = new ExperienceFragment();
        experienceFragment.setArguments(bundle1);

        viewPagerAdapter.addFragment(infoFragment, "Info");
        viewPagerAdapter.addFragment(experienceFragment, "Experience");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        btnNext.setOnClickListener(v -> {
            Intent i = new Intent(DoctorProfileActivity.this, DoctorOverviewActivity.class);
            i.putExtra("imageId", extraImageId);
            i.putExtra("name", extraName);
            i.putExtra("qualification", extraQualification);
            i.putExtra("speciality", designation);
            i.putExtra("fee", feeAmount);
            startActivity(i);
        });

        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }
}