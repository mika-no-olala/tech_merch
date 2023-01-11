package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.adapters.CardAdapterConsumable;
import kz.smrtx.techmerch.adapters.CardAdapterReport;
import kz.smrtx.techmerch.adapters.CardAdapterString;
import kz.smrtx.techmerch.items.entities.Consumable;
import kz.smrtx.techmerch.items.entities.Element;
import kz.smrtx.techmerch.items.entities.Report;
import kz.smrtx.techmerch.items.viewmodels.ConsumableViewModel;
import kz.smrtx.techmerch.items.viewmodels.ElementViewModel;

public class AllReportsFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Report> reports = new ArrayList<>();
    private TextView noReports;
    private CardView card;

    private FragmentListener listener;
    public interface FragmentListener {
        void getPageName(String name);
    }

    public static AllReportsFragment getInstance() {
        return new AllReportsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_reports, container, false);

        listener.getPageName(getResources().getString(R.string.reports));
        recyclerView = view.findViewById(R.id.recyclerView);
        noReports = view.findViewById(R.id.noReports);
        card = view.findViewById(R.id.card);

        getReports();

        return view;
    }

    private void makeReportList(List<Consumable> consumables) {
        if (consumables.isEmpty()) {
            card.setVisibility(View.GONE);
            noReports.setVisibility(View.VISIBLE);
        }
        else {
            card.setVisibility(View.VISIBLE);
            noReports.setVisibility(View.GONE);
        }

        String patternFrom = "yyyy-MM-dd'T'HH:mm:ss";
        String patternTo = "dd.MM.yyyy";

        for (Consumable c : consumables) {
            int position = getPositionInReports(c.getTER_CODE());
            if (position==-1) {

                String created = Ius.remakeDate(c.getTER_CREATED(), patternFrom, "HH:mm dd.MM.yyyy");
                String from = Ius.remakeDate(c.getTER_FROM(), patternFrom, patternTo);
                String to = Ius.remakeDate(c.getTER_TO(), patternFrom, patternTo);

                Report report = new Report(
                        c.getTER_CODE(),
                        getString(R.string.report_for) + " " + from + " - " + to,
                        c.getTER_CONSUMABLE_NAME() + " " + c.getTER_COST() + " тг x" + c.getTER_QUANTITY(),
                        c.getTER_COST() * c.getTER_QUANTITY(),
                        created
                );
                reports.add(report);
            }
            else {
                Report report = reports.get(position);
                reports.get(position).setItem(report.getItem() + "\n" +
                        c.getTER_CONSUMABLE_NAME() + " - " + c.getTER_COST() + "тг x" + c.getTER_QUANTITY());
                reports.get(position).setTotal(report.getTotal() +
                        c.getTER_COST() * c.getTER_QUANTITY());
            }
        }

        setAdapter();
    }

    private int getPositionInReports(String code) {
        for (int i = 0; i < reports.size(); i++) {
            if (reports.get(i).getCode().equals(code))
                return i;
        }
        return -1;
    }

    private void getReports() {
        ConsumableViewModel consumableViewModel = new ViewModelProvider(this).get(ConsumableViewModel.class);
        consumableViewModel.getReports().observe(getViewLifecycleOwner(), this::makeReportList);
    }

    private void setAdapter() {
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        CardAdapterReport cardAdapter = new CardAdapterReport(reports, this.getContext());
        recyclerView.setAdapter(cardAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement onFragmentListener");
        }
    }
}