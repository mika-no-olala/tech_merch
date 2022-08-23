package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.adapters.CardAdapterOutlets;
import kz.smrtx.techmerch.adapters.CardAdapterString;

public class RCEquipmentFragment extends Fragment {

    RecyclerView recyclerView;
    CardAdapterString adapter;
    ArrayList<String> strings = new ArrayList<>();
    EditText type;
    EditText subtype;

    public static RCEquipmentFragment getInstance() {
        RCEquipmentFragment fragment = new RCEquipmentFragment();
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rc_equipment, container, false);

        type = view.findViewById(R.id.equipmentType);
        subtype = view.findViewById(R.id.equipmentSubtype);

        createList();

        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(type);
            }
        });

        subtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(subtype);
            }
        });

        return view;
    }

    private void openDialog(EditText editText) {
        adapter = new CardAdapterString(strings);

        Dialog dialog = Ius.createDialogList(this.getContext(), adapter);


        dialog.show();

        adapter.setOnItemClickListener(new CardAdapterString.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                editText.setText(strings.get(position));
                dialog.cancel();
            }
        });
    }

    private void createList() {
        strings.add("Напольное оборудование без крепления (BWD)");
        strings.add("Напольное оборудование без крепления (оборудование конкурентов)");
        strings.add("Напольное оборудование с анкерным креплением");
        strings.add("Напольное оборудование с анкерным креплением (оборудование конкурентов), крепление оборудования на стену (Module)");
        strings.add("Напольное оборудование без крепления (BWD)");
        strings.add("Напольное оборудование без крепления (оборудование конкурентов)");
        strings.add("Напольное оборудование с анкерным креплением");
        strings.add("Напольное оборудование с анкерным креплением (оборудование конкурентов), крепление оборудования на стену (Module)");
        strings.add("Напольное оборудование без крепления (BWD)");
        strings.add("Напольное оборудование без крепления (оборудование конкурентов)");
        strings.add("Напольное оборудование с анкерным креплением");
        strings.add("Напольное оборудование с анкерным креплением (оборудование конкурентов), крепление оборудования на стену (Module)");
    }
}