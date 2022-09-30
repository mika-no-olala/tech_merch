package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.CreateRequestActivity;
import kz.smrtx.techmerch.adapters.CardAdapterString;
import kz.smrtx.techmerch.adapters.CardAdapterStringAddress;
import kz.smrtx.techmerch.items.entities.Element;
import kz.smrtx.techmerch.items.entities.SalePointItem;
import kz.smrtx.techmerch.items.viewmodels.ChoosePointsViewModel;

public class RCAddressFragment extends Fragment {

    private List<SalePointItem> salePoints = new ArrayList<>();
    private List<SalePointItem> array;
    private EditText toOutlet;
    private ChoosePointsViewModel choosePointsViewModel;

    public static RCAddressFragment getInstance() {
        return new RCAddressFragment();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rc_address, container, false);

        choosePointsViewModel = new ViewModelProvider(this).get(ChoosePointsViewModel.class);
        getList();

        EditText fromOutlet = view.findViewById(R.id.fromOutlet);
        toOutlet = view.findViewById(R.id.toOutlet);

        fromOutlet.setText(Ius.readSharedPreferences(this.getContext(), Ius.LAST_SALE_POINT_ADDRESS));

        toOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        return view;
    }

    private void openDialog() {
        CardAdapterStringAddress adapter = new CardAdapterStringAddress(salePoints);
        array = new ArrayList<>(salePoints);
        Dialog dialog = createDialogList(this.getContext(), adapter);
        SearchView search = dialog.findViewById(R.id.search);

        dialog.show();

        adapter.setOnItemClickListener(new CardAdapterStringAddress.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String address = array.get(position).getName() + " - " + array.get(position).getHouse();
                toOutlet.setText(address);
                ((CreateRequestActivity) requireActivity()).setAddress(address);
                dialog.cancel();
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                array = new ArrayList<>();
                for (SalePointItem s : salePoints) {
                    if (s.getName().toLowerCase().contains(newText.toLowerCase()) ||
                        s.getCode().toLowerCase().contains(newText.toLowerCase()) ||
                        s.getHouse().toLowerCase().contains(newText.toLowerCase()))
                        array.add(s);
                }
                adapter.setAdapter(array);
                return false;
            }
        });
    }

    private void getList() {
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

    private Dialog createDialogList(Context context, CardAdapterStringAddress adapter) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dialog.setContentView(R.layout.dialog_window_list);
        dialog.setCanceledOnTouchOutside(true);

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(dialog.getContext());

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return dialog;
    }
}