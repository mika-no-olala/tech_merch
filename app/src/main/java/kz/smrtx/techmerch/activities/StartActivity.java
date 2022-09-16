package kz.smrtx.techmerch.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.sqlite.db.SimpleSQLiteQuery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kz.smrtx.techmerch.BuildConfig;
import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.items.viewmodels.SessionViewModel;

public class StartActivity extends AppCompatActivity {

    private final Context context = this;
    private TextView name;
    private TextView role;
    private TextView bottomBarText;
    private SessionViewModel sessionViewModel;

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
        sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);
        sessionViewModel.deleteAllSessions();

        check();

        Ius.checkPermissions(this);

        startWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivitySession();
            }
        });

        synchronization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivitySync();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivitySettings();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ius.writeSharedPreferences(context, Ius.DATE_LOGIN, "");
                finish();
            }
        });
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

    private void check() {
        double lat = 50;
        double lon = 50;
        double latPoint = 100;
        double lonPoint = 100;
        int distance = 0;

        double latKM = Math.abs((latPoint - lat)*111.32);
        double lonKM = Math.abs(lonPoint - lon) * 40075 * Math.cos(Math.abs(latPoint-lat))/360;
        distance = (int) Math.pow(Math.pow(latKM, 2) + Math.pow(lonKM, 2), 0.5);

        Log.e("sss", String.valueOf(distance));
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
    }
}