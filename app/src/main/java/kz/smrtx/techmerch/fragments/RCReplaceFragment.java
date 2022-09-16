package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import kz.smrtx.techmerch.activities.CreateRequestActivity;
import kz.smrtx.techmerch.R;

public class RCReplaceFragment extends Fragment {

    public static RCReplaceFragment getInstance() {
        return new RCReplaceFragment();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rc_replace, container, false);

        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                TextView radioButtonText = view.findViewById(i);
                ((CreateRequestActivity)requireActivity())
                        .setFromOutToOut(radioButtonText.getText().toString().equals(getResources().getString(R.string.from_out_to_out)));
                ((CreateRequestActivity)requireActivity())
                        .setReplacePoint(radioButtonText.getText().toString());
            }
        });

        return view;
    }
}