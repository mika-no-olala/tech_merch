package kz.smrtx.techmerch.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.adapters.CardAdapterHistory;
import kz.smrtx.techmerch.adapters.CardAdapterRequests;
import kz.smrtx.techmerch.adapters.FragmentAdapter;
import kz.smrtx.techmerch.fragments.RSRequestFragment;
import kz.smrtx.techmerch.fragments.RSWaitingFragment;
import kz.smrtx.techmerch.items.entities.History;
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.viewmodels.ChoosePointsViewModel;
import kz.smrtx.techmerch.items.viewmodels.HistoryViewModel;
import kz.smrtx.techmerch.items.viewmodels.PhotoViewModel;
import kz.smrtx.techmerch.items.viewmodels.RequestViewModel;
import kz.smrtx.techmerch.items.viewmodels.UserViewModel;
import kz.smrtx.techmerch.utils.Aen;
import kz.smrtx.techmerch.utils.RequestSync;

public class RequestStatusesActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private FragmentAdapter adapter;
    public static RequestStatusesActivity instance;

    private RequestViewModel requestViewModel;
    private HistoryViewModel historyViewModel;
    private PhotoViewModel photoViewModel;
    private UserViewModel userViewModel;
    private ChoosePointsViewModel choosePointsViewModel;
    private SwipeRefreshLayout refresher;

    private int userCode;
    private int salePointCode;
    private List<Request> waitingList;
    private List<History> advancingList;
    private List<History> finishedList;
    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_statuses);

        TextView bottomBarText = findViewById(R.id.bottomBarText);
        TextView pageName = findViewById(R.id.pageName);
        View back = findViewById(R.id.back);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        refresher = findViewById(R.id.refresher);

        setTabLayoutWithPager();

        requestViewModel = new ViewModelProvider(this).get(RequestViewModel.class);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        photoViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        choosePointsViewModel = new ViewModelProvider(this).get(ChoosePointsViewModel.class);

        userCode = Integer.parseInt(Ius.readSharedPreferences(this, Ius.USER_CODE));
        bottomBarText.setText(Ius.readSharedPreferences(this, Ius.BOTTOM_BAR_TEXT));

        Bundle arguments = getIntent().getExtras();
        if (arguments!=null) {
            salePointCode = Integer.parseInt(arguments.getString("salePointCode"));
            pageName.setText(getResources().getString(R.string.requests_statuses_sale_point));
        }
        else {
            pageName.setText(getResources().getString(R.string.requests_statuses));
        }

//        getWaitingRequests(salePointCode);
        getAdvancingRequests(salePointCode);
        getFinishedRequests(salePointCode);

        back.setOnClickListener(view -> onBackPressed());
        refresher.setOnRefreshListener(() -> new RequestSync(refresher));
    }

    public RequestStatusesActivity() {
        instance = this;
    }
    public static RequestStatusesActivity getInstance() {
        return instance;
    }

    private void setTimerCooldown() {
        refresher.setRefreshing(true);
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                new RequestSync(refresher);
            }
        }.start();
    }

//    private void getWaitingRequests(int salePointCode) {
//        Log.i("gettingRequests from", String.valueOf(userCode));
//
//        if (salePointCode!=-1) {
//            requestViewModel.getRequestsByAppointed(userCode, salePointCode)
//                    .observe(this, w -> {
//                        if (w!=null) {
//                            waitingList = w;
//                            RSWaitingFragment fragment = (RSWaitingFragment) adapter.getRsWaiting();
//                            fragment.setWaitingList(w);
//                        }
////                        setAdapter(waitingRecycler, noRequestsW, 1);
////                        waitingTextView.setText(getResources().getString(R.string.request_waiting_status) + " (" + waitingList.size() + ")");
//                        // TODO: set tab name, optimize all 3 methods to one
//                    });
//        }
//        else {
//            requestViewModel.getRequestsByAppointed(userCode)
//                    .observe(this, w -> {
//                        if (w!=null) {
//                            waitingList = w;
//                            RSWaitingFragment fragment = (RSWaitingFragment) adapter.getRsWaiting();
//                            fragment.setWaitingList(w);
//                        }
////                        setAdapter(waitingRecycler, noRequestsW, 1);
////                        waitingTextView.setText(getResources().getString(R.string.request_waiting_status) + " (" + waitingList.size() + ")");
//                    });
//        }
//    }

    @SuppressLint("SetTextI18n")
    private void getAdvancingRequests(int salePointCode) {
        if (salePointCode!=-1) {
            historyViewModel.getHistoryListWhichAreRelatedTo(userCode, salePointCode)
                    .observe(this, a -> {
                        advancingList = a;
                    });
        }
        else {
            historyViewModel.getHistoryListWhichAreRelatedTo(userCode)
                    .observe(this, a -> {
                        advancingList = a;
                    });
        }
    }

    @SuppressLint("SetTextI18n")
    private void getFinishedRequests(int salePointCode) {
        if (salePointCode!=-1) {
            historyViewModel.getHistoryListWhichAreRelatedToAndFinished(userCode, salePointCode)
                    .observe(this, f -> {
                        finishedList = f;
                    });
        }
        else {
            historyViewModel.getHistoryListWhichAreRelatedToAndFinished(userCode)
                    .observe(this, f -> {
                        finishedList = f;
                    });
        }
    }

    public void checkNoRequestsInList(TextView noRequests, RecyclerView recyclerView, boolean listIsEmpty) {
        if (listIsEmpty) {
            noRequests.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            noRequests.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void setAdapterWaiting(RecyclerView recyclerView, TextView noRequests, List<Request> waitingList) {
        RecyclerView.LayoutManager layoutManager;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        boolean isTechnic = Ius.readSharedPreferences(this, Ius.USER_ROLE_CODE).equals(Aen.ROLE_TECHNIC);

        // while db is loading, /first case/ triggers. Need to set visibility back if it loads the data after
//        RequestStatusesActivity.getInstance().checkNoRequestsInList(noRequests, recyclerView, waitingList.isEmpty());

        CardAdapterRequests cardAdapterRequests = new CardAdapterRequests(waitingList, this);
        recyclerView.setAdapter(cardAdapterRequests);
        cardAdapterRequests.setOnItemClickListener(position -> {
            request = waitingList.get(position);
            Log.e("sssR", request.toString());
            viewPager2.setCurrentItem(0);
            RSRequestFragment requestFragment = (RSRequestFragment) adapter.getRsRequest();
            requestFragment.setRequest(request);
        });
    }

    private void setTabLayoutWithPager() {
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.request_waiting_status_short)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.request_advancing_status_short)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.request_finished_status_short)));

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new FragmentAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition()+1);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position-1));
            }
        });

        viewPager2.setCurrentItem(1);

        // warning: 0 page is RequestFragment. Here is minus position on viewPagerListener and plus position on TabListener
        // otherwise they would be out of sync
        // viewPager --> 0-request, 1-waiting, 2-advancing, 3-finished
        // tabLayout --> 0-waiting, 1-advancing, 2-finished
        // These all were needed to avoid null exception when request is chosen. When request page is on position 3,
        // viewPager doesn't create it at the beginning. So I need to create request page first
    }

    public int getSalePointCode() {
        return salePointCode;
    }
}