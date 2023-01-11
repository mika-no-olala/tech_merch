package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Date;

import kz.smrtx.techmerch.activities.CreateRequestActivity;
import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;

public class RCTypeFragment extends Fragment {

    public static RCTypeFragment getInstance() {
        RCTypeFragment fragment = new RCTypeFragment();
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rc_type, container, false);

        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        TextView dateOfCreation = view.findViewById(R.id.dateOfCreation);
        TextView deadline = view.findViewById(R.id.deadline);

        dateOfCreation.setText(
                dateOfCreation.getText().toString() + ": "
                        + Ius.getDateByFormat(new Date(), "dd.MM.yyyy")
        );
        deadline.setText(
                deadline.getText().toString() + ": "
                        + Ius.getDateByFormat(Ius.plusDaysToDate(new Date()), "dd.MM.yyyy, EEEE")
        );

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                TextView radioButtonText = view.findViewById(i);
                Log.i("onChecked guarantee", String.valueOf(radioButtonText.getText().toString().equals(getResources().getString(R.string.guarantee))));
                ((CreateRequestActivity)requireActivity())
                        .setType(radioButtonText.getText().toString().equals(getResources().getString(R.string.guarantee)));
            }
        });

        return view;
    }
}