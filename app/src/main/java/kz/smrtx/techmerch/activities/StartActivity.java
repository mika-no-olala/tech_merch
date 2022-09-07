package kz.smrtx.techmerch.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;

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

    private void openActivitySession() {
        Intent intent = new Intent(this, SessionActivity.class);
        startActivity(intent);
    }

    private void openActivitySettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}