package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.CreateRequestActivity;
import kz.smrtx.techmerch.activities.SessionActivity;
import kz.smrtx.techmerch.adapters.CardAdapterOutlets;
import kz.smrtx.techmerch.adapters.CardAdapterString;
import kz.smrtx.techmerch.items.GetDataAsync;
import kz.smrtx.techmerch.items.entities.Element;
import kz.smrtx.techmerch.items.viewmodels.ElementViewModel;

public class RCEquipmentFragment extends Fragment {

    private List<Element> types = new ArrayList<>();
    private List<Element> subtypes = new ArrayList<>();
    private List<Element> filteredSubtypes;
    private EditText type;
    private EditText subtype;

    public static RCEquipmentFragment getInstance() {
        RCEquipmentFragment fragment = new RCEquipmentFragment();
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rc_equipment, container, false);

        ElementViewModel elementViewModel = new ViewModelProvider(this).get(ElementViewModel.class);

        type = view.findViewById(R.id.equipmentType);
        subtype = view.findViewById(R.id.equipmentSubtype);

        new GetDataAsync(elementViewModel).execute();

        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(type, types, true);
            }
        });

        subtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(subtype, filteredSubtypes, false);
            }
        });

        return view;
    }

    private void openDialog(EditText editText, List<Element> array, boolean equipmentType) {
        CardAdapterString adapter = new CardAdapterString(array);
        Dialog dialog = Ius.createDialogList(this.getContext(), adapter);
        dialog.show();

        adapter.setOnItemClickListener(new CardAdapterString.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                editText.setText(array.get(position).getName());
                if (equipmentType) {
                    subtype.setText("");
                    ((CreateRequestActivity) requireActivity()).setEquipment(type.getText().toString());
                    filterSubtypeList(array.get(position).getId());
                }
                else
                    ((CreateRequestActivity) requireActivity()).setEquipmentSubtype(subtype.getText().toString());

                dialog.cancel();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class GetDataAsync extends AsyncTask<Void, Void, Void> {
        private final ElementViewModel elementViewModel;

        @Override
        protected void onPostExecute(Void aVoid) {}

        public GetDataAsync(ElementViewModel elementViewModel) {
            this.elementViewModel = elementViewModel;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getLists();
            return null;
        }

        @SuppressLint("Range")
        public void getLists() {
            types = elementViewModel.getElementList(1);
            subtypes = elementViewModel.getElementList(2);
            filteredSubtypes = new ArrayList<>(subtypes);
        }
    }

    private void filterSubtypeList(int parentId) {
        filteredSubtypes.clear();
        for (Element e : subtypes) {
            if (e.getSubtypeFrom()==parentId)
                filteredSubtypes.add(e);
        }
    }
}