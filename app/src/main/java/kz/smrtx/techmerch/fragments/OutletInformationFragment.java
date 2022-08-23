package kz.smrtx.techmerch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.SessionActivity;
import kz.smrtx.techmerch.adapters.CardAdapterOutlets;
import kz.smrtx.techmerch.items.Outlet;

public class OutletInformationFragment extends Fragment {

    private String outletName = "";
    private FragmentListener listener;
    public interface FragmentListener {
        void getPageName(String name);
    }

    public static OutletInformationFragment getInstance(String role, String outletName) {
        OutletInformationFragment fragment = new OutletInformationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("USER_ROLE", role);
        bundle.putString("OUT_NAME", outletName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outlet_information, container, false);

        listener.getPageName(getResources().getString(R.string.outlet_information));
        Button startWork = view.findViewById(R.id.start);

        if (getArguments()!=null) {
            outletName = getArguments().getString("OUT_NAME");
            if (getArguments().getString("USER_ROLE").equals("tmr")) {
                // do smth
            }
        }

        startWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SessionActivity)requireActivity()).openOperationsOnOutlet(outletName);
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OutletInformationFragment.FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement onFragmentListener");
        }
    }
}