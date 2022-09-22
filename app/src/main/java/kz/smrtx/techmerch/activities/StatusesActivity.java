package kz.smrtx.techmerch.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.adapters.CardAdapterHistory;
import kz.smrtx.techmerch.adapters.CardAdapterRequests;
import kz.smrtx.techmerch.items.entities.History;
import kz.smrtx.techmerch.items.entities.Request;

import kz.smrtx.techmerch.items.viewmodels.HistoryViewModel;
import kz.smrtx.techmerch.items.viewmodels.RequestViewModel;

public class StatusesActivity extends AppCompatActivity {

    private boolean listIsOpen = true;
    private boolean myRequestOpened = false;
    private TextView pageName;

    // <----  list ---->

    private View list;
    private RecyclerView waitingRecycler;
    private RecyclerView advancingRecycler;
    private TextView noRequestsW;
    private TextView noRequestsA;
    private CardView waitingView;
    private CardView advancingView;
    private final List<Request> waitingList = new ArrayList<>();
    private final List<History> advancingList = new ArrayList<>();
    private boolean waitingListIsOpen = false;
    private boolean advancingListIsOpen = false;
    private TextView waitingTextView;
    private TextView advancingTextView;

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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statuses);

        noRequestsW = findViewById(R.id.noRequestsW);
        noRequestsA = findViewById(R.id.noRequestsA);
        waitingRecycler = findViewById(R.id.waitingRequestsRecycler);
        advancingRecycler = findViewById(R.id.advancingRequestsRecycler);
        waitingTextView = findViewById(R.id.waitingTextView);
        advancingTextView = findViewById(R.id.advancingTextView);

        requestViewModel = new ViewModelProvider(this).get(RequestViewModel.class);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        getWaitingRequests();
        getAdvancingRequests();

        pageName = findViewById(R.id.pageName);
        TextView bottomBarText = findViewById(R.id.bottomBarText);
        View back = findViewById(R.id.back);
        CardView waitingButton = findViewById(R.id.waitingRequests);
        CardView advancingButton = findViewById(R.id.advancingRequests);
        ImageView waitingArrowIcon = findViewById(R.id.waitingArrowIcon);
        ImageView advancingArrowIcon = findViewById(R.id.advancingArrowIcon);
        waitingView = findViewById(R.id.waitingView);
        advancingView = findViewById(R.id.advancingView);
        list = findViewById(R.id.list);
        summary = findViewById(R.id.summary);

        pageName.setText(getResources().getString(R.string.requests_statuses));
        bottomBarText.setText(Ius.readSharedPreferences(this, Ius.BOTTOM_BAR_TEXT));

        initializeSummaryStuff();

        waitingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (waitingListIsOpen) {
                    waitingArrowIcon.setRotation(180);
                    waitingView.setVisibility(View.GONE);
                    waitingListIsOpen = false;
                }
                else {
                    waitingArrowIcon.setRotation(-90);
                    waitingView.setVisibility(View.VISIBLE);
                    waitingListIsOpen = true;
                }
            }
        });

        advancingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (advancingListIsOpen) {
                    advancingArrowIcon.setRotation(180);
                    advancingView.setVisibility(View.GONE);
                    advancingListIsOpen = false;
                }
                else {
                    advancingArrowIcon.setRotation(-90);
                    advancingView.setVisibility(View.VISIBLE);
                    advancingListIsOpen = true;
                }
            }
        });

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // openDialog r u sure?
            }
        });

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
    }

    @SuppressLint("SetTextI18n")
    private void getWaitingRequests() {
        int userCode = Integer.parseInt(Ius.readSharedPreferences(this, Ius.USER_CODE));
        Log.i("gettingRequests from", String.valueOf(userCode));
        requestViewModel.getRequestsByAppointed(userCode)
                .observe(this, w -> {
                    waitingList.addAll(w);
                    setAdapter(waitingRecycler, noRequestsW, true);
                    waitingTextView.setText(getResources().getString(R.string.request_waiting_status) + " (" + waitingList.size() + ")");
                });
    }

    @SuppressLint("SetTextI18n")
    private void getAdvancingRequests() {
        int userCode = Integer.parseInt(Ius.readSharedPreferences(this, Ius.USER_CODE));
        historyViewModel.getHistoryListWhichAreRelatedTo(userCode)
            .observe(this, a -> {
                advancingList.addAll(a);
                setAdapter(advancingRecycler, noRequestsA, false);
                advancingTextView.setText(getResources().getString(R.string.request_advancing_status) + " (" + advancingList.size() + ")");
            });
    }

    private void setAdapter(RecyclerView recyclerView, TextView noRequests, boolean waiting) {
        RecyclerView.LayoutManager layoutManager;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (waiting) {
            if (waitingList.isEmpty()) {
                noRequests.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            else { // while db is loading, first case triggers. Need to set visibility back if it loads the data after
                noRequests.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            CardAdapterRequests cardAdapterRequests = new CardAdapterRequests(waitingList, this);
            recyclerView.setAdapter(cardAdapterRequests);
            cardAdapterRequests.setOnItemClickListener(new CardAdapterRequests.onItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    request = waitingList.get(position);
                    myRequestOpened = true;
                    setRequest();
                    requestLoading.setVisibility(View.GONE);
                    closeList();
                }
            });
        }
        else {
            if (advancingList.isEmpty()) {
                noRequests.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            else {
                noRequests.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            CardAdapterHistory cardAdapterHistory = new CardAdapterHistory(advancingList, this);
            recyclerView.setAdapter(cardAdapterHistory);
            cardAdapterHistory.setOnItemClickListener(new CardAdapterHistory.onItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    getRequest(advancingList.get(position).getRequestCode());
                    myRequestOpened = false;
                    closeList();
                }
            });
        }
    }

    private void getRequest(String code) {
        requestViewModel.getRequestByCode(code).observe(this, r -> {
            if (r!=null) {
                requestLoading.setVisibility(View.GONE);
                request = r;
                setRequest();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setRequest() {
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

        appointed.setText(
                Ius.makeTextBold(this, getResources().getString(R.string.appointed_on) + ": " + request.getREQ_USE_NAME_APPOINTED() + " " +
                        getResources().getString(R.string.from_) + " " + dateUpdated)
        );

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

    private void closeList() {
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

    private boolean isNull(String text) {
        if (text==null)
            return true;
        return text.length() == 0;
    }

    private void createToast(String text, boolean success) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, (ViewGroup) findViewById(R.id.toast));
        Ius.showToast(layout, this, text, success);
    }
}