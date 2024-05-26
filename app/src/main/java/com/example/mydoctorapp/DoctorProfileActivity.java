package com.example.mydoctorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
    private TextView doctorName, doctorQualification;
    private Button doctorSpeciality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        doctorImage = findViewById(R.id.doctorImage);
        doctorName = findViewById(R.id.doctorName);
        doctorQualification = findViewById(R.id.doctorQualification);
        doctorSpeciality = findViewById(R.id.doctorSpeciality);

        int givenImageId = getIntent().getIntExtra("imageId", 1);
        System.out.println("Image ID from Doctor's Profile: " + givenImageId);

        doctorImage.setImageResource(givenImageId);
        doctorName.setText(getIntent().getExtras().getString("name"));
        doctorQualification.setText(getIntent().getExtras().getString("qualification"));
        doctorSpeciality.setText(getIntent().getExtras().getString("speciality"));

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(new InfoFragment(), "Info");
        viewPagerAdapter.addFragment(new ExperienceFragment(), "Experience");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
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