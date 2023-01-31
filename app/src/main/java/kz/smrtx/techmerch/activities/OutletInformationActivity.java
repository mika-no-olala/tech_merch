package kz.smrtx.techmerch.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.fragments.OISalePointFragment;
import kz.smrtx.techmerch.fragments.OITechnicFragment;
import kz.smrtx.techmerch.fragments.OperationsFragment;
import kz.smrtx.techmerch.items.entities.Photo;
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.entities.Visit;
import kz.smrtx.techmerch.items.entities.Warehouse;
import kz.smrtx.techmerch.items.viewmodels.ChoosePointsViewModel;
import kz.smrtx.techmerch.items.viewmodels.HistoryViewModel;
import kz.smrtx.techmerch.items.viewmodels.PhotoViewModel;
import kz.smrtx.techmerch.items.viewmodels.RequestViewModel;
import kz.smrtx.techmerch.items.viewmodels.UserViewModel;
import kz.smrtx.techmerch.items.viewmodels.VisitViewModel;
import kz.smrtx.techmerch.items.viewmodels.WarehouseViewModel;
import kz.smrtx.techmerch.utils.Aen;
import kz.smrtx.techmerch.utils.GPSTracker;
import kz.smrtx.techmerch.utils.LocaleHelper;

public class OutletInformationActivity extends AppCompatActivity implements OISalePointFragment.FragmentListener, OITechnicFragment.FragmentListener {

    private String scenario;
    private String salePointCode;
    private Visit visit;
    private Request request;
    private Warehouse warehouse;
    private RequestViewModel requestViewModel;
    private HistoryViewModel historyViewModel;
    private ChoosePointsViewModel choosePointsViewModel;
    private VisitViewModel visitViewModel;
    private UserViewModel userViewModel;
    private PhotoViewModel photoViewModel;
    private WarehouseViewModel warehouseViewModel;
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
            photoViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);
            warehouseViewModel = new ViewModelProvider(this).get(WarehouseViewModel.class);
            getRequest(arguments.get("requestCode").toString());
        }
        else if(scenario.equals("detail")) {
            openSalePoint("");
        }
        pageName = findViewById(R.id.pageName);
        scrollView = findViewById(R.id.scrollView);
        View back = findViewById(R.id.back);
        TextView bottomBarText = findViewById(R.id.bottomBarText);
        bottomBarText.setText(Ius.readSharedPreferences(this, Ius.BOTTOM_BAR_TEXT));

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

    public void openSalePoint(String requestCode) {
        OISalePointFragment oiSalePointFragment = OISalePointFragment.getInstance(salePointCode, scenario, requestCode);
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
                visit.setVIS_SESSION_CODE(Ius.readSharedPreferences(this, Ius.LAST_SESSION_CODE));

                visitViewModel.insert(visit);
            }
        });
    }

    private void finishVisit() {
        if (scenario.equals("technic") && visit!=null) {
            String finished = Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss");
            visit.setVIS_FINISH_DATE(finished);
            visitViewModel.update(visit);
        }
    }

    private void getRequest(String requestCode) {
        requestViewModel.getRequestByCode(requestCode).observe(this, r -> {
            if (r!=null) {
                request = r;
                if (isReplaceCase())
                    getWarehouse();
                openSalePoint(request.getREQ_CODE());
            }
        });
    }

    private void getWarehouse() {
        warehouseViewModel.getWarehouseByIdAndEquipment(request.getREQ_EQU_SUBTYPE(), request.getREQ_SECONDARY_ADDRESS_ID()).observe(this, w -> {
            if (w.size()==0) {
                SessionActivity.getInstance().createToast(getString(R.string.no_warehouse_error), false);
                Log.e("getWarehouse", "no warehouse!");
                finish();
                return;
            }
            warehouse = w.get(0);
            Log.i("getWarehouse", "got an object " + warehouse.getWAC_WAR_NAME());
        });
    }

    public void sendRequest(String comment, String[] photos, boolean accept) {
        List<Photo> photoList = new ArrayList<>();
        String userCode = String.valueOf(request.getREQ_USE_CODE_APPOINTED());
        int roleCode = Integer.parseInt(Ius.readSharedPreferences(this, Ius.USER_ROLE_CODE));

        for (String s : photos) {
            if (s==null)
                continue;

            if (s.length()>0) {
                photoList.add(new Photo(s, request.getREQ_CODE(), s + ".jpg", userCode, roleCode, "yes"));
            }
        }
        if (photoList.size()>0)
            photoViewModel.insertPhotos(photoList);

        request.setREQ_COMMENT(Ius.saveApostrophe(comment));
        Log.i("Convert comment", comment + " -> " + request.getREQ_COMMENT());

        if (accept)
            new GetDataIfAccept(this, historyViewModel).execute();
        else
            new GetDataIfNoAccept(this, userViewModel).execute();
    }

    private void setGeneralData() {
        request.setREQ_USE_CODE(Integer.parseInt(Ius.readSharedPreferences(this, Ius.USER_CODE)));
        request.setREQ_UPDATED(Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss"));
        request.setREQ_VIS_NUMBER(visit.getVIS_NUMBER());
        request.setNES_TO_UPDATE("yes");

        requestViewModel.update(request);

        SessionActivity.getInstance().startRequestSending();
        SessionActivity.getInstance().cancelSessionClear();
        finish();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class GetDataIfAccept extends AsyncTask<Void, Void, Void> {
        HistoryViewModel historyViewModel;
        Activity context;

        public GetDataIfAccept(Activity context, HistoryViewModel historyViewModel) {
            this.context = context;
            this.historyViewModel = historyViewModel;
        }

        @Override
        protected void onPostExecute(Void unused) {
            request.setREQ_STA_ID(Aen.STATUS_WAITING_TMR);
            if (isReplaceCase())
                replaceCase();

            setGeneralData();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int tmrCode = historyViewModel.getTMRCodeByRequest(request.getREQ_CODE());
            request.setREQ_USE_CODE_APPOINTED(tmrCode);
            return null;
        }

        private void replaceCase() {
            if (request.getREQ_REPLACE_ID()==Aen.REPLACE_VALUE_FROM_STORAGE)
                warehouse.setWAC_CHANGES(-1);
            else if(request.getREQ_REPLACE_ID()==Aen.REPLACE_VALUE_TO_STORAGE)
                warehouse.setWAC_CHANGES(1);
            warehouse.setWAC_USE_CODE(Ius.readSharedPreferences(context, Ius.USER_CODE));
            warehouse.setNES_TO_UPDATE("yes");
            warehouseViewModel.update(warehouse);
        }
    }

    private boolean isReplaceCase() {
        return request.getREQ_REPLACE_ID()==Aen.REPLACE_VALUE_TO_STORAGE ||
                request.getREQ_REPLACE_ID()==Aen.REPLACE_VALUE_FROM_STORAGE;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class GetDataIfNoAccept extends AsyncTask<Void, Void, Void> {
        UserViewModel userViewModel;
        Activity context;

        public GetDataIfNoAccept(Activity context, UserViewModel userViewModel) {
            this.context = context;
            this.userViewModel = userViewModel;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int userRole = userViewModel.getUserRole(request.getREQ_USE_CODE());
            request.setREQ_STA_ID(Aen.getCancelStatusAfterTechnic(userRole));
            request.setREQ_USE_CODE_APPOINTED(request.getREQ_USE_CODE());
            setGeneralData();
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        int fragmentIndex = getSupportFragmentManager().getBackStackEntryCount() - 1;
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}