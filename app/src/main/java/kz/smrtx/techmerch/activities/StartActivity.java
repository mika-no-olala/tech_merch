package kz.smrtx.techmerch.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.sqlite.db.SimpleSQLiteQuery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import kz.smrtx.techmerch.BuildConfig;
import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.items.viewmodels.SessionViewModel;
import kz.smrtx.techmerch.utils.Aen;
import kz.smrtx.techmerch.utils.CheckEquipmentSupply;
import kz.smrtx.techmerch.utils.LocaleHelper;

public class StartActivity extends AppCompatActivity {

    private final Context context = this;
    private TextView name;
    private TextView role;
    private TextView bottomBarText;
    private SessionViewModel sessionViewModel;
    private static StartActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        CardView startWork = findViewById(R.id.startWork);
        CardView synchronization = findViewById(R.id.synchronization);
        CardView settings = findViewById(R.id.settings);
        name = findViewById(R.id.name);
        role = findViewById(R.id.role);
        bottomBarText = findViewById(R.id.bottomBarText);
        Button signOut = findViewById(R.id.signOut);

        setUser();
        if (Integer.parseInt(Ius.readSharedPreferences(this, Ius.USER_ROLE_CODE)) == Aen.ROLE_MANAGER)
            checkSupply();

        sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);

        Ius.checkPermissions(this);
        Ius.checkPermissionsCamera(this);

        Bundle arguments = getIntent().getExtras();

        if (arguments!=null) {
            if (arguments.getBoolean("SYNC"))
                openActivitySync();
        }

        startWork.setOnClickListener(startWorkView -> openActivitySession());

        synchronization.setOnClickListener(synchronizationView -> openActivitySync());

        settings.setOnClickListener(settingsView -> openActivitySettings());

        signOut.setOnClickListener(signOutView -> {
            Ius.writeSharedPreferences(context, Ius.DATE_LOGIN, "");
            onBackPressed();
        });
    }

    public StartActivity() {
        instance = this;
    }

    public static StartActivity getInstance() {
        return instance;
    }

    private void checkSupply() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ComponentName componentName = new ComponentName(this, CheckEquipmentSupply.class);
            JobInfo jobInfo = new JobInfo.Builder(22, componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setPeriodic(1000 * 60 * 15)
                    .setPersisted(true)
                    .build();

            JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            int resultCode = scheduler.schedule(jobInfo);
            if (resultCode == JobScheduler.RESULT_SUCCESS)
                Log.e("startCheckingSupply", "Job success");
            else
                Log.i("startCheckingSupply", "Job failed");
        }
    }

    private void setUser() {
        name.setText(Ius.readSharedPreferences(context, Ius.USER_NAME));
        role.setText(Ius.readSharedPreferences(context, Ius.USER_ROLE_NAME));

        String bottomTextStr =
                Ius.readSharedPreferences(context, Ius.USER_ID) + " | " +
                        BuildConfig.VERSION_NAME  + " | " +
                        Ius.readSharedPreferences(context, Ius.DEVICE_ID) + " | " +
                        Ius.readSharedPreferences(context, Ius.DATE_LOGIN);

        Ius.writeSharedPreferences(context, Ius.BOTTOM_BAR_TEXT, bottomTextStr);
        bottomBarText.setText(bottomTextStr);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetSyncData(this, sessionViewModel).execute();
    }

    @SuppressLint("StaticFieldLeak")
    private static class GetSyncData extends AsyncTask<Void, Void, Void> {
        SessionViewModel sessionViewModel;
        Activity context;

        @Override
        protected void onPostExecute(Void aVoid) {

        }
        public GetSyncData(Activity context, SessionViewModel sessionViewModel) {
            this.context = context;
            this.sessionViewModel = sessionViewModel;
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            check();
            return null;
        }

        @SuppressLint("Range")
        public void check() {
            SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM ST_SESSION", null);
            Cursor cursor = sessionViewModel.getCursor(query);
            Log.e("sss cursor", String.valueOf(cursor==null));
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    Log.e("sss count", String.valueOf(cursor.getCount()));
                    while (cursor.moveToNext()) {
                        Log.e("sss start", cursor.getString(cursor.getColumnIndex("started")));
                        Log.e("sss finished", cursor.getString(cursor.getColumnIndex("finished")));
                    }
                }
                cursor.close();
            }
        }
    }

    private void openActivitySession() {
        Intent intent = new Intent(this, SessionActivity.class);
        startActivity(intent);
    }

    private void openActivitySync() {
        Intent intent = new Intent(this, SyncActivity.class);
        startActivity(intent);
    }

    private void openActivitySettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    private void openActivityLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void createToast(String text, boolean success) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, (ViewGroup) findViewById(R.id.toast));
        Ius.showToast(layout, this, text, success);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openActivityLogin();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}