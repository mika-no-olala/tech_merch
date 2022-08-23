package kz.smrtx.techmerch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText login;
    private EditText password;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        Button signIn = findViewById(R.id.signIn);
        forgotPassword = findViewById(R.id.forgotPassword);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSignIn();
            }
        });
    }

    private void doSignIn() {
        if (Ius.isEmpty(login)) {
            login.setError(getString(R.string.fill_field));
            return;
        }
        if (Ius.isEmpty(password)) {
            password.setError(getString(R.string.fill_field));
            return;
        }
        if (!password.getText().toString().trim().equals("123")) {
            createToast(getString(R.string.wrong_data_log_in));
            return;
        }

        Ius.writeSharedPreferences(this, Ius.USER_LOGIN, login.getText().toString().trim());
        openActivityStart();
    }

    private void openActivityStart() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    private void createToast(String text) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, (ViewGroup) findViewById(R.id.toast));
        Ius.showToast(layout, getApplicationContext(), text, false);
    }
}