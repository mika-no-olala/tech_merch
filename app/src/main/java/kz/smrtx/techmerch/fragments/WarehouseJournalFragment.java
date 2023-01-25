package kz.smrtx.techmerch.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.SessionActivity;
import kz.smrtx.techmerch.adapters.CardAdapterJournal;
import kz.smrtx.techmerch.adapters.CardAdapterWarehouses;
import kz.smrtx.techmerch.api.StringQuery;
import kz.smrtx.techmerch.items.entities.Warehouse;
import kz.smrtx.techmerch.items.entities.WarehouseJournal;
import kz.smrtx.techmerch.items.viewmodels.WarehouseJournalViewModel;
import kz.smrtx.techmerch.items.viewmodels.WarehouseViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarehouseJournalFragment extends Fragment {

    private RecyclerView recyclerView;
    private CardAdapterJournal adapter;
    private WarehouseJournalViewModel warehouseJournalViewModel;
    private TextView loading;
    private List<WarehouseJournal> journalEdited = new ArrayList<>();

    private FragmentListener listener;
    public interface FragmentListener {
        void getPageName(String name);
    }

    public static WarehouseJournalFragment getInstance() {
        return new WarehouseJournalFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_warehouse_journal, container, false);

        listener.getPageName(getResources().getString(R.string.warehouse_supply));
        warehouseJournalViewModel = new ViewModelProvider(this).get(WarehouseJournalViewModel.class);
        loading = view.findViewById(R.id.loading);
        recyclerView = view.findViewById(R.id.recyclerView);

        checkSuppliesOnServer();

        return view;
    }

    private void checkSuppliesOnServer() {
        Ius.getApiService().getQuery(
                        Ius.readSharedPreferences(this.getContext(), Ius.TOKEN),
                        StringQuery.getNewSupplies(Ius.readSharedPreferences(this.getContext(), Ius.USER_CITIES)))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (!response.isSuccessful()) {
                            Log.e("WarehouseJournal", "not Successful - " + response.code());
                            return;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                            Type type = new TypeToken<List<WarehouseJournal>>() {
                            }.getType();
                            List<WarehouseJournal> journal = new Gson().fromJson(jsonObject.getJSONArray("data").toString(), type);
                            Log.e("WarehouseJournal", "data size: " + journal.size() + " " + new Date());
                            if (journal.isEmpty())
                                loading.setText(getString(R.string.no_new_supplies));
                            else
                                gatherInOneByDate(journal);

                        } catch (JSONException | IOException e) {
                            Log.e("WarehouseJournal", e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Log.e("WarehouseJournal", "onFailure - " + t.getMessage());
                    }
                });
    }

    private void gatherInOneByDate(List<WarehouseJournal> journal) {
        String date;
        for (WarehouseJournal j : journal) {
            boolean objectFound = false;
            date = j.getDate().substring(0, j.getDate().indexOf('T'));

            for (WarehouseJournal copy : journalEdited) {
                if (copy.getDate().substring(0, j.getDate().indexOf('T')).equals(date)) {
                    copy.setContent(copy.getContent() + ", \n" + j.getContent());
                    objectFound = true;
                }
            }

            if (!objectFound)
                journalEdited.add(j);
        }

        loading.setVisibility(View.GONE);
        setAdapter();
    }

    private void setAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CardAdapterJournal(journalEdited, this.getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnAcceptCLickListener(position -> {
            Dialog dialog = Ius.createDialogAcception(this.getContext(), getString(R.string.acception), getString(R.string.supplies_dialog_text), true);
            Button negative = dialog.findViewById(R.id.negative);
            Button positive = dialog.findViewById(R.id.positive);
            negative.setOnClickListener(view -> dialog.cancel());
            positive.setOnClickListener(view -> {
                updateJournalOnServer(position);
                dialog.cancel();
            });
            dialog.show();
        });
    }

    private void updateJournalOnServer(int position) {
        Ius.getApiService().getQuery(
                        Ius.readSharedPreferences(this.getContext(), Ius.TOKEN),
                        StringQuery.acceptNewSupplies(this.getContext(), journalEdited.get(position).getDate()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (!response.isSuccessful()) {
                            Log.e("WarehouseJournal", "not Successful - " + response.code());
                            SessionActivity.getInstance().createToast(getString(R.string.acception_error) + " " + response.code(), false);
                            return;
                        }

                        journalEdited.remove(position);
                        adapter.refreshAdapter(journalEdited);
                        if (journalEdited.isEmpty()) {
                            loading.setText(getString(R.string.no_new_supplies));
                            loading.setVisibility(View.VISIBLE);
                        }
                        SessionActivity.getInstance().createToast(getString(R.string.acception_success), true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Log.e("WarehouseJournal", "onFailure - " + t.getMessage());
                        SessionActivity.getInstance().createToast(getString(R.string.acception_error) + " " + t.getMessage(), false);
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