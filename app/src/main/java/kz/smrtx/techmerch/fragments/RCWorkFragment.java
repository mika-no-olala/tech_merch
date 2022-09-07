package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.fragment.app.Fragment;

import kz.smrtx.techmerch.activities.CreateRequestActivity;
import kz.smrtx.techmerch.R;

public class RCWorkFragment extends Fragment {

    public static RCWorkFragment getInstance() {
        RCWorkFragment fragment = new RCWorkFragment();
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rc_work, container, false);

        CheckBox repair = view.findViewById(R.id.repair);
        CheckBox replace = view.findViewById(R.id.replace);

        repair.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.i("onChecked repair", String.valueOf(b));

                ((CreateRequestActivity)requireActivity()).setRepair(b);
            }
        });

        replace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.i("onChecked replace", String.valueOf(b));

                ((CreateRequestActivity)requireActivity()).setReplace(b);
            }
        });

        return view;
    }
}