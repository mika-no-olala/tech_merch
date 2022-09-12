package kz.smrtx.techmerch.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.items.reqres.synctables.SyncTables;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartActivity extends AppCompatActivity {

    private final Context context = this;
    private CardView startWork;
    private CardView synchronization;
    private CardView settings;
    private TextView name;
    private TextView role;
    private TextView bottomBarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startWork = findViewById(R.id.startWork);
        synchronization = findViewById(R.id.synchronization);
        settings = findViewById(R.id.settings);
        name = findViewById(R.id.name);
        role = findViewById(R.id.role);
        bottomBarText = findViewById(R.id.bottomBarText);
        Button signOut = findViewById(R.id.signOut);

        setUser();

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

        Ius.setBottomText(bottomBarText, context);
    }

    private void check() {
        String code = Ius.readSharedPreferences(context, Ius.USER_CODE);
        Log.e("user code", code);

        Ius.getApiService().getSyncTables(code).enqueue(new Callback<SyncTables>() {
            @Override
            public void onResponse(Call<SyncTables> call, Response<SyncTables> response) {
                if (!response.isSuccessful()) {
                    Log.e("sss", "not successful");
                    return;
                }
                Log.e("sss", String.valueOf(response.body().getData().size()));
                Log.e("sss", String.valueOf(response.body().getData().get(0).getMVLVIEWNAME()));
            }

            @Override
            public void onFailure(Call<SyncTables> call, Throwable t) {
                Log.e("sss", t.getMessage());
            }
        });
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