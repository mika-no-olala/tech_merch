package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.adapters.CardAdapterConsumable;
import kz.smrtx.techmerch.adapters.CardAdapterString;
import kz.smrtx.techmerch.items.entities.Consumable;
import kz.smrtx.techmerch.items.entities.Element;
import kz.smrtx.techmerch.items.viewmodels.ConsumableViewModel;
import kz.smrtx.techmerch.items.viewmodels.ElementViewModel;

public class TechnicReportFragment extends Fragment {

    private final Calendar calendar = Calendar.getInstance();
    private boolean fromFieldChosen;
    private final List<Consumable> consumables = new ArrayList<>();
    private List<Element> elements = new ArrayList<>();
    private List<Element> elementsFiltered = new ArrayList<>(elements);
    private final List<String> elementsStr = new ArrayList<>();
    private boolean heCanDoItHimself = false;
    private int fullCostInt = 0;
    private int totalInt = 0;
    private Date now;
    private ConsumableViewModel consumableViewModel;

    private RecyclerView recyclerView;
    private CardAdapterConsumable cardAdapter;
    private View totalList;
    private Button send;
    private AutoCompleteTextView consumable;
    private EditText cost;
    private EditText quantity;
    private EditText from;
    private EditText to;
    private TextView fullCost;
    private TextView total;

    private FragmentListener listener;
    public interface FragmentListener {
        void getPageName(String name);
    }

    public static TechnicReportFragment getInstance() {
        return new TechnicReportFragment();
    }

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_technic_report, container, false);

        listener.getPageName(getResources().getString(R.string.report));

        ElementViewModel elementViewModel = new ViewModelProvider(this).get(ElementViewModel.class);
        consumableViewModel = new ViewModelProvider(this).get(ConsumableViewModel.class);
        new GetDataAsync(elementViewModel).execute();

        initializeItems(view);
        CheckBox iDoItMyself = view.findViewById(R.id.iDoItMyself);
        Button add = view.findViewById(R.id.add);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, elementsStr);
        consumable.setAdapter(adapter);
        setAdapter();

        iDoItMyself.setOnCheckedChangeListener((compoundButton, b) -> {
            heCanDoItHimself = b;
            setFieldAccess();
        });

        consumable.setOnClickListener(view1 -> {
            if (heCanDoItHimself) {
                return;
            }

            openDialog();
        });

        add.setOnClickListener(view12 -> {
            if (consumable.getText()==null || consumable.getText().length()==0) {
                createToast(view, getResources().getString(R.string.fill_field), false);
                return;
            }

            if (cost.getText()==null || cost.getText().length()==0) {
                createToast(view, getResources().getString(R.string.fill_field), false);
                return;
            }

            if (quantity.getText()==null || quantity.getText().length()==0)
                quantity.setText("1");

            if (consumables.isEmpty()) {
                totalList.setVisibility(View.VISIBLE);
                send.setVisibility(View.VISIBLE);
            }
            addNewRow();
        });

        send.setOnClickListener(viewSend -> makeReport(view));

        return view;
    }

    private void setFieldAccess() {
        if (heCanDoItHimself) {
            consumable.setCursorVisible(true);
            consumable.setFocusable(true);
            consumable.setFocusableInTouchMode(true);
        }
        else {
            consumable.setCursorVisible(false);
            consumable.setFocusable(false);
        }
    }

    @SuppressLint("SetTextI18n")
    private void changeFullCost() {
        int currentCost = 0;
        int currentQuantity = 1;

        if (cost.getText()!=null && cost.getText().length()>0)
            currentCost = Integer.parseInt(cost.getText().toString().trim());
        if (quantity.getText()!=null && quantity.getText().length()>0)
            currentQuantity = Integer.parseInt(quantity.getText().toString().trim());

        fullCostInt = currentCost * currentQuantity;
        fullCost.setText(getResources().getString(R.string.full_cost) + ": " + fullCostInt + "тг");
    }

    @SuppressLint("SetTextI18n")
    private void addNewRow() {
        String consName = this.consumable.getText().toString().trim();

        int consId = 0;
        for (Element e : elements) {
            if (e.getName().equals(consName)) {
                consId = e.getId();
                break;
            }
        }

        int cost = Integer.parseInt(this.cost.getText().toString().trim());
        int quantity = Integer.parseInt(this.quantity.getText().toString().trim());

        Consumable consumable = new Consumable(consName, consId, cost, quantity);
        consumables.add(consumable);
        totalInt = totalInt + fullCostInt;
        cardAdapter.setAdapter(consumables);

        total.setText(getResources().getString(R.string.total) + ": " + totalInt + "тг");
        resetFields();
    }

    private void resetFields() {
        consumable.setText("");
        cost.setText("");
        quantity.setText("");
        fullCost.setText("");
        fullCostInt = 0;
    }

    private void makeReport(View view) {
        if (consumables.isEmpty()) {
            createToast(view, getResources().getString(R.string.report_empty), false);
            return;
        }

        now = new Date();

        if (!isIntervalCorrect(view))
            return;

        String code = Ius.generateUniqueCode(this.getContext(), "c");
        int userCode = Integer.parseInt(Ius.readSharedPreferences(this.getContext(), Ius.USER_CODE));
        String userName = Ius.readSharedPreferences(this.getContext(), Ius.USER_NAME);
        String date = Ius.getDateByFormat(now, "dd.MM.yyyy HH:mm:ss");

        for (Consumable c : consumables) {
            c.setTER_CODE(code);
            c.setTER_USE_CODE(userCode);
            c.setTER_USE_NAME(userName);
            c.setTER_CREATED(date);
            c.setTER_FROM(from.getText().toString());
            c.setTER_TO(to.getText().toString());
            c.setNES_TO_UPDATE("yes");
        }

        consumableViewModel.insertReport(consumables);
        createToast(view, getResources().getString(R.string.report_send), true);
        requireActivity().onBackPressed();
    }

    private boolean isIntervalCorrect(View view) {
        if (Ius.isEmpty(from)) {
            createToast(view, getString(R.string.fill_time_interval), false);
            return false;
        }

        if (Ius.isEmpty(to)) {
            createToast(view, getString(R.string.fill_time_interval), false);
            return false;
        }

        Date dateTo = Ius.getDateFromString(to.getText().toString(), "dd.MM.yyyy");
        Date dateFrom = Ius.getDateFromString(from.getText().toString(), "dd.MM.yyyy");
        long difference = Ius.getDifferenceBetweenDates(dateFrom, dateTo, "d");

        if (difference<0) {
            createToast(view, getString(R.string.time_interval_negative), false);
            return false;
        }

        if (difference>31) {
            createToast(view, getString(R.string.time_interval_too_big), false);
            return false;
        }

        difference = Ius.getDifferenceBetweenDates(now, dateTo, "d");

        if (difference>0) {
            createToast(view, getString(R.string.time_interval_time_walker), false);
            return false;
        }

        if (difference<0)
            difference = difference * -1;

        if (difference > 90) {
            createToast(view, getString(R.string.time_interval_non_actual), false);
            return false;
        }

        return true;
    }

    private void openDialog() {
        CardAdapterString adapter = new CardAdapterString();
        adapter.setAdapterElement(elements);
        elementsFiltered = new ArrayList<>(elements);
        Dialog dialog = Ius.createDialogList(this.getContext(), adapter, false);

        SearchView search = dialog.findViewById(R.id.search);
        dialog.show();

        adapter.setOnItemClickListener(position -> {
            consumable.setText(elementsFiltered.get(position).getName());
            dialog.cancel();
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                elementsFiltered = new ArrayList<>();
                for (Element e : elements) {
                    if (e.getName().toLowerCase().contains(s.toLowerCase()))
                        elementsFiltered.add(e);
                }
                adapter.setAdapterElement(elementsFiltered);

                return false;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setAdapter() {
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        cardAdapter = new CardAdapterConsumable(consumables, this.getContext());
        recyclerView.setAdapter(cardAdapter);

        cardAdapter.setOnItemClickListener(new CardAdapterConsumable.onItemClickListener() {
            @Override
            public void onItemClick(int position) {}

            @Override
            public void onDeleteClick(int position) {
                Consumable c = consumables.get(position);

                totalInt = totalInt - c.getTER_COST() * c.getTER_QUANTITY();
                total.setText(getResources().getString(R.string.total) + ": " + totalInt + "тг");
                consumables.remove(c);
                cardAdapter.setAdapter(consumables);

                if (consumables.isEmpty()) {
                    send.setVisibility(View.GONE);
                    totalList.setVisibility(View.GONE);
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
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
            elements = elementViewModel.getElementList(50);
            for (Element e : elements)
                elementsStr.add(e.getName());
            return null;
        }
    }

    private void createToast(View view, String text, boolean success) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, view.findViewById(R.id.toast));
        Ius.showToast(layout, this.getContext(), text, success);
    }

    private void initializeItems(View view) {
        consumable = view.findViewById(R.id.consumable);
        cost = view.findViewById(R.id.cost);
        quantity = view.findViewById(R.id.quantity);
        from = view.findViewById(R.id.from);
        to = view.findViewById(R.id.to);
        fullCost = view.findViewById(R.id.fullCost);
        total = view.findViewById(R.id.total);
        send = view.findViewById(R.id.send);
        totalList = view.findViewById(R.id.totalList);
        recyclerView = view.findViewById(R.id.recyclerView);

        DatePickerDialog.OnDateSetListener date = (datePicker, i, i1, i2) -> {
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH, i1);
            calendar.set(Calendar.DAY_OF_MONTH, i2);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            if (fromFieldChosen)
                from.setText(simpleDateFormat.format(calendar.getTime()));
            else
                to.setText(simpleDateFormat.format(calendar.getTime()));
        };

        from.setOnClickListener(from -> {
            fromFieldChosen = true;
            new DatePickerDialog(this.getContext(), R.style.DialogTheme, date, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        to.setOnClickListener(to -> {
            fromFieldChosen = false;
            new DatePickerDialog(this.getContext(), R.style.DialogTheme, date, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });


        cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                changeFullCost();
            }
        });

        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                changeFullCost();
            }
        });
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