package kz.smrtx.techmerch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    private CardView startWork;
    private CardView synchronization;
    private CardView settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startWork = findViewById(R.id.startWork);
        synchronization = findViewById(R.id.synchronization);
        settings = findViewById(R.id.settings);

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