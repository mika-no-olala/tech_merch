package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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

public class RCWorkSubtypeFragment extends Fragment {

    private List<Element> workSubtypeList = new ArrayList<>();
    private List<Element> specialList = new ArrayList<>();
    private List<Element> array;
    private EditText specVariant;
    private TextView specVariantText;
    private ElementViewModel elementViewModel;

    public static RCWorkSubtypeFragment getInstance() {
        return new RCWorkSubtypeFragment();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rc_work_subtype, container, false);

        elementViewModel = new ViewModelProvider(this).get(ElementViewModel.class);
        new GetDataAsync(elementViewModel).execute();

        EditText workSubtype = view.findViewById(R.id.workSubtype);
        specVariant = view.findViewById(R.id.specVariant);
        specVariantText = view.findViewById(R.id.specVariantText);

        workSubtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(workSubtype, workSubtypeList, false);
            }
        });

        specVariant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(specVariant, specialList, true);
            }
        });

        return view;
    }

    private void openDialog(EditText editText, List<Element> arrayOriginal, boolean special) {
        CardAdapterString adapter = new CardAdapterString();
        adapter.setAdapterElement(arrayOriginal);
        array = new ArrayList<>(arrayOriginal);
        Dialog dialog = Ius.createDialogList(this.getContext(), adapter, false);
        SearchView search = dialog.findViewById(R.id.search);

        dialog.show();

        adapter.setOnItemClickListener(new CardAdapterString.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String itemName = array.get(position).getName();
                editText.setText(itemName);
                if (!special) {
                    specVariant.setText("");
                    boolean glo = itemName.contains("GLO");
                    ((CreateRequestActivity) requireActivity()).setWorkSubtype(itemName, glo);

                    if (glo) {
                        specVariant.setEnabled(true);
                        specVariantText.setTextColor(getResources().getColor(R.color.main_deep_purple));
                    }
                    else {
                        specVariant.setEnabled(false);
                        specVariantText.setTextColor(getResources().getColor(R.color.light_purple));
                    }
                }
                else
                    ((CreateRequestActivity) requireActivity()).setSpecial(itemName);

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
                for (Element e : arrayOriginal) {
                    if (e.getName().toLowerCase().contains(newText.toLowerCase()))
                        array.add(e);
                }
                adapter.setAdapterElement(array);

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
            workSubtypeList = elementViewModel.getElementList(11);
            specialList = elementViewModel.getElementList(12);
        }
    }
}