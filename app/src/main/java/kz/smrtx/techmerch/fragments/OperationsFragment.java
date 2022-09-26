package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.StatusesActivity;
import kz.smrtx.techmerch.activities.SessionActivity;

public class OperationsFragment extends Fragment {

    private String dateStarted;

    private FragmentListener listener;
    public interface FragmentListener {
        void getPageName(String name);
    }

    public static OperationsFragment getInstance(String dateStarted) {
        OperationsFragment fragment = new OperationsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("DATE_STARTED", dateStarted);
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operations, container, false);

        if (getArguments()!=null) {
            dateStarted = getArguments().getString("DATE_STARTED");
        }

        TextView startWork = view.findViewById(R.id.startWorkCheckPoint);
        Button workIsOver = view.findViewById(R.id.workIsOver);
        CardView outlet = view.findViewById(R.id.outlet);
        CardView statuses = view.findViewById(R.id.statuses);
        listener.getPageName(getResources().getString(R.string.operations));
        startWork.setText(getResources().getString(R.string.start_work_check_point) + ": " + dateStarted);

        if (Ius.readSharedPreferences(this.getContext(), Ius.USER_ROLE_CODE).equals("4"))
            outlet.setVisibility(View.GONE);

        outlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SessionActivity)requireActivity()).openFragment(OutletsFragment.getInstance("tmr"), false);
            }
        });
        statuses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SessionActivity)requireActivity()).openActivityStatuses();
            }
        });
        workIsOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SessionActivity)requireActivity()).openDialog();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement onFragmentListener");
        }
    }
}