package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.CreateRequestActivity;
import kz.smrtx.techmerch.activities.StatusesActivity;
import kz.smrtx.techmerch.activities.SessionActivity;
import kz.smrtx.techmerch.items.viewmodels.WarehouseJournalViewModel;
import kz.smrtx.techmerch.utils.Aen;

public class OperationsFragment extends Fragment {

    private String dateStarted;
    private WarehouseJournalViewModel warehouseJournalViewModel;
    private TextView suppliesTitle;

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
        warehouseJournalViewModel = new ViewModelProvider(this).get(WarehouseJournalViewModel.class);

        suppliesTitle = view.findViewById(R.id.suppliesTitle);
        TextView startWork = view.findViewById(R.id.startWorkCheckPoint);
        Button workIsOver = view.findViewById(R.id.workIsOver);
        CardView outlet = view.findViewById(R.id.outlet);
        CardView daily = view.findViewById(R.id.daily);
        CardView statuses = view.findViewById(R.id.statuses);
        CardView report = view.findViewById(R.id.report);
        CardView allReports = view.findViewById(R.id.reportList);
        CardView warehouses = view.findViewById(R.id.warehouses);
        CardView newDeliveries = view.findViewById(R.id.newDeliveries);
        listener.getPageName(getResources().getString(R.string.operations));
        startWork.setText(getResources().getString(R.string.start_work_check_point) + ": " + dateStarted);

        if (roleCode!=Aen.ROLE_TMR)
            outlet.setVisibility(View.GONE);

        if (roleCode!=Aen.ROLE_TECHNIC) {
            report.setVisibility(View.GONE);
            allReports.setVisibility(View.GONE);
        }

        if (roleCode!=Aen.ROLE_MANAGER) {
            newDeliveries.setVisibility(View.GONE);
            warehouses.setVisibility(View.GONE);
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
        warehouses.setOnClickListener(wareView -> ((SessionActivity)requireActivity()).openFragment(WarehousesFragment.getInstance(), true));
        allReports.setOnClickListener(allReportsView -> ((SessionActivity)requireActivity()).openFragment(AllReportsFragment.getInstance(), true));
        newDeliveries.setOnClickListener(wareJournalView -> ((SessionActivity)requireActivity()).openFragment(WarehouseJournalFragment.getInstance(), true));
        workIsOver.setOnClickListener(workIsOverView -> ((SessionActivity)requireActivity()).openDialog());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        new GetSuppliesInfo(this.getContext(), warehouseJournalViewModel).execute();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class GetSuppliesInfo extends AsyncTask<Void, Void, Void> {
        private final WarehouseJournalViewModel warehouseJournalViewModel;
        private final Context context;
        private int quantity;

        public GetSuppliesInfo(Context context, WarehouseJournalViewModel warehouseJournalViewModel) {
            this.context = context;
            this.warehouseJournalViewModel = warehouseJournalViewModel;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            CardView card = ((Activity) context).findViewById(R.id.newDeliveries);

            if (quantity > 0) {
                suppliesTitle.setText(context.getString(R.string.new_delivery) + " (" + quantity + ")");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    card.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.old_linen)));
            }
            else {
                suppliesTitle.setText(context.getString(R.string.new_delivery));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    card.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.white)));
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            quantity = warehouseJournalViewModel.getSuppliesQuantity();
            return null;
        }
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