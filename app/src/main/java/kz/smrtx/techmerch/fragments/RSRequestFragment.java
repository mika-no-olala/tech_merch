package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.OutletInformationActivity;
import kz.smrtx.techmerch.activities.RequestStatusesActivity;
import kz.smrtx.techmerch.activities.SessionActivity;
import kz.smrtx.techmerch.activities.StatusesActivity;
import kz.smrtx.techmerch.adapters.CardAdapterString;
import kz.smrtx.techmerch.items.entities.Photo;
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.entities.User;
import kz.smrtx.techmerch.items.viewmodels.ChoosePointsViewModel;
import kz.smrtx.techmerch.items.viewmodels.HistoryViewModel;
import kz.smrtx.techmerch.items.viewmodels.PhotoViewModel;
import kz.smrtx.techmerch.items.viewmodels.RequestViewModel;
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
    private RequestViewModel requestViewModel;

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
    private CardView card;

    private int userRole;
    private int executorRoleFound;
    private boolean requestIsOpened = false;
    private boolean isTechnic;

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
        requestViewModel = new ViewModelProvider(this).get(RequestViewModel.class);

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
        technicButton = view.findViewById(R.id.technicButton);
        recyclerViewTMR = view.findViewById(R.id.recyclerViewTMR);
        recyclerViewTech = view.findViewById(R.id.recyclerViewTech);
        card = view.findViewById(R.id.card);

        closeRequestView();
        isTechnic = Ius.readSharedPreferences(getContext(), Ius.USER_ROLE_CODE).equals(String.valueOf(Aen.ROLE_TECHNIC));
        userRole = Integer.parseInt(Ius.readSharedPreferences(getContext(), Ius.USER_ROLE_CODE));

        negative.setOnClickListener(button -> {
            positiveButtonPressed = false;
            openDialogCommentAcception();
        });

        positive.setOnClickListener(button -> {
            positiveButtonPressed = true;
            openDialogCommentAcception();
        });

        technicButton.setOnClickListener(button -> {
            Intent intent = new Intent(getContext(), OutletInformationActivity.class);
            intent.putExtra("salePointCode", request.getREQ_SAL_CODE());
            intent.putExtra("scenario", "technic");
            intent.putExtra("requestCode", request.getREQ_CODE());
            startActivity(intent);
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void openDialogCommentAcception() {
        Dialog dialog = Ius.createDialog(getContext(), R.layout.dialog_window_request_acception, "");

        boolean needComment = isCommentNeeded(userRole);
        boolean needExecutor = isExecutorNeeded(userRole);

        TextView title = dialog.findViewById(R.id.title);
        EditText comment = dialog.findViewById(R.id.comment);
        executor = dialog.findViewById(R.id.executor);
        Button positive = dialog.findViewById(R.id.positive);

        if (!positiveButtonPressed)
            title.setText(getString(R.string.send_back));

        else if (request.getREQ_STA_ID() == Aen.STATUS_WAITING_TMR)
            title.setText(getString(R.string.end_request));

        if (!needComment) {
            comment.setVisibility(View.GONE);
        }
        if (needExecutor) {
            executor.setVisibility(View.VISIBLE);
        }

        dialog.show();

        positive.setOnClickListener(view -> {
            String commentStr = "";
            int executorCode = 0;

            if (needComment) {
                commentStr = comment.getText().toString().trim();
                if (commentStr.length()==0) {
                    SessionActivity.getInstance().createToast(getString(R.string.fill_field), false);
                    return;
                }
            }
            if (needExecutor) {
                String executorStr = executor.getText().toString();
                if (executorStr.length()==0) {
                    SessionActivity.getInstance().createToast(getString(R.string.fill_field), false);
                    return;
                }

                int indexOfDivider = executorStr.indexOf("-");
                executorCode = Integer.parseInt(executorStr.substring(0, indexOfDivider - 1));
            }

            if (userRole == Aen.ROLE_MANAGER && !positiveButtonPressed) { // sss SET POSITIVE BUTTON FOR MANAGER
                request.setREQ_STA_ID(Aen.STATUS_MANAGER_CANCELED);
                request.setREQ_USE_CODE_APPOINTED(request.getREQ_USE_CODE());
            }

            else if (userRole == Aen.ROLE_MANAGER) {
                request.setREQ_STA_ID(Aen.getStatusByExecutorAfterManager(executorRoleFound));
                request.setREQ_USE_CODE_APPOINTED(executorCode);
            }

            else if (userRole == Aen.ROLE_COORDINATOR && !positiveButtonPressed) {
                request.setREQ_STA_ID(Aen.STATUS_COORDINATOR_CANCELED);
                request.setREQ_USE_CODE_APPOINTED(request.getREQ_USE_CODE());
            }

            else if (userRole == Aen.ROLE_COORDINATOR) {
                request.setREQ_STA_ID(Aen.STATUS_WAITING_TECHNIC);
                request.setREQ_USE_CODE_APPOINTED(executorCode);
            }

            else if (userRole == Aen.ROLE_TMR && !positiveButtonPressed) {
                request.setREQ_STA_ID(Aen.STATUS_TMR_CANCELED);
                request.setREQ_USE_CODE_APPOINTED(request.getREQ_USE_CODE());
            }

            else if (userRole == Aen.ROLE_TMR && request.getREQ_STA_ID() == Aen.STATUS_WAITING_TMR) {
                request.setREQ_STA_ID(Aen.STATUS_FINISHED);
                request.setREQ_USE_CODE_APPOINTED(8); // 8 is superadmin - means request is finished
            }

            else if (userRole == Aen.ROLE_TMR) {
                request.setREQ_STA_ID(Aen.getStatusByExecutorAfterTMR(executorRoleFound));
                request.setREQ_USE_CODE_APPOINTED(executorCode);
            }

            if (needComment) {
                request.setREQ_COMMENT(Ius.saveApostrophe(commentStr));
                Log.i("Convert comment", commentStr + " -> " + request.getREQ_COMMENT());
            }

            request.setREQ_USE_CODE(Integer.parseInt(Ius.readSharedPreferences(getContext(), Ius.USER_CODE)));
            request.setREQ_UPDATED(Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss"));
            request.setNES_TO_UPDATE("yes");
            requestViewModel.update(request);

            SessionActivity.getInstance().startRequestSending();

            dialog.cancel();

            int salePointCode = ((RequestStatusesActivity)requireActivity()).getSalePointCode();
            if (salePointCode!=-1)
                OperationsOnOutletFragment.getInstance().cancelVisitClear();
            else
                SessionActivity.getInstance().cancelSessionClear();

            closeRequestView();
            ((RequestStatusesActivity)requireActivity()).setTimerCoolDown();
        });

        executor.setOnClickListener(executorView -> {
            if (!executors.isEmpty()) {
                openDialogUsers();
                return;
            }

            SessionActivity.getInstance().createToast(getString(R.string.no_executor_error), false);
        });
    }

    private void openDialogUsers() {
        CardAdapterString adapter = new CardAdapterString();
        adapter.setAdapterUser(executors);
        Dialog dialog = Ius.createDialogList(getContext(), adapter);
        SearchView search = dialog.findViewById(R.id.search);

        search.setVisibility(View.GONE);

        dialog.show();

        adapter.setOnItemClickListener(position -> {
            String userInfo = executors.get(position).getCode() + " - " + executors.get(position).getName();
            executor.setText(userInfo);
            dialog.cancel();
        });
    }

    private boolean isCommentNeeded(int userRole) {
        return (userRole == Aen.ROLE_MANAGER && !positiveButtonPressed)
                || (userRole == Aen.ROLE_COORDINATOR && !positiveButtonPressed)
                || userRole == Aen.ROLE_TECHNIC
                || (userRole == Aen.ROLE_TMR && !(positiveButtonPressed && request.getREQ_STA_ID() == Aen.STATUS_WAITING_TMR));
    }

    private boolean isExecutorNeeded(int userRole) {
        return ((userRole == Aen.ROLE_MANAGER
                || userRole == Aen.ROLE_COORDINATOR
                || (userRole == Aen.ROLE_TMR
                && request.getREQ_STA_ID() != Aen.STATUS_WAITING_TMR)) && positiveButtonPressed);
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

    private void setButtons() {
        int tabNumber = ((RequestStatusesActivity)requireActivity()).getLastTabNumber();

        if (!isTechnic && tabNumber == 1) {  //waiting list
            negative.setVisibility(View.VISIBLE);
            positive.setVisibility(View.VISIBLE);
        }
        else {
            negative.setVisibility(View.GONE);
            positive.setVisibility(View.GONE);
        }

        if (isTechnic && tabNumber == 1)
            technicButton.setVisibility(View.VISIBLE);
        else
            technicButton.setVisibility(View.GONE);
    }

    public void closeRequestView() {
        requestLoading.setVisibility(View.VISIBLE);
        card.setVisibility(View.GONE);
        positive.setVisibility(View.GONE);
        negative.setVisibility(View.GONE);
        technicButton.setVisibility(View.GONE);
    }

    private void showRequestView() {
        requestLoading.setVisibility(View.GONE);
        card.setVisibility(View.VISIBLE);
        positive.setVisibility(View.VISIBLE);
        negative.setVisibility(View.VISIBLE);
        technicButton.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    public void setRequest(Request request) {
        this.request = request;
        historyViewModel.getAllComments(request.getREQ_CODE()).observe(this, this::setComment);

        showRequestView();

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

        setButtons();
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