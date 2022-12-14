package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.CreateRequestActivity;
import kz.smrtx.techmerch.adapters.CardAdapterString;
import kz.smrtx.techmerch.items.entities.SalePointItem;
import kz.smrtx.techmerch.items.entities.Warehouse;
import kz.smrtx.techmerch.items.viewmodels.ChoosePointsViewModel;
import kz.smrtx.techmerch.items.viewmodels.WarehouseViewModel;

public class RCAddressFragment extends Fragment {

    private String event;
    private View view;
    private List<SalePointItem> salePoints = new ArrayList<>();
    private List<SalePointItem> salePointsCopy;
    private List<Warehouse> warehouseList = new ArrayList<>();
    private List<Warehouse> warehouseListCopy;
    private TextView fromTitle;
    private EditText toOutlet;
    private EditText fromOutlet;
    private ChoosePointsViewModel choosePointsViewModel;
    private WarehouseViewModel warehouseViewModel;

    public static RCAddressFragment getInstance(String event) {
        RCAddressFragment fragment = new RCAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putString("EVENT", event);
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rc_address, container, false);

        fromTitle = view.findViewById(R.id.fromTitle);
        fromOutlet = view.findViewById(R.id.fromOutlet);
        toOutlet = view.findViewById(R.id.toOutlet);

        if (getArguments()!=null) {
            event = getArguments().getString("EVENT");
            if (event.equals(getString(R.string.from_out_to_out)))
                prepareForOutToOut();
            else if (event.equals(getString(R.string.to_storage)))
                prepareForMoveToWare();
            else
                prepareForMoveFromWare();
        }

        return view;
    }

    private void openDialogSalePoint() {
        CardAdapterString adapter = new CardAdapterString();
        adapter.setAdapterAddress(salePoints);
        salePointsCopy = new ArrayList<>(salePoints);
        Dialog dialog = Ius.createDialogList(this.getContext(), adapter);
        SearchView search = dialog.findViewById(R.id.search);

        dialog.show();

        adapter.setOnItemClickListener(position -> {
            SalePointItem sp = salePointsCopy.get(position);
            if (sp.getStreet().equals(fromOutlet.getText().toString())) {
                createToast(getString(R.string.error_destination), false);
                return;
            }
            String address = sp.getName() + " - " + sp.getStreet();
            toOutlet.setText(address);
            ((CreateRequestActivity) requireActivity()).setAddress(address, Integer.parseInt(sp.getCode()));
            dialog.cancel();
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                salePointsCopy = new ArrayList<>();
                for (SalePointItem s : salePoints) {
                    if (s.getName().toLowerCase().contains(newText.toLowerCase()) ||
                        s.getCode().toLowerCase().contains(newText.toLowerCase()) ||
                        s.getStreet().toLowerCase().contains(newText.toLowerCase()))
                        salePointsCopy.add(s);
                }
                adapter.setAdapterAddress(salePointsCopy);
                return false;
            }
        });
    }

    private void openDialogWarehouse(boolean toWarehouse) {
        CardAdapterString adapter = new CardAdapterString();
        adapter.setAdapterWarehouseAddress(warehouseList);
        warehouseListCopy = new ArrayList<>(warehouseList);
        Dialog dialog = Ius.createDialogList(this.getContext(), adapter);
        SearchView search = dialog.findViewById(R.id.search);

        dialog.show();

        adapter.setOnItemClickListener(position -> {
            Warehouse w = warehouseListCopy.get(position);
            String address = w.getWAC_WAR_NAME() + ", " + w.getWAC_WAR_ADDRESS() + " - " + w.getWAC_LOC_NAME();
            if (toWarehouse)
                toOutlet.setText(address);
            else
                fromOutlet.setText(address);
            ((CreateRequestActivity) requireActivity()).setAddress(address, w.getWAC_WAR_ID());
            dialog.cancel();
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                warehouseListCopy = new ArrayList<>();
                for (Warehouse w : warehouseList) {
                    if (w.getWAC_WAR_NAME().toLowerCase().contains(newText.toLowerCase()) ||
                            (String.valueOf(w.getWAC_WAR_ID())).contains(newText.toLowerCase()) ||
                            w.getWAC_WAR_ADDRESS().toLowerCase().contains(newText.toLowerCase()) ||
                            w.getWAC_LOC_NAME().toLowerCase().contains(newText.toLowerCase()))
                        warehouseListCopy.add(w);
                }
                adapter.setAdapterWarehouseAddress(warehouseListCopy);
                return false;
            }
        });
    }

    private void getListSalePoint() {
        choosePointsViewModel.getSalePoints().observe(getViewLifecycleOwner(), salePointItems -> {
            if (salePointItems==null) {
                Log.w("getAddresses", "list == null");
                //createToast
                return;
            }
            if (salePointItems.size() <= 0) {
                Log.w("createList", "list size == 0");
                //createToast
                return;
            }
            salePoints = salePointItems;
        });
    }

    private void getListWarehouses() {
        Log.e("sss", "cities - " + Arrays.toString(Ius.getCityArray(this.getContext())));
        warehouseViewModel.getAllWarehousesByCity(Ius.getCityArray(this.getContext())).observe(getViewLifecycleOwner(), warehouses -> {
            if (warehouses==null) {
                Log.w("getWarehouses", "list == null");
                //createToast
                return;
            }
            if (warehouses.size() <= 0) {
                Log.w("getWarehouses", "list size == 0");
                //createToast
                return;
            }
            warehouseList = warehouses;
        });
    }

    private void prepareForOutToOut() {
        choosePointsViewModel = new ViewModelProvider(this).get(ChoosePointsViewModel.class);
        getListSalePoint();
        fromOutlet.setText(Ius.readSharedPreferences(this.getContext(), Ius.LAST_SALE_POINT_ADDRESS));
        toOutlet.setOnClickListener(toOutletView -> openDialogSalePoint());
    }

    private void prepareForMoveToWare() {
        warehouseViewModel = new ViewModelProvider(this).get(WarehouseViewModel.class);
        getListWarehouses();
        fromOutlet.setText(Ius.readSharedPreferences(this.getContext(), Ius.LAST_SALE_POINT_ADDRESS));
        toOutlet.setOnClickListener(toOutletView -> openDialogWarehouse(true));
    }

    private void prepareForMoveFromWare() {
        warehouseViewModel = new ViewModelProvider(this).get(WarehouseViewModel.class);
        getListWarehouses();

        fromOutlet.setEnabled(true);
        toOutlet.setEnabled(false);

        fromTitle.setText(getString(R.string.from_warehouse));
        toOutlet.setText(Ius.readSharedPreferences(this.getContext(), Ius.LAST_SALE_POINT_ADDRESS));
        fromOutlet.setOnClickListener(toOutletView -> openDialogWarehouse(false));
    }

    private void createToast(String text, boolean success) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, (ViewGroup) view.findViewById(R.id.toast));
        Ius.showToast(layout, view.getContext(), text, success);
    }
}