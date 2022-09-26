package kz.smrtx.techmerch.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kz.smrtx.techmerch.utils.GPSTracker;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.SessionActivity;
import kz.smrtx.techmerch.adapters.CardAdapterOutlets;
import kz.smrtx.techmerch.items.entities.SalePointItem;
import kz.smrtx.techmerch.items.viewmodels.ChoosePointsViewModel;

public class OutletsFragment extends Fragment {

    private TextView doSync;
    private RecyclerView recyclerView;
    private CardAdapterOutlets cardAdapter;
    private ChoosePointsViewModel choosePointsViewModel;
    private List<SalePointItem> outlets = new ArrayList<>();

    private FragmentListener listener;
    public interface FragmentListener {
        void getPageName(String name);
    }

    public static OutletsFragment getInstance(String role) {
        OutletsFragment fragment = new OutletsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("USER_ROLE", role);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outlets, container, false);

        listener.getPageName(getResources().getString(R.string.outlets));
        doSync = view.findViewById(R.id.doSync);
        SearchView search = view.findViewById(R.id.search);
        recyclerView = view.findViewById(R.id.recyclerView);
        choosePointsViewModel = new ViewModelProvider(this).get(ChoosePointsViewModel.class);

//        if (getArguments()!=null) {
//            if (getArguments().get("USER_ROLE").equals("tmr")) {
//                // do smth
//            }
//        }

        createList();

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s))
                    cardAdapter.setOutletList(outlets);
                else {
                    if (s.length() >= 2) {
                        filter(s.toUpperCase());
                    }
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OutletsFragment.FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement onFragmentListener");
        }
    }

    private void setAdapter() {
        RecyclerView.LayoutManager layoutManager;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getActivity());


        GPSTracker gps = new GPSTracker(this.getActivity());
        double lat = 0;
        double lon = 0;
        if (gps.getIsGPSTrackingEnabled()) {
            lat = gps.getLatitude();
            lon = gps.getLongitude();
        }

        cardAdapter = new CardAdapterOutlets(outlets, this.getActivity(), lat, lon);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cardAdapter);

        cardAdapter.setOnItemClickListener(new CardAdapterOutlets.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ((SessionActivity)requireActivity()).openFragment(OutletInformationFragment.getInstance("tmr", outlets.get(position).getCode()), false);
            }
        });
    }

    private void createList() {
        choosePointsViewModel.getSalePoints().observe(getViewLifecycleOwner(), salePointItems -> {
            Log.e("sss", String.valueOf(salePointItems.size()));
            if (salePointItems==null) {
                Log.w("createList", "list == null");
                recyclerView.setVisibility(View.GONE);
                doSync.setVisibility(View.VISIBLE);
            }
            if (salePointItems.size() <= 0) {
                Log.w("createList", "list size == 0");
                recyclerView.setVisibility(View.GONE);
                doSync.setVisibility(View.VISIBLE);
            } else {
                outlets = salePointItems;
                setAdapter();
            }
        });
    }

    private void filter(String input) {
        StringBuilder likeStatement = new StringBuilder("'");
        String[] splitted = input.split("\\s");
        for (String s: splitted){
            likeStatement.append("*").append(s);
        }
        likeStatement.append("*'");

        choosePointsViewModel.getSalePointsByFilter(likeStatement.toString()).observe(this, salePointItems -> {
            cardAdapter.setOutletList(salePointItems);
            cardAdapter.notifyDataSetChanged();
        });
    }
}