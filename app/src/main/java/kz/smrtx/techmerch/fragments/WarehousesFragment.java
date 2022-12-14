package kz.smrtx.techmerch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.adapters.CardAdapterReport;
import kz.smrtx.techmerch.adapters.CardAdapterWarehouses;
import kz.smrtx.techmerch.items.entities.Consumable;
import kz.smrtx.techmerch.items.entities.Report;
import kz.smrtx.techmerch.items.entities.Warehouse;
import kz.smrtx.techmerch.items.viewmodels.ConsumableViewModel;
import kz.smrtx.techmerch.items.viewmodels.WarehouseViewModel;

public class WarehousesFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Warehouse> warehouseList = new ArrayList<>();
    private List<Warehouse> warehouseListCopy = new ArrayList<>();
    private WarehouseViewModel warehouseViewModel;
    private CardAdapterWarehouses cardAdapter;

    private FragmentListener listener;
    public interface FragmentListener {
        void getPageName(String name);
    }

    public static WarehousesFragment getInstance() {
        return new WarehousesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_warehouses, container, false);

        listener.getPageName(getResources().getString(R.string.warehouses));
        warehouseViewModel = new ViewModelProvider(this).get(WarehouseViewModel.class);
        getWarehouses();

        SearchView search = view.findViewById(R.id.search);
        recyclerView = view.findViewById(R.id.recyclerView);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) {
                    warehouseListCopy = new ArrayList<>();
                    for (Warehouse w : warehouseList) {
                        if (w.getWAC_WAR_ADDRESS().toLowerCase().contains(newText.toLowerCase()) ||
                                w.getWAC_CONTENT_NAME().toLowerCase().contains(newText.toLowerCase()) ||
                                w.getWAC_WAR_NAME().toLowerCase().contains(newText.toLowerCase()) ||
                                w.getWAC_LOC_NAME().toLowerCase().contains(newText.toLowerCase())) {
                            warehouseListCopy.add(w);
                        }
                    }
                    cardAdapter.setWarehouseList(warehouseListCopy);
                    return false;
                }
                cardAdapter.setWarehouseList(warehouseList);
                warehouseListCopy = new ArrayList<>(warehouseList);
                return false;
            }
        });

        return view;
    }

    private void getWarehouses() {
        warehouseViewModel.getAllWarehouseContentByCity(Ius.getCityArray(this.getContext())).observe(getViewLifecycleOwner(), w -> {
            warehouseList = w;
            setAdapter();
        });

    }

    private void setAdapter() {
        RecyclerView.LayoutManager layoutManager;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getActivity());

        cardAdapter = new CardAdapterWarehouses(warehouseList, this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cardAdapter);

        cardAdapter.setOnItemClickListener(position -> {

        });
    }

    private void createToast(View view, String text, boolean success) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, view.findViewById(R.id.toast));
        Ius.showToast(layout, this.getContext(), text, success);
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