package kz.smrtx.techmerch.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.RequestStatusesActivity;
import kz.smrtx.techmerch.adapters.CardAdapterRequests;
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.viewmodels.RequestViewModel;
import kz.smrtx.techmerch.utils.Aen;

public class RSWaitingFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView noRequests;
    private List<Request> waitingList;
    private RequestViewModel requestViewModel;

    private int salePointCode;
    private int userCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rs_waiting, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        noRequests = view.findViewById(R.id.noRequests);

        userCode = Integer.parseInt(Ius.readSharedPreferences(getContext(), Ius.USER_CODE));

        requestViewModel = new ViewModelProvider(this).get(RequestViewModel.class);
        salePointCode = ((RequestStatusesActivity)requireActivity()).getSalePointCode();

        getWaitingRequests();

        return view;
    }

    private void getWaitingRequests() {
        Log.i("gettingRequests from", String.valueOf(userCode));

        if (salePointCode!=-1) {
            requestViewModel.getRequestsByAppointed(userCode, salePointCode)
                    .observe(getViewLifecycleOwner(), w -> {
                        if (w!=null) {
                            waitingList = w;
                            ((RequestStatusesActivity)requireActivity()).setAdapterWaiting(recyclerView, noRequests, waitingList);
                        }
                        // TODO: set tab name, optimize all 3 methods to one
                    });
        }
        else {
            requestViewModel.getRequestsByAppointed(userCode)
                    .observe(getViewLifecycleOwner(), w -> {
                        if (w!=null) {
                            waitingList = w;
                            ((RequestStatusesActivity)requireActivity()).setAdapterWaiting(recyclerView, noRequests, waitingList);
                        }
                    });
        }
    }

}