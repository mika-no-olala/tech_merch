package kz.smrtx.techmerch.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.SessionActivity;
import kz.smrtx.techmerch.adapters.CardAdapterOutlets;
import kz.smrtx.techmerch.items.entities.Outlet;

public class OutletsFragment extends Fragment {

    private RecyclerView recyclerView;
    private CardAdapterOutlets cardAdapter;
    private final ArrayList<Outlet> outlets = new ArrayList<>();

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
        recyclerView = view.findViewById(R.id.recyclerView);

        if (getArguments()!=null) {
            if (getArguments().get("USER_ROLE").equals("tmr")) {
                // do smth
            }
        }

        createList();
        setAdapter();

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
        layoutManager = new LinearLayoutManager(this.getContext());
        cardAdapter = new CardAdapterOutlets(outlets);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cardAdapter);

        cardAdapter.setOnItemClickListener(new CardAdapterOutlets.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ((SessionActivity)requireActivity()).openOutletInformation(outlets.get(position).getOUT_NAME());
            }
        });
    }

    private void createList() {
        outlets.add(new Outlet("ALA00015528", "Лимон", "ул. Басенова, 10 к3", 2, 2.8f));
        outlets.add(new Outlet("ALA00015528", "Лимон", "ул. Басенова, 10 к3", 2, 2.8f));
        outlets.add(new Outlet("ALA00015528", "Евразиан Фудс Корпорэйшн", "ул. Фурманова 117", 0, 4.8f));
        outlets.add(new Outlet("ALA00015528", "Евразиан Фудс Корпорэйшн", "ул. Фурманова 117", 0, 4.8f));
        outlets.add(new Outlet("ALA00015528", "Евразиан Фудс Корпорэйшн", "ул. Фурманова 117", 0, 4.8f));
    }
}