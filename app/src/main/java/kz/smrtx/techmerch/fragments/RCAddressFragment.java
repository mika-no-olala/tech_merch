package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import kz.smrtx.techmerch.R;

public class RCAddressFragment extends Fragment {

    public static RCAddressFragment getInstance() {
        RCAddressFragment fragment = new RCAddressFragment();
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rc_address, container, false);


        return view;
    }
}