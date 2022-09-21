package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.SessionActivity;
import kz.smrtx.techmerch.adapters.CardAdapterOutlets;
import kz.smrtx.techmerch.adapters.CardAdapterRequests;
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.viewmodels.RequestViewModel;
import kz.smrtx.techmerch.utils.GPSTracker;

public class RSStatusesFragment extends Fragment {

    private CardAdapterRequests cardAdapter;
    private RecyclerView waitingRecycler;
    private RecyclerView advancingRecycler;
    private CardView waitingView;
    private CardView advancingView;
    private final List<Request> waitingList = new ArrayList<>();
    private final List<Request> advancingList = new ArrayList<>();
    private RequestViewModel requestViewModel;
    private boolean waitingListIsOpen = false;
    private boolean advancingListIsOpen = false;

    private FragmentListener listener;
    public interface FragmentListener {
        void getPageName(String name);
    }

    public static RSStatusesFragment getInstance() {
        return new RSStatusesFragment();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rs_statuses, container, false);

        listener.getPageName(getResources().getString(R.string.requests_statuses));

        TextView noRequestsW = view.findViewById(R.id.noRequestsW);
        TextView noRequestsA = view.findViewById(R.id.noRequestsA);
        waitingRecycler = view.findViewById(R.id.waitingRequestsRecycler);
        advancingRecycler = view.findViewById(R.id.advancingRequestsRecycler);

        requestViewModel = new ViewModelProvider(this).get(RequestViewModel.class);
        getWaitingRequests();
        setAdapter(waitingRecycler, waitingList, noRequestsW);

        CardView waitingButton = view.findViewById(R.id.waitingRequests);
        CardView advancingButton = view.findViewById(R.id.advancingRequests);
        ImageView waitingArrowIcon = view.findViewById(R.id.waitingArrowIcon);
        ImageView advancingArrowIcon = view.findViewById(R.id.advancingArrowIcon);
        waitingView = view.findViewById(R.id.waitingView);
        advancingView = view.findViewById(R.id.advancingView);

        waitingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (waitingListIsOpen) {
                    waitingArrowIcon.setRotation(180);
                    waitingView.setVisibility(View.GONE);
                    waitingListIsOpen = false;
                }
                else {
                    waitingArrowIcon.setRotation(-90);
                    waitingView.setVisibility(View.VISIBLE);
                    waitingListIsOpen = true;
                }
            }
        });

        advancingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (advancingListIsOpen) {
                    advancingArrowIcon.setRotation(180);
                    advancingView.setVisibility(View.GONE);
                    advancingListIsOpen = false;
                }
                else {
                    advancingArrowIcon.setRotation(-90);
                    advancingView.setVisibility(View.VISIBLE);
                    advancingListIsOpen = true;
                }
            }
        });

        return view;
    }

    private void getWaitingRequests() {
        int userCode = Integer.parseInt(Ius.readSharedPreferences(this.getContext(), Ius.USER_CODE));
        Log.i("gettingWRequests from", String.valueOf(userCode));
        requestViewModel.getRequestsByAppointed(userCode)
            .observe(getViewLifecycleOwner(), waitingList::addAll);

        Log.i("WRequests size", String.valueOf(waitingList.size()));
    }

    private void setAdapter(RecyclerView recyclerView, List<Request> list, TextView noRequests) {
        if (list.isEmpty()) {
            noRequests.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }

        RecyclerView.LayoutManager layoutManager;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        cardAdapter = new CardAdapterRequests(list, this.getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cardAdapter);

        cardAdapter.setOnItemClickListener(new CardAdapterRequests.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // call activity
            }
        });
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