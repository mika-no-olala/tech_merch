package kz.smrtx.techmerch.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.fragments.OISalePointFragment;
import kz.smrtx.techmerch.fragments.OITechnicFragment;
import kz.smrtx.techmerch.fragments.OperationsFragment;
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.entities.Visit;
import kz.smrtx.techmerch.items.viewmodels.ChoosePointsViewModel;
import kz.smrtx.techmerch.items.viewmodels.HistoryViewModel;
import kz.smrtx.techmerch.items.viewmodels.RequestViewModel;
import kz.smrtx.techmerch.items.viewmodels.UserViewModel;
import kz.smrtx.techmerch.items.viewmodels.VisitViewModel;
import kz.smrtx.techmerch.utils.GPSTracker;

public class OutletInformationActivity extends AppCompatActivity implements OISalePointFragment.FragmentListener, OITechnicFragment.FragmentListener {

    private String scenario;
    private String salePointCode;
    private Visit visit;
    private Request request;
    private RequestViewModel requestViewModel;
    private HistoryViewModel historyViewModel;
    private ChoosePointsViewModel choosePointsViewModel;
    private VisitViewModel visitViewModel;
    private UserViewModel userViewModel;
    private ArrayList<String> pageNames = new ArrayList<>();

    private ScrollView scrollView;
    private TextView pageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_information);

        Bundle arguments = getIntent().getExtras();

        salePointCode = arguments.get("salePointCode").toString();

        scenario = arguments.get("scenario").toString();
        if (scenario.equals("technic")) {
            requestViewModel = new ViewModelProvider(this).get(RequestViewModel.class);
            historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
            visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);
            choosePointsViewModel = new ViewModelProvider(this).get(ChoosePointsViewModel.class);
            userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
            getRequest(arguments.get("requestCode").toString());
        }
        pageName = findViewById(R.id.pageName);
        scrollView = findViewById(R.id.scrollView);
        View back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void getPageName(String name) {
        pageName.setText(name);
        pageNames.add(name);
    }

    public void openSalePoint() {
        OISalePointFragment oiSalePointFragment = OISalePointFragment.getInstance(salePointCode, scenario, request.getREQ_CODE());
        getSupportFragmentManager().beginTransaction().replace(R.id.container, oiSalePointFragment)
                .addToBackStack(null).commit();
    }

    public void openFragment(Fragment fragment, boolean scrollUp) {
        Fragment previous = getSupportFragmentManager().findFragmentById(R.id.container);

        getSupportFragmentManager().beginTransaction().hide(previous).add(R.id.container, fragment)
                .addToBackStack(null).commit();
        if (scrollUp)
            scrollView.post(() -> scrollView.fullScroll(View.FOCUS_UP));
    }

    public void createVisit() {
        choosePointsViewModel.getSalePointByCode(salePointCode).observe(this, s -> {
            if (s!=null) {
                GPSTracker gps = new GPSTracker(this);
                String lat = "0";
                String lon = "0";
                if (gps.getIsGPSTrackingEnabled()) {
                    lat = String.valueOf(gps.getLatitude());
                    lon = String.valueOf(gps.getLongitude());
                }

                visit = new Visit();
                String started = Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss");
                String number = Ius.generateUniqueCode(this, "v");

                visit.setVIS_NUMBER(number);
                visit.setVIS_USE_CODE(Integer.parseInt(Ius.readSharedPreferences(this, Ius.USER_CODE)));
                visit.setVIS_SAL_CODE(Integer.parseInt(salePointCode));
                visit.setVIS_SAL_ID(s.getId());
                visit.setVIS_START_DATE(started);
                visit.setVIS_CREATED(started);
                visit.setVIS_DEVICE_ID(Ius.readSharedPreferences(this, Ius.DEVICE_ID));
                visit.setVIS_LATITUDE(lat);
                visit.setVIS_LONGITUDE(lon);
                visit.setVIS_SES_CODE(Ius.readSharedPreferences(this, Ius.LAST_SESSION_CODE));

                visitViewModel.insert(visit);
            }
        });
    }

    private void finishVisit() {
        String finished = Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss");
        visit.setVIS_FINISH_DATE(finished);
        visitViewModel.update(visit);
    }

    private void getRequest(String requestCode) {
        requestViewModel.getRequestByCode(requestCode).observe(this, r -> {
            if (r!=null) {
                request = r;
                openSalePoint();
            }
        });
    }

    public void sendRequest(String comment, boolean accept) {
        if (accept)
            new GetData(this, historyViewModel).execute();
        else
            new GetDataUser(this, userViewModel).execute();

        request.setREQ_COMMENT(comment);
    }

    private void setGeneralData() {
        request.setREQ_USE_CODE(Integer.parseInt(Ius.readSharedPreferences(this, Ius.USER_CODE)));
        request.setREQ_UPDATED(Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss"));

        request.setNES_TO_UPDATE("yes");

        requestViewModel.update(request);
        createToast(getResources().getString(R.string.request_saved), true);
        finish();
    }

    @SuppressLint("StaticFieldLeak")
    private class GetData extends AsyncTask<Void, Void, Void> {
        HistoryViewModel historyViewModel;
        Activity context;

        public GetData(Activity context, HistoryViewModel historyViewModel) {
            this.context = context;
            this.historyViewModel = historyViewModel;
        }

        @Override
        protected void onPostExecute(Void unused) {
            request.setREQ_STA_ID(3);
            setGeneralData();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            check();
            return null;
        }

        @SuppressLint("Range")
        public void check() {
            int tmrCode = historyViewModel.getTMRCodeByRequest(request.getREQ_CODE());
            request.setREQ_USE_CODE_APPOINTED(tmrCode);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetDataUser extends AsyncTask<Void, Void, Void> {
        UserViewModel userViewModel;
        Activity context;

        public GetDataUser(Activity context, UserViewModel userViewModel) {
            this.context = context;
            this.userViewModel = userViewModel;
        }

        @Override
        protected void onPostExecute(Void unused) {
            request.setREQ_USE_CODE_APPOINTED(request.getREQ_USE_CODE());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            setStatusByRole();
            return null;
        }

        public void setStatusByRole() {
            int userRole = userViewModel.getUserRole(request.getREQ_USE_CODE());
            Log.e("sss role", String.valueOf(userRole));
            if (userRole==5)
                request.setREQ_STA_ID(8);
            if (userRole==6)
                request.setREQ_STA_ID(7);
        }
    }

    @Override
    public void onBackPressed() {
        int fragmentIndex = getSupportFragmentManager().getBackStackEntryCount();
        String pageNameStr = pageName.getText().toString();
        if (pageNameStr.equals(getResources().getString(R.string.outlet_information))) {
            finish();
            return;
        }
        if (pageNameStr.equals(getResources().getString(R.string.request_completion)))
            finishVisit();

        getSupportFragmentManager().popBackStack(fragmentIndex - 1, 0);
        pageName.setText(pageNames.get(fragmentIndex - 1));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishVisit();
    }

    private void createToast(String text, boolean success) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, (ViewGroup) findViewById(R.id.toast));
        Ius.showToast(layout, this, text, success);
    }
}