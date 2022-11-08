package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.activities.CreateRequestActivity;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.adapters.CardAdapterString;
import kz.smrtx.techmerch.items.entities.Element;
import kz.smrtx.techmerch.items.viewmodels.ElementViewModel;

public class RCWorkFragment extends Fragment {

    private List<Element> originalAdditional;
    private List<Element> additional = new ArrayList<>();
    private ArrayList<String> choice = new ArrayList<>();
    private EditText additionalField;

    public static RCWorkFragment getInstance() {
        RCWorkFragment fragment = new RCWorkFragment();
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rc_work, container, false);

        ElementViewModel elementViewModel = new ViewModelProvider(this).get(ElementViewModel.class);

        CheckBox repair = view.findViewById(R.id.repair);
        CheckBox replace = view.findViewById(R.id.replace);
        additionalField = view.findViewById(R.id.additionalWork);

        new GetDataAsync(elementViewModel).execute();

        repair.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.i("onChecked repair", String.valueOf(b));

                if (!compoundButton.isPressed()) // when u rotate phone, listener calls. What kind of bread is dis?
                    return;
                ((CreateRequestActivity)requireActivity()).setRepair(b);
            }
        });

        replace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.i("onChecked replace", String.valueOf(b));

                if (!compoundButton.isPressed())
                    return;
                ((CreateRequestActivity)requireActivity()).setReplace(b);
            }
        });

        additionalField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        return view;
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
            additional = elementViewModel.getElementList(21);
            originalAdditional = new ArrayList<>(additional);
        }
    }

    @SuppressLint("SetTextI18n")
    private void openDialog() {
        CardAdapterString adapter = new CardAdapterString();
        adapter.setAdapterElement(additional);
        Dialog dialog = Ius.createDialogList(this.getContext(), adapter, true);
        TextView listChoice = dialog.findViewById(R.id.listChoice);
        Button positive = dialog.findViewById(R.id.positive);
        Button negative = dialog.findViewById(R.id.negative);

        if (!choice.isEmpty()) {
            for (String s : choice) {
                if (listChoice.getText().toString().trim().equals(""))
                    listChoice.setText(s);
                else
                    listChoice.setText(listChoice.getText().toString() + ", " + s);
            }
        }

        dialog.show();

        adapter.setOnItemClickListener(new CardAdapterString.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String itemName = additional.get(position).getName();
                if (listChoice.getText().toString().trim().equals(""))
                    listChoice.setText(itemName);
                else
                    listChoice.setText(listChoice.getText().toString() + ", " + itemName);
                choice.add(itemName);
                additional.remove(additional.get(position));
                adapter.notifyDataSetChanged();
            }
        });
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additionalField.setText(listChoice.getText().toString());
                ((CreateRequestActivity) requireActivity()).setAdditional(additionalField.getText().toString());
                dialog.cancel();
            }
        });
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additional = new ArrayList<>(originalAdditional);
                choice.clear();
                additionalField.setText("");
                ((CreateRequestActivity) requireActivity()).clearAdditional();
                dialog.cancel();
            }
        });
    }
}