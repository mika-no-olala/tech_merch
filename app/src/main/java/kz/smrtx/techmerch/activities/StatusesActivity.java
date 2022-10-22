package kz.smrtx.techmerch.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.adapters.CardAdapterHistory;
import kz.smrtx.techmerch.adapters.CardAdapterImages;
import kz.smrtx.techmerch.adapters.CardAdapterRequests;
import kz.smrtx.techmerch.adapters.CardAdapterString;
import kz.smrtx.techmerch.items.dao.ChoosePointsDao;
import kz.smrtx.techmerch.items.entities.History;
import kz.smrtx.techmerch.items.entities.Photo;
import kz.smrtx.techmerch.items.entities.Request;

import kz.smrtx.techmerch.items.entities.User;
import kz.smrtx.techmerch.items.viewmodels.ChoosePointsViewModel;
import kz.smrtx.techmerch.items.viewmodels.HistoryViewModel;
import kz.smrtx.techmerch.items.viewmodels.PhotoViewModel;
import kz.smrtx.techmerch.items.viewmodels.RequestViewModel;
import kz.smrtx.techmerch.items.viewmodels.SalePointViewModel;
import kz.smrtx.techmerch.items.viewmodels.UserViewModel;
import kz.smrtx.techmerch.utils.LocaleHelper;
import kz.smrtx.techmerch.utils.Aen;

public class StatusesActivity extends AppCompatActivity {

    private boolean listIsOpen = true;
    private boolean myRequestOpened = false;
    private TextView pageName;
    private final Context context = this;
    private int salePointCode = -1;
    private int userCode;

    // <----  list ---->

    private View list;
    private RecyclerView waitingRecycler;
    private RecyclerView advancingRecycler;
    private RecyclerView finishedRecycler;
    private TextView noRequestsW;
    private TextView noRequestsA;
    private TextView noRequestsF;
    private CardView waitingView;
    private CardView advancingView;
    private CardView finishedView;
    private List<Request> waitingList;
    private List<History> advancingList;
    private List<History> finishedList;
    private boolean waitingListIsOpen = true;
    private boolean advancingListIsOpen = false;
    private boolean finishedListIsOpen = false;
    private TextView waitingTextView;
    private TextView advancingTextView;
    private TextView finishedTextView;

    private RequestViewModel requestViewModel;
    private HistoryViewModel historyViewModel;

    // <----  request ---->

    private View summary;
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
    private Request request;
    private Button negative;
    private Button positive;
    private Button technicButton;
    private boolean positiveButtonPressed = false;
    private List<Photo> photoListTMR = new ArrayList<>();
    private List<Photo> photoListTech = new ArrayList<>();
    private RecyclerView recyclerViewTMR;
    private RecyclerView recyclerViewTech;
    private PhotoViewModel photoViewModel;
    private UserViewModel userViewModel;
    private ChoosePointsViewModel choosePointsViewModel;
    private List<User> executors = new ArrayList<>();
    private EditText executor;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statuses);

        noRequestsW = findViewById(R.id.noRequestsW);
        noRequestsA = findViewById(R.id.noRequestsA);
        noRequestsF = findViewById(R.id.noRequestsF);
        waitingRecycler = findViewById(R.id.waitingRequestsRecycler);
        advancingRecycler = findViewById(R.id.advancingRequestsRecycler);
        finishedRecycler = findViewById(R.id.finishedRequestsRecycler);
        waitingTextView = findViewById(R.id.waitingTextView);
        advancingTextView = findViewById(R.id.advancingTextView);
        finishedTextView = findViewById(R.id.finishedTextView);
        pageName = findViewById(R.id.pageName);

        requestViewModel = new ViewModelProvider(this).get(RequestViewModel.class);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        photoViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        choosePointsViewModel = new ViewModelProvider(this).get(ChoosePointsViewModel.class);

        userCode = Integer.parseInt(Ius.readSharedPreferences(this, Ius.USER_CODE));

        Bundle arguments = getIntent().getExtras();
        if (arguments!=null) {
            salePointCode = Integer.parseInt(arguments.getString("salePointCode"));
            pageName.setText(getResources().getString(R.string.requests_statuses_sale_point));
        }
        else {
            pageName.setText(getResources().getString(R.string.requests_statuses));
        }

        getWaitingRequests(salePointCode);
        getAdvancingRequests(salePointCode);
        getFinishedRequests(salePointCode);

        TextView bottomBarText = findViewById(R.id.bottomBarText);
        View back = findViewById(R.id.back);
        CardView waitingButton = findViewById(R.id.waitingRequests);
        CardView advancingButton = findViewById(R.id.advancingRequests);
        CardView finishedButton = findViewById(R.id.finishedRequests);
        ImageView waitingArrowIcon = findViewById(R.id.waitingArrowIcon);
        ImageView advancingArrowIcon = findViewById(R.id.advancingArrowIcon);
        ImageView finishedArrowIcon = findViewById(R.id.finishedArrowIcon);
        waitingView = findViewById(R.id.waitingView);
        advancingView = findViewById(R.id.advancingView);
        finishedView = findViewById(R.id.finishedView);
        list = findViewById(R.id.list);
        summary = findViewById(R.id.summary);
        technicButton = findViewById(R.id.technicButton);

        bottomBarText.setText(Ius.readSharedPreferences(this, Ius.BOTTOM_BAR_TEXT));

        initializeSummaryStuff();
        technicStuff();

        waitingButton.setOnClickListener(view -> listButtonIsPressed(waitingListIsOpen, waitingArrowIcon, waitingView, 1));
        advancingButton.setOnClickListener(view -> listButtonIsPressed(advancingListIsOpen, advancingArrowIcon, advancingView, 2));
        finishedButton.setOnClickListener(view -> listButtonIsPressed(finishedListIsOpen, finishedArrowIcon, finishedView, 3));

        negative.setOnClickListener(view -> {
            positiveButtonPressed = false;
            openDialogCommentAcception();
        });

        positive.setOnClickListener(view -> {
            positiveButtonPressed = true;
            openDialogCommentAcception();
        });

        back.setOnClickListener(view -> onBackPressed());
    }

    private void listButtonIsPressed(boolean listIsOpen, ImageView arrowIcon, CardView contentView, int listNumber) {
        if (listIsOpen) {
            arrowIcon.setRotation(180);
            contentView.setVisibility(View.GONE);
            listIsOpen = false;
        }
        else {
            arrowIcon.setRotation(-90);
            contentView.setVisibility(View.VISIBLE);
            listIsOpen = true;
        }

        switch (listNumber) {
            case 1:
                waitingListIsOpen = listIsOpen;
                break;
            case 2:
                advancingListIsOpen = listIsOpen;
                break;
            case 3:
                finishedListIsOpen = listIsOpen;
                break;
        }
    }

    private void initializeSummaryStuff() {
        requestLoading = findViewById(R.id.requestLoading);
        codeSummary = findViewById(R.id.code);
        createdSummary = findViewById(R.id.created);
        deadlineSummary = findViewById(R.id.deadline);
        status = findViewById(R.id.status);
        appointed = findViewById(R.id.appointed);
        typeSummary = findViewById(R.id.type);
        equipmentTypeSummary = findViewById(R.id.equipmentType);
        equipmentSubtypeSummary = findViewById(R.id.equipmentSubtype);
        workSummary = findViewById(R.id.work);
        replaceSummary = findViewById(R.id.replace);
        additionalSummary = findViewById(R.id.additional);
        addressSummary = findViewById(R.id.address);
        workSubtypeSummary = findViewById(R.id.workSubtype);
        specialSummary = findViewById(R.id.special);
        commentSummary = findViewById(R.id.comment);

        negative = findViewById(R.id.negative);
        positive = findViewById(R.id.positive);
        recyclerViewTMR = findViewById(R.id.recyclerViewTMR);
        recyclerViewTech = findViewById(R.id.recyclerViewTech);
    }

    private void technicStuff() {
        if (!Ius.readSharedPreferences(context, Ius.USER_ROLE_CODE).equals("4"))
            return;

        View twoButtons = findViewById(R.id.twoButtons);
        technicButton = findViewById(R.id.technicButton);
        twoButtons.setVisibility(View.GONE);
        technicButton.setVisibility(View.VISIBLE);

        technicButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, OutletInformationActivity.class);
            intent.putExtra("salePointCode", request.getREQ_SAL_CODE());
            intent.putExtra("scenario", "technic");
            intent.putExtra("requestCode", request.getREQ_CODE());
            startActivity(intent);
        });
    }

    @SuppressLint("SetTextI18n")
    private void getWaitingRequests(int salePointCode) {
        Log.i("gettingRequests from", String.valueOf(userCode));

        if (salePointCode!=-1) {
            requestViewModel.getRequestsByAppointed(userCode, salePointCode)
                .observe(this, w -> {
                    waitingList = w;
                    setAdapter(waitingRecycler, noRequestsW, 1);
                    waitingTextView.setText(getResources().getString(R.string.request_waiting_status) + " (" + waitingList.size() + ")");
                });
        }
        else {
            requestViewModel.getRequestsByAppointed(userCode)
                .observe(this, w -> {
                    waitingList = w;
                    setAdapter(waitingRecycler, noRequestsW, 1);
                    waitingTextView.setText(getResources().getString(R.string.request_waiting_status) + " (" + waitingList.size() + ")");
                });
        }
    }

    @SuppressLint("SetTextI18n")
    private void getAdvancingRequests(int salePointCode) {
        if (salePointCode!=-1) {
            historyViewModel.getHistoryListWhichAreRelatedTo(userCode, salePointCode)
                .observe(this, a -> {
                    advancingList = a;
                    setAdapter(advancingRecycler, noRequestsA, 2);
                    advancingTextView.setText(getResources().getString(R.string.request_advancing_status) + " (" + advancingList.size() + ")");
                });
        }
        else {
            historyViewModel.getHistoryListWhichAreRelatedTo(userCode)
                .observe(this, a -> {
                    advancingList = a;
                    setAdapter(advancingRecycler, noRequestsA, 2);
                    advancingTextView.setText(getResources().getString(R.string.request_advancing_status) + " (" + advancingList.size() + ")");
                });
        }
    }

    @SuppressLint("SetTextI18n")
    private void getFinishedRequests(int salePointCode) {
        if (salePointCode!=-1) {
            historyViewModel.getHistoryListWhichAreRelatedToAndFinished(userCode, salePointCode)
                .observe(this, f -> {
                    finishedList = f;
                    setAdapter(finishedRecycler, noRequestsF, 3);
                    finishedTextView.setText(getResources().getString(R.string.request_finished_status) + " (" + finishedList.size() + ")");
                });
        }
        else {
            historyViewModel.getHistoryListWhichAreRelatedToAndFinished(userCode)
                .observe(this, f -> {
                    finishedList = f;
                    setAdapter(finishedRecycler, noRequestsF, 3);
                    finishedTextView.setText(getResources().getString(R.string.request_finished_status) + " (" + finishedList.size() + ")");
                });
        }
    }

    private void setAdapter(RecyclerView recyclerView, TextView noRequests, int listNumber) {
        RecyclerView.LayoutManager layoutManager;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        
        boolean isTechnic = Ius.readSharedPreferences(context, Ius.USER_ROLE_CODE).equals("4");

        if (listNumber==1) {
            // while db is loading, /first case/ triggers. Need to set visibility back if it loads the data after
            checkNoRequestsInList(noRequests, recyclerView, waitingList.isEmpty());

            CardAdapterRequests cardAdapterRequests = new CardAdapterRequests(waitingList, this);
            recyclerView.setAdapter(cardAdapterRequests);
            cardAdapterRequests.setOnItemClickListener(position -> {
                request = waitingList.get(position);
                myRequestOpened = true;
                setRequest();
                requestLoading.setVisibility(View.GONE);
                closeList(isTechnic, true);
            });
        }
        else {
            List<History> list;
            CardAdapterHistory cardAdapterHistory;
            if (listNumber==2) {
                list = advancingList;
                cardAdapterHistory = new CardAdapterHistory(advancingList, this);
            }
            else {
                list = finishedList;
                cardAdapterHistory = new CardAdapterHistory(finishedList, this);
            }

            checkNoRequestsInList(noRequests, recyclerView, list.isEmpty());

            recyclerView.setAdapter(cardAdapterHistory);
            cardAdapterHistory.setOnItemClickListener(position -> {
                getRequest(list.get(position).getRequestCode());
                myRequestOpened = false;
                closeList(isTechnic, false);
            });
        }
    }

    private void checkNoRequestsInList(TextView noRequests, RecyclerView recyclerView, boolean listIsEmpty) {
        if (listIsEmpty) {
            noRequests.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            noRequests.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setAdapter(RecyclerView recyclerView, List<Photo> photoList) {
        RecyclerView.LayoutManager layoutManager;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        if (photoList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            return;
        }

        CardAdapterImages cardAdapter = new CardAdapterImages(photoList, this);
        recyclerView.setAdapter(cardAdapter);
        cardAdapter.setOnItemClickListener(position -> createDialog(photoList.get(position).getREP_PHOTO()));
    }

    private void createDialog(String photoName) {
        Dialog dialog = Ius.createDialog(this, R.layout.dialog_window_image, "");
        ImageView image = dialog.findViewById(R.id.image);

        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + photoName);

        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            image.setImageBitmap(bitmap);
        }

        dialog.show();
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

    private void getRequest(String code) {
        Log.i("getRequest code", code);
        requestViewModel.getRequestByCode(code).observe(this, r -> {
            if (r!=null) {
                requestLoading.setVisibility(View.GONE);
                request = r;
                setRequest();
            }
            else {
                Log.e("getRequest", "there is no request in db...");
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setRequest() {
        int userRole = Integer.parseInt(Ius.readSharedPreferences(this, Ius.USER_ROLE_CODE));
        if (userRole==Aen.ROLE_TMR && request.getREQ_STA_ID()==Aen.STATUS_MANAGER_CANCELED)
            findNecessaryRole(Aen.ROLE_MANAGER);

        else if (userRole==Aen.ROLE_MANAGER)
            findNecessaryRole(Aen.ROLE_COORDINATOR);

        else if (userRole==Aen.ROLE_COORDINATOR)
            findNecessaryRole(Aen.ROLE_TECHNIC);

        photoViewModel.getPhotosByTMR(request.getREQ_CODE()).observe(this, tmr -> {
            photoListTMR = tmr;
            setAdapter(recyclerViewTMR, photoListTMR);
        });
        photoViewModel.getPhotosByTech(request.getREQ_CODE()).observe(this, tech -> {
            photoListTech = tech;
            setAdapter(recyclerViewTech, photoListTech);
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

        createdSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.date_of_creation) + ": " +
                dateCreated));
        deadlineSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.deadline) + ": " +
                dateDeadline));

        status.setText(
                Ius.makeTextBold(this, getResources().getString(R.string.request_status) + ": " + request.getREQ_STATUS())
        );

        if (request.getREQ_USE_NAME_APPOINTED()!=null)
            appointed.setText(
                    Ius.makeTextBold(this, getResources().getString(R.string.appointed_on) + ": " + request.getREQ_USE_NAME_APPOINTED() + " " +
                            getResources().getString(R.string.from_) + " " + dateUpdated)
            );
        else
            appointed.setText(
                   getResources().getString(R.string.from_) + " " + " " + dateUpdated);

        typeSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.request_type) + ": " + request.getREQ_TYPE()));

        if (isNull(request.getREQ_EQUIPMENT())) {
            equipmentTypeSummary.setVisibility(View.GONE);
            equipmentSubtypeSummary.setVisibility(View.GONE);
        }
        else {
            equipmentTypeSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.equipment_type) + ": " + request.getREQ_EQUIPMENT()));
            equipmentSubtypeSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.equipment_subtype) + ": " + request.getREQ_EQU_SUBTYPE()));
            equipmentTypeSummary.setVisibility(View.VISIBLE);
            equipmentSubtypeSummary.setVisibility(View.VISIBLE);
        }

        workSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.work_type) + ": " + request.getREQ_WORK()));

        if (isNull(request.getREQ_ADDITIONAL()))
            additionalSummary.setVisibility(View.GONE);
        else {
            additionalSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.additional) + ": " + request.getREQ_ADDITIONAL()));
            additionalSummary.setVisibility(View.VISIBLE);
        }

        if (isNull(request.getREQ_REPLACE()))
            replaceSummary.setVisibility(View.GONE);
        else {
            replaceSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.replace) + ": " + request.getREQ_REPLACE()));
            replaceSummary.setVisibility(View.VISIBLE);
        }

        if (isNull(request.getREQ_ADDRESS_SALEPOINT()))
            addressSummary.setVisibility(View.GONE);
        else {
            addressSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.address) + ": " + request.getREQ_ADDRESS_SALEPOINT()));
            addressSummary.setVisibility(View.VISIBLE);
        }

        if (isNull(request.getREQ_WORK_SUBTYPE()))
            workSubtypeSummary.setVisibility(View.GONE);
        else {
            workSubtypeSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.work_subtype) + ": " + request.getREQ_WORK_SUBTYPE()));
            workSubtypeSummary.setVisibility(View.VISIBLE);
        }

        if (isNull(request.getREQ_WORK_SPECIAL()))
            specialSummary.setVisibility(View.GONE);
        else {
            specialSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.glo_equipment) + ": " + request.getREQ_WORK_SPECIAL()));
            specialSummary.setVisibility(View.VISIBLE);
        }

        if (isNull(request.getREQ_COMMENT()))
            commentSummary.setVisibility(View.GONE);
        else {
            commentSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.comment) + ": " + request.getREQ_COMMENT()));
            commentSummary.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void openDialogCommentAcception() {
        Dialog dialog = Ius.createDialog(this, R.layout.dialog_window_request_acception, "");

        int userRole = Integer.parseInt(Ius.readSharedPreferences(context, Ius.USER_ROLE_CODE));
        boolean needComment = isCommentNeeded(userRole);
        boolean needExecutor = isExecutorNeeded(userRole);

        EditText comment = dialog.findViewById(R.id.comment);
        executor = dialog.findViewById(R.id.executor);
        Button positive = dialog.findViewById(R.id.positive);

        Log.e("sss", request.getREQ_STATUS());
        if (request.getREQ_STATUS().contains("Отклонено"))
            executor.setText(request.getREQ_USE_CODE() + " - " + request.getREQ_USE_NAME());

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
                    createToast(getResources().getString(R.string.fill_field), false);
                    return;
                }
            }
            if (needExecutor) {
                String executorStr = executor.getText().toString();
                if (executorStr.length()==0) {
                    createToast(getResources().getString(R.string.fill_field), false);
                    return;
                }

                int indexOfDivider = executorStr.indexOf("-");
                executorCode = Integer.parseInt(executorStr.substring(0, indexOfDivider - 1));
            }

            if (userRole == Aen.ROLE_MANAGER && !positiveButtonPressed) { // sss SET POSITIVE BUTTON FOR MANAGER
                request.setREQ_STA_ID(Aen.STATUS_MANAGER_CANCELED);
                request.setREQ_USE_CODE_APPOINTED(request.getREQ_USE_CODE());
            }

            else if (userRole == Aen.ROLE_MANAGER && positiveButtonPressed) {
                request.setREQ_STA_ID(Aen.STATUS_WAITING_TECHNIC);
                request.setREQ_USE_CODE_APPOINTED(executorCode);
            }

            else if (userRole== Aen.ROLE_TMR && !positiveButtonPressed) {
                request.setREQ_STA_ID(Aen.STATUS_TMR_CANCELED);
                request.setREQ_USE_CODE_APPOINTED(request.getREQ_USE_CODE());
            }

            else if (userRole==Aen.ROLE_TMR && positiveButtonPressed) {
                request.setREQ_STA_ID(Aen.STATUS_FINISHED);
                request.setREQ_USE_CODE_APPOINTED(8); // 8 is superadmin - means request is finished
            }

            if (needComment)
                request.setREQ_COMMENT(commentStr);

            request.setREQ_USE_CODE(Integer.parseInt(Ius.readSharedPreferences(context, Ius.USER_CODE)));
            request.setREQ_UPDATED(Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss"));
            request.setNES_TO_UPDATE("yes");

            requestViewModel.update(request);

            createToast(getResources().getString(R.string.request_saved), true);
            dialog.cancel();
            closeSummary();
        });

        executor.setOnClickListener(executorView -> {
            if (!executors.isEmpty()) {
                openDialogUsers();
                return;
            }

            createToast(getString(R.string.no_executor_error), false);
        });
    }

    private boolean isCommentNeeded(int userRole) {
        return (userRole == Aen.ROLE_MANAGER && !positiveButtonPressed)
        || userRole == Aen.ROLE_TECHNIC
        || (userRole == Aen.ROLE_TMR && !positiveButtonPressed);
    }

    private boolean isExecutorNeeded(int userRole) {
        return ((userRole == Aen.ROLE_MANAGER
                || userRole == Aen.ROLE_COORDINATOR) && positiveButtonPressed)
                || (userRole == Aen.ROLE_TMR && !positiveButtonPressed);
    }

    private void closeList(boolean isTechnic, boolean isWaitingList) {
        listIsOpen = false;
        summary.setVisibility(View.VISIBLE);
        list.setVisibility(View.GONE);

        if (!myRequestOpened) {
            negative.setVisibility(View.GONE);
            positive.setVisibility(View.GONE);
        }
        else {
            negative.setVisibility(View.VISIBLE);
            positive.setVisibility(View.VISIBLE);
        }
        pageName.setText(getResources().getString(R.string.request));

        if (isTechnic) {
            if (isWaitingList)
                technicButton.setVisibility(View.VISIBLE);
            else
                technicButton.setVisibility(View.GONE);
        }
    }

    private void closeSummary() {
        listIsOpen = true;
        summary.setVisibility(View.GONE);
        list.setVisibility(View.VISIBLE);
        requestLoading.setVisibility(View.VISIBLE);
        pageName.setText(getResources().getString(R.string.requests_statuses));
    }

    @Override
    public void onBackPressed() {
        if (listIsOpen)
            finish();
        else
            closeSummary();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWaitingRequests(salePointCode);
        getAdvancingRequests(salePointCode);
        getFinishedRequests(salePointCode);
        closeSummary();
    }

    private boolean isNull(String text) {
        if (text==null)
            return true;
        return text.length() == 0;
    }

    @SuppressWarnings("deprecation")
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
            Log.i("WhatRoleToGet", "process ended, getting list of role " + roleCode);
            getExecutorList(locationCode, roleCode);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("WhatRoleToGet", "search between roles " + roleCode + " in city " + locationCode);

            if (roleCode==6) {
                searchInManagers();
            }
            else if (roleCode==7) {
                searchInCoordinators();
            }

            return null;
        }

        private void searchInManagers() {
            if (userViewModel.getNumberOfUsers(locationCode, roleCode)==0) {
                Log.w("WhatRoleToGet", "0 managers");
                searchInCoordinators();
                roleCode = 7;
            }
        }

        private void searchInCoordinators() {
            if (userViewModel.getNumberOfUsers(locationCode, roleCode)==0) {
                Log.w("WhatRoleToGet", "0 coordinators");
                roleCode = 4;
            }
        }
    }

    private void openDialogUsers() {
        CardAdapterString adapter = new CardAdapterString();
        adapter.setAdapterUser(executors);
        Dialog dialog = Ius.createDialogList(this, adapter);
        SearchView search = dialog.findViewById(R.id.search);

        search.setVisibility(View.GONE);

        dialog.show();

        adapter.setOnItemClickListener(position -> {
            String userInfo = executors.get(position).getCode() + " - " + executors.get(position).getName();
            executor.setText(userInfo);
            dialog.cancel();
        });
    }

    private void createToast(String text, boolean success) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, findViewById(R.id.toast));
        Ius.showToast(layout, this, text, success);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}