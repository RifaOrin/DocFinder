package com.example.mydoctorapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class InfoFragment extends Fragment {

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.info_fragment, container, false);

        String feeAmount = getArguments().getString("feeAmount");

        TextView consultationFee = rootView.findViewById(R.id.tvConsultationFee);
        consultationFee.setText(feeAmount);
        // Inflate the layout for this fragment
        return rootView;
    }
}
