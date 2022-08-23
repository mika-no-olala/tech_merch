package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import kz.smrtx.techmerch.R;

public class RCEndingFragment extends Fragment {

    public static RCEndingFragment getInstance() {
        RCEndingFragment fragment = new RCEndingFragment();
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rc_ending, container, false);


        return view;
    }
}