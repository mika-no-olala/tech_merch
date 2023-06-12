package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.RequestStatusesActivity;
import kz.smrtx.techmerch.items.entities.History;
import kz.smrtx.techmerch.items.viewmodels.HistoryViewModel;

public class RSFinishedFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView noRequests;
    private List<History> list;
    private HistoryViewModel historyViewModel;

    private int salePointCode;
    private int userCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rs_finished, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        noRequests = view.findViewById(R.id.noRequests);

        userCode = Integer.parseInt(Ius.readSharedPreferences(getContext(), Ius.USER_CODE));

        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        salePointCode = ((RequestStatusesActivity)requireActivity()).getSalePointCode();

        getFinishedRequests();

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void getFinishedRequests() {
        if (salePointCode!=-1) {
            historyViewModel.getHistoryListWhichAreRelatedToAndFinished(userCode, salePointCode)
                    .observe(getViewLifecycleOwner(), f -> {
                        list = f;
                        ((RequestStatusesActivity)requireActivity()).setAdapterAdvAndFin(recyclerView, noRequests, list);
                    });
        }
        else {
            historyViewModel.getHistoryListWhichAreRelatedToAndFinished(userCode)
                    .observe(getViewLifecycleOwner(), f -> {
                        list = f;
                        ((RequestStatusesActivity)requireActivity()).setAdapterAdvAndFin(recyclerView, noRequests, list);
                    });
        }
    }
}