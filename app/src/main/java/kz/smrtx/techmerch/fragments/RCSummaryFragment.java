package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import kz.smrtx.techmerch.R;

public class RCSummaryFragment extends Fragment {

    public static RCSummaryFragment getInstance() {
        return new RCSummaryFragment();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rc_empty, container, false);


        return view;
    }
}