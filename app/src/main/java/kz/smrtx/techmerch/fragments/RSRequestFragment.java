package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.StatusesActivity;
import kz.smrtx.techmerch.items.entities.Photo;
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.entities.User;
import kz.smrtx.techmerch.items.viewmodels.ChoosePointsViewModel;
import kz.smrtx.techmerch.items.viewmodels.HistoryViewModel;
import kz.smrtx.techmerch.items.viewmodels.PhotoViewModel;
import kz.smrtx.techmerch.items.viewmodels.UserViewModel;
import kz.smrtx.techmerch.utils.Aen;

public class RSRequestFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView noRequests;
    private List<Request> waitingList;
    private List<User> executors = new ArrayList<>();
    private Request request;
    private UserViewModel userViewModel;
    private ChoosePointsViewModel choosePointsViewModel;
    private PhotoViewModel photoViewModel;
    private HistoryViewModel historyViewModel;

    private TextView requestLoading;
    private TextView codeSummary;
    private TextView createdSummary;
    private TextView deadlineSummary;
    private TextView status;
    private TextView appointed;
    private TextView typeSummary;
    private TextView equipmentTypeSummary;
    private TextView equipmentSubtypeSummary;
    private TextView workSummary;
    private TextView replaceSummary;
    private TextView additionalSummary;
    private TextView addressSummary;
    private TextView workSubtypeSummary;
    private TextView specialSummary;
    private TextView commentSummary;
    private Button negative;
    private Button positive;
    private Button technicButton;
    private boolean positiveButtonPressed = false;
    private List<Photo> photoListTMR = new ArrayList<>();
    private List<Photo> photoListTech = new ArrayList<>();
    private RecyclerView recyclerViewTMR;
    private RecyclerView recyclerViewTech;
    private EditText executor;

    private int executorRoleFound;
    private boolean requestIsOpened = false;

    public static RSRequestFragment instance;

    public static RSRequestFragment getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rs_request, container, false);

        instance = this;

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        choosePointsViewModel = new ViewModelProvider(this).get(ChoosePointsViewModel.class);
        photoViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        recyclerView = view.findViewById(R.id.recyclerView);
        noRequests = view.findViewById(R.id.noRequests);
        requestLoading = view.findViewById(R.id.requestLoading);
        codeSummary = view.findViewById(R.id.code);
        createdSummary = view.findViewById(R.id.created);
        deadlineSummary = view.findViewById(R.id.deadline);
        status = view.findViewById(R.id.status);
        appointed = view.findViewById(R.id.appointed);
        typeSummary = view.findViewById(R.id.type);
        equipmentTypeSummary = view.findViewById(R.id.equipmentType);
        equipmentSubtypeSummary = view.findViewById(R.id.equipmentSubtype);
        workSummary = view.findViewById(R.id.work);
        replaceSummary = view.findViewById(R.id.replace);
        additionalSummary = view.findViewById(R.id.additional);
        addressSummary = view.findViewById(R.id.address);
        workSubtypeSummary = view.findViewById(R.id.workSubtype);
        specialSummary = view.findViewById(R.id.special);
        commentSummary = view.findViewById(R.id.comment);

        negative = view.findViewById(R.id.negative);
        positive = view.findViewById(R.id.positive);
        recyclerViewTMR = view.findViewById(R.id.recyclerViewTMR);
        recyclerViewTech = view.findViewById(R.id.recyclerViewTech);

        return view;
    }

    private void findNecessaryRole(int roleToSearch) {
        Log.i("findNecessaryRole", "starting to find with role " + roleToSearch);
        choosePointsViewModel.getSalePointByCode(String.valueOf(request.getREQ_SAL_CODE())).observe(this, sp -> {
            if (sp!=null) {
                Log.i("findNecessaryRole", "for creating executor list found salePoint " + sp.getName());
                if (roleToSearch==Aen.ROLE_TECHNIC)
                    getExecutorList(Integer.parseInt(sp.getLocationCode()), roleToSearch);
                else
                    new WhatRoleToGet(userViewModel, Integer.parseInt(sp.getLocationCode()), roleToSearch).execute();
            }
        });
    }

    private void getExecutorList(int locationCode, int roleCode) {
        userViewModel.getUserList(locationCode, roleCode).observe(this, u -> {
            executors = u;
        });
    }

    @SuppressLint("SetTextI18n")
    public void setRequest(Request request) {
        requestLoading.setVisibility(View.GONE);

        this.request = request;
        historyViewModel.getAllComments(request.getREQ_CODE()).observe(this, this::setComment);

        int userRole = Integer.parseInt(Ius.readSharedPreferences(getContext(), Ius.USER_ROLE_CODE));
        if (userRole== Aen.ROLE_TMR && request.getREQ_STA_ID()==Aen.STATUS_MANAGER_CANCELED)
            findNecessaryRole(Aen.ROLE_MANAGER);

        else if (userRole==Aen.ROLE_MANAGER)
            findNecessaryRole(Aen.ROLE_COORDINATOR);

        else if (userRole==Aen.ROLE_COORDINATOR)
            findNecessaryRole(Aen.ROLE_TECHNIC);

        photoViewModel.getPhotosByTMR(request.getREQ_CODE()).observe(this, tmr -> {
            photoListTMR = tmr;
            Ius.setAdapterImagesList(getContext(), recyclerViewTMR, photoListTMR);
        });
        photoViewModel.getPhotosByTech(request.getREQ_CODE()).observe(this, tech -> {
            photoListTech = tech;
            Ius.setAdapterImagesList(getContext(), recyclerViewTech, photoListTech);
        });

        String dateCreated = Ius.getDateByFormat(
                Ius.getDateFromString(
                        request.getREQ_CREATED(), "yyyy-MM-dd'T'HH:mm:ss"
                ), "dd.MM.yyyy, EEEE");
        String dateDeadline = Ius.getDateByFormat(
                Ius.getDateFromString(
                        request.getREQ_CREATED(), "yyyy-MM-dd'T'HH:mm:ss"
                ), "dd.MM.yyyy, EEEE");
        String dateUpdated = Ius.getDateByFormat(
                Ius.getDateFromString(
                        request.getREQ_UPDATED(), "yyyy-MM-dd'T'HH:mm:ss"
                ), "dd.MM.yyyy HH:mm:ss");

        codeSummary.setText(request.getREQ_CODE());

        createdSummary.setText(Ius.makeTextBold(getContext(), getResources().getString(R.string.date_of_creation) + ": " +
                dateCreated));
        deadlineSummary.setText(Ius.makeTextBold(getContext(), getResources().getString(R.string.deadline) + ": " +
                dateDeadline));

        status.setText(
                Ius.makeTextBold(getContext(), getResources().getString(R.string.request_status) + ": " + request.getREQ_STATUS())
        );

        if (request.getREQ_USE_NAME_APPOINTED()!=null)
            appointed.setText(
                    Ius.makeTextBold(getContext(), getResources().getString(R.string.appointed_on) + ": " + request.getREQ_USE_NAME_APPOINTED() + " " +
                            getResources().getString(R.string.from_) + " " + dateUpdated)
            );
        else
            appointed.setText(
                    getResources().getString(R.string.from_) + " " + " " + dateUpdated);

        typeSummary.setText(Ius.makeTextBold(getContext(), getResources().getString(R.string.request_type) + ": " + request.getREQ_TYPE()));

        if (isNull(request.getREQ_EQUIPMENT())) {
            equipmentTypeSummary.setVisibility(View.GONE);
            equipmentSubtypeSummary.setVisibility(View.GONE);
        }
        else {
            equipmentTypeSummary.setText(Ius.makeTextBold(getContext(), getResources().getString(R.string.equipment_type) + ": " + request.getREQ_EQUIPMENT()));
            equipmentSubtypeSummary.setText(Ius.makeTextBold(getContext(), getResources().getString(R.string.equipment_subtype) + ": " + request.getREQ_EQU_SUBTYPE()));
            equipmentTypeSummary.setVisibility(View.VISIBLE);
            equipmentSubtypeSummary.setVisibility(View.VISIBLE);
        }

        workSummary.setText(Ius.makeTextBold(getContext(), getResources().getString(R.string.work_type) + ": " + request.getREQ_WORK()));

        if (isNull(request.getREQ_ADDITIONAL()))
            additionalSummary.setVisibility(View.GONE);
        else {
            additionalSummary.setText(Ius.makeTextBold(getContext(), getResources().getString(R.string.additional) + ": " + request.getREQ_ADDITIONAL()));
            additionalSummary.setVisibility(View.VISIBLE);
        }

        if (isNull(request.getREQ_REPLACE()))
            replaceSummary.setVisibility(View.GONE);
        else {
            replaceSummary.setText(Ius.makeTextBold(getContext(), getResources().getString(R.string.replace) + ": " + request.getREQ_REPLACE()));
            replaceSummary.setVisibility(View.VISIBLE);
        }

        if (isNull(request.getREQ_SECONDARY_ADDRESS()))
            addressSummary.setVisibility(View.GONE);
        else {
            addressSummary.setText(Ius.makeTextBold(getContext(), getResources().getString(R.string.address) + ": " + request.getREQ_SECONDARY_ADDRESS()));
            addressSummary.setVisibility(View.VISIBLE);
        }

        if (isNull(request.getREQ_WORK_SUBTYPE()))
            workSubtypeSummary.setVisibility(View.GONE);
        else {
            workSubtypeSummary.setText(Ius.makeTextBold(getContext(), getResources().getString(R.string.work_subtype) + ": " + request.getREQ_WORK_SUBTYPE()));
            workSubtypeSummary.setVisibility(View.VISIBLE);
        }

        if (isNull(request.getREQ_WORK_SPECIAL()))
            specialSummary.setVisibility(View.GONE);
        else {
            specialSummary.setText(Ius.makeTextBold(getContext(), getResources().getString(R.string.glo_equipment) + ": " + request.getREQ_WORK_SPECIAL()));
            specialSummary.setVisibility(View.VISIBLE);
        }
    }

    private boolean isNull(String text) {
        if (text==null)
            return true;
        return text.length() == 0;
    }

    @SuppressLint("StaticFieldLeak")
    class WhatRoleToGet extends AsyncTask<Void, Void, Void> {
        UserViewModel userViewModel;
        int roleCode;
        int locationCode;

        public WhatRoleToGet(UserViewModel userViewModel, int locationCode, int roleCode) {
            this.userViewModel = userViewModel;
            this.locationCode = locationCode;
            this.roleCode = roleCode;
        }

        @Override
        protected void onPostExecute(Void unused) {
            executorRoleFound = roleCode;
            Log.i("WhatRoleToGet", "process ended, getting list of role " + roleCode);
            getExecutorList(locationCode, roleCode);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("WhatRoleToGet", "search between roles " + roleCode + " in city " + locationCode);

            if (roleCode==Aen.ROLE_MANAGER) {
                searchInManagers();
            }
            else if (roleCode==Aen.ROLE_COORDINATOR) {
                searchInCoordinators();
            }

            return null;
        }

        private void searchInManagers() {
            if (userViewModel.getNumberOfUsers(locationCode, roleCode)==0) {
                Log.w("WhatRoleToGet", "0 managers");
                searchInCoordinators();
                roleCode = Aen.ROLE_COORDINATOR;
            }
        }

        private void searchInCoordinators() {
            if (userViewModel.getNumberOfUsers(locationCode, roleCode)==0) {
                Log.w("WhatRoleToGet", "0 coordinators");
                roleCode = Aen.ROLE_TECHNIC;
            }
        }
    }

    private void setComment(List<String> c) {
        if (c==null) {
            Log.e("getRequestComments", "there is no comments in db...");
            commentSummary.setVisibility(View.GONE);
            return;
        }

        StringBuilder comment = new StringBuilder();
        for (String part : c) {
            comment.append("\n\n").append("- ").append(part);
        }
        commentSummary.setText(Ius.makeTextBold(getContext(), getResources().getString(R.string.comments) + ": " + comment));
        commentSummary.setVisibility(View.VISIBLE);
        Log.i("getRequestComments", String.valueOf(c.size()));
    }
}