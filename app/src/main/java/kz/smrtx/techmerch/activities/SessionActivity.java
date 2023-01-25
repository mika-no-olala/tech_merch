package kz.smrtx.techmerch.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import kz.smrtx.techmerch.BuildConfig;
import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.fragments.AllReportsFragment;
import kz.smrtx.techmerch.fragments.MapsFragment;
import kz.smrtx.techmerch.fragments.NotesFragment;
import kz.smrtx.techmerch.fragments.OperationsFragment;
import kz.smrtx.techmerch.fragments.OperationsOnOutletFragment;
import kz.smrtx.techmerch.fragments.OutletInformationFragment;
import kz.smrtx.techmerch.fragments.OutletsFragment;
import kz.smrtx.techmerch.fragments.TechnicReportFragment;
import kz.smrtx.techmerch.fragments.WarehouseJournalFragment;
import kz.smrtx.techmerch.fragments.WarehousesFragment;
import kz.smrtx.techmerch.items.entities.Session;
import kz.smrtx.techmerch.items.viewmodels.SessionViewModel;
import kz.smrtx.techmerch.utils.LocaleHelper;
import kz.smrtx.techmerch.utils.RequestSender;

public class SessionActivity extends AppCompatActivity implements OperationsFragment.FragmentListener, OutletsFragment.FragmentListener,
        OutletInformationFragment.FragmentListener, OperationsOnOutletFragment.FragmentListener, NotesFragment.FragmentListener,
        MapsFragment.FragmentListener, TechnicReportFragment.FragmentListener, AllReportsFragment.FragmentListener,
        WarehousesFragment.FragmentListener, WarehouseJournalFragment.FragmentListener {

    private static SessionActivity instance;
    private TextView pageName;
    private ScrollView scrollView;
    private ArrayList<String> pageNames = new ArrayList<>();
    private String dateStarted;
    private SessionViewModel sessionViewModel;
    private Session session;
    private int fragmentIndex = 0;
    private static boolean deleteSession;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        dateStarted = Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss");
        deleteSession = true;

        openOperations();

        TextView bottomBarText = findViewById(R.id.bottomBarText);
        scrollView = findViewById(R.id.scrollView);
        pageName = findViewById(R.id.pageName);
        View back = findViewById(R.id.back);
        sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);
        bottomBarText.setText(Ius.readSharedPreferences(this, Ius.BOTTOM_BAR_TEXT));

        generateSession();

        back.setOnClickListener(view -> onBackPressed());
    }

    public SessionActivity() {
        instance = this;
    }

    public static SessionActivity getInstance() {
        return instance;
    }

    @Override
    public void getPageName(String name) {
        pageName.setText(name);
        pageNames.add(name);
    }

    public void generateSession() {
        session = new Session();
        String code = Ius.generateUniqueCode(this, "s");
        Ius.writeSharedPreferences(this, Ius.LAST_SESSION_CODE, code);
        session.setSES_CODE(code);
        session.setSES_STARTED(dateStarted);
        session.setSES_USE_ID(Integer.parseInt(Ius.readSharedPreferences(this, Ius.USER_ID)));
        session.setSES_APP_VERSION(BuildConfig.VERSION_NAME);
        sessionViewModel.insert(session);
    }

    public void openOperations() {
        OperationsFragment operationsFragment = OperationsFragment.getInstance(dateStarted);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerSession, operationsFragment)
                .addToBackStack(null).commit();
    }

    public void openFragment(Fragment fragment, boolean scrollUp) {
        Fragment previous = getSupportFragmentManager().findFragmentById(R.id.containerSession);

        getSupportFragmentManager().beginTransaction().hide(previous).add(R.id.containerSession, fragment)
                .addToBackStack(null).commit();
        if (scrollUp)
            scrollView.post(() -> scrollView.fullScroll(View.FOCUS_UP));
    }

    public void openActivityCreateRequest() {
        Intent intent = new Intent(this, CreateRequestActivity.class);
        startActivity(intent);
    }

    public void openActivityStatuses() {
        Intent intent = new Intent(this, StatusesActivity.class);
        startActivity(intent);
    }

    public void openActivitySync() {
        Intent intent = new Intent(this, SyncActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fragmentIndex = getSupportFragmentManager().getBackStackEntryCount();
        if (fragmentIndex<1) {
            if (deleteSession) {
                finish();
                return;
            }
            openDialog();
            openOperations();
        }
        else {
//            Log.e("ssssize", pageNames.toString());
//            Log.e("sssse", String.valueOf(fragmentIndex));
            pageNames.remove(fragmentIndex);
            getSupportFragmentManager().popBackStack(fragmentIndex - 1, 0);
            pageName.setText(pageNames.get(fragmentIndex - 1));
        }
    }

    public void openDialog() {
        Dialog dialog = Ius.createDialogAcception(this, getResources().getString(R.string.finishing_work),
                getResources().getString(R.string.finishing_work_question), true);

        Button yes = dialog.findViewById(R.id.positive);
        Button no = dialog.findViewById(R.id.negative);

        dialog.show();

        no.setOnClickListener(view -> dialog.cancel());
        yes.setOnClickListener(view -> {
            session.setSES_FINISHED(Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss"));
            sessionViewModel.update(session);
            dialog.cancel();
            finish();
            openActivitySync();
        });
    }

    public boolean checkPermissions() {
        return Ius.checkPermissionsCamera(this);
    }

    public void cancelSessionClear() {
        deleteSession = false;
    }

    public void startRequestSending() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ComponentName componentName = new ComponentName(this, RequestSender.class);
            JobInfo jobInfo = new JobInfo.Builder(55, componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .build();

            JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            int resultCode = scheduler.schedule(jobInfo);
            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Log.i("startRequestSending", "Job success");
                createToast(getResources().getString(R.string.request_has_send), true);
            }
            else {
                Log.e("startRequestSending", "Job failed");
                createToast(getResources().getString(R.string.request_created_not_sended), true);
            }
        }
        else
            createToast(getResources().getString(R.string.request_success), true);
    }

    public void createToast(String text, boolean success) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, (ViewGroup) findViewById(R.id.toast));
        Ius.showToast(layout, this, text, success);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!deleteSession)
            Log.w("SessionActivity", "Action was registered, session will not be deleted");
        else
            Log.w("SessionActivity", "No action, session will be deleted");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (deleteSession)
            sessionViewModel.delete(session);
    }
}