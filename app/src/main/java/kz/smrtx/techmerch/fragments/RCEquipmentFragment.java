package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.CreateRequestActivity;
import kz.smrtx.techmerch.adapters.CardAdapterString;
import kz.smrtx.techmerch.items.entities.Element;
import kz.smrtx.techmerch.items.viewmodels.ElementViewModel;

public class RCEquipmentFragment extends Fragment {

    private List<Element> types = new ArrayList<>();
    private List<Element> subtypes = new ArrayList<>();
    private List<Element> filteredSubtypes;
    private List<Element> array;
    private EditText type;
    private EditText subtype;

    public static RCEquipmentFragment getInstance() {
        return new RCEquipmentFragment();
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

        type.setOnClickListener(view1 -> openDialog(type, types, true));

        subtype.setOnClickListener(view12 -> openDialog(subtype, filteredSubtypes, false));

        return view;
    }

    private void openDialog(EditText editText, List<Element> originalArray, boolean equipmentType) {
        CardAdapterString adapter = new CardAdapterString(originalArray);
        array = new ArrayList<>(originalArray);
        Dialog dialog = Ius.createDialogList(this.getContext(), adapter, false);

        SearchView search = dialog.findViewById(R.id.search);
        dialog.show();

        adapter.setOnItemClickListener(position -> {
            editText.setText(array.get(position).getName());
            if (equipmentType) {
                subtype.setText("");
                ((CreateRequestActivity) requireActivity()).setEquipment(type.getText().toString());
                filterSubtypeList(array.get(position).getId());
            }
            else {
                ((CreateRequestActivity) requireActivity()).setEquipmentSubtype(subtype.getText().toString());
                if (Ius.isEmpty(type)) {
                    filterSubtypeList(array.get(position).getSubtypeFrom());

                    for (Element e : types) {
                        if (e.getId() == array.get(position).getSubtypeFrom()) {
                            type.setText(e.getName());
                            ((CreateRequestActivity) requireActivity()).setEquipment(e.getName());
                            break;
                        }
                    }
                }
            }

            dialog.cancel();
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                array = new ArrayList<>();
                for (Element e : originalArray) {
                    if (e.getName().toLowerCase().contains(s.toLowerCase()))
                        array.add(e);
                }
                adapter.setAdapter(array);

                return false;
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