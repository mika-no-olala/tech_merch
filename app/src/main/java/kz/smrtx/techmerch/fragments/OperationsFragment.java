package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.CreateRequestActivity;
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

        int roleCode = Integer.parseInt(Ius.readSharedPreferences(this.getContext(), Ius.USER_ROLE_CODE));

        TextView startWork = view.findViewById(R.id.startWorkCheckPoint);
        Button workIsOver = view.findViewById(R.id.workIsOver);
        CardView outlet = view.findViewById(R.id.outlet);
        CardView daily = view.findViewById(R.id.daily);
        CardView statuses = view.findViewById(R.id.statuses);
        CardView report = view.findViewById(R.id.report);
        CardView allReports = view.findViewById(R.id.reportList);
        listener.getPageName(getResources().getString(R.string.operations));
        startWork.setText(getResources().getString(R.string.start_work_check_point) + ": " + dateStarted);

        if (roleCode==4)
            outlet.setVisibility(View.GONE);

        if (roleCode!=4) {
            report.setVisibility(View.GONE);
            allReports.setVisibility(View.GONE);
        }

        outlet.setOnClickListener(outletView -> ((SessionActivity)requireActivity()).openFragment(OutletsFragment.getInstance("tmr"), false));

        statuses.setOnClickListener(statusesView -> {
            if (!((SessionActivity)requireActivity()).checkPermissions()) {
                Log.w("openStatusesActivity", "NO PERMISSION");
                return;
            }
            ((SessionActivity)requireActivity()).openActivityStatuses();
        });
        report.setOnClickListener(reportView -> ((SessionActivity)requireActivity()).openFragment(TechnicReportFragment.getInstance(), true));
        allReports.setOnClickListener(allReportsView -> ((SessionActivity)requireActivity()).openFragment(AllReportsFragment.getInstance(), true));
        workIsOver.setOnClickListener(workIsOverView -> ((SessionActivity)requireActivity()).openDialog());
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