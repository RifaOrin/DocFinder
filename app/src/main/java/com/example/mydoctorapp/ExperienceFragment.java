package com.example.mydoctorapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ExperienceFragment extends Fragment {

    public ExperienceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.experience_fragment, container, false);
        String designation = getArguments().getString("designation");
        String work = getArguments().getString("work");

        TextView tvWork = rootView.findViewById(R.id.tvWork);
        TextView tvDesignation = rootView.findViewById(R.id.tvDesignation);
        tvWork.setText(work);
        tvDesignation.setText(designation);

        return rootView;
    }
}
