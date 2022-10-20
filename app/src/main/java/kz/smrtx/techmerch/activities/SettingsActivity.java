package kz.smrtx.techmerch.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Locale;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.api.StringQuery;
import kz.smrtx.techmerch.items.reqres.StringResponse;
import kz.smrtx.techmerch.utils.LocaleHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private Context context = this;
    private TextView pageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        View back = findViewById(R.id.back);
        LinearLayout password = findViewById(R.id.password);
        LinearLayout language = findViewById(R.id.language);
        TextView deviceIdCard = findViewById(R.id.deviceIdCard);
        pageName = findViewById(R.id.pageName);
        pageName.setText(getResources().getText(R.string.settings));
        deviceIdCard.setText(Ius.readSharedPreferences(context, Ius.DEVICE_ID));

        password.setOnClickListener(passwordView -> openDialogPassword());
        language.setOnClickListener(languageView -> openDialogLanguage());
        back.setOnClickListener(backView -> onBackPressed());
    }

    private void openDialogPassword() {
        Dialog dialog = Ius.createDialog(context, R.layout.dialog_window_password, "");

        EditText old = dialog.findViewById(R.id.passwordOld);
        EditText newP = dialog.findViewById(R.id.passwordNew);
        EditText newR = dialog.findViewById(R.id.passwordNewRepeat);
        Button change = dialog.findViewById(R.id.positive);
        dialog.show();

        change.setOnClickListener(changeView -> {
            if (Ius.isEmpty(old)) {
                old.setError(getResources().getString(R.string.fill_field));
                return;
            }
            if (Ius.isEmpty(newP)) {
                newP.setError(getResources().getString(R.string.fill_field));
                return;
            }
            if (Ius.isEmpty(newR)) {
                newR.setError(getResources().getString(R.string.fill_field));
                return;
            }
            if (!newP.getText().toString().equals(newR.getText().toString())) {
                createToast(getResources().getString(R.string.password_no_equal), false);
                return;
            }

            Ius.getApiService().query(Ius.readSharedPreferences(context, Ius.TOKEN),
                StringQuery.changePassword(Ius.USER_ID, old.getText().toString().trim(), newP.getText().toString().trim()))
                .enqueue(new Callback<StringResponse>() {
                    @Override
                    public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                        if (!response.isSuccessful()) {
                            Log.e("Settings - password", String.valueOf(response.code()));
                            createToast(getResources().getString(R.string.server_error), false);
                            return;
                        }
                        if(response.body().getMessage()!=null) {
                            if(response.body().getMessage().equals("token expired")) {
                                Log.e("Settings - password", response.body().getMessage());
                                Ius.refreshToken(context);
                                change.callOnClick();
                                return;
                            }
                        }
                        if(response.body().getDataList().get(0).getRows()==0) {
                            Log.w("Settings - password", "wrong old password");
                            createToast(getResources().getString(R.string.password_wrong), false);
                            return;
                        }

                        createToast(getResources().getString(R.string.password_success), true);
                        dialog.cancel();
                    }
                    @Override
                    public void onFailure(Call<StringResponse> call, Throwable t) {
                        Log.e("Settings - password", t.getMessage());
                        createToast(getResources().getString(R.string.error)  + ": " + t.getMessage(), false);
                    }
                });
        });
    }

    private void openDialogLanguage() {
        Dialog dialog = Ius.createDialog(context, R.layout.dialog_window_language, "");

        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
        RadioButton kz = dialog.findViewById(R.id.kz);
        Button apply = dialog.findViewById(R.id.positive);

        if (Ius.readSharedPreferences(this, Ius.USER_LANGUAGE).equals("kk"))
            kz.setChecked(true);

        apply.setOnClickListener(view -> {
            String language;
            if (radioGroup.getCheckedRadioButtonId()==R.id.ru)
                language = "ru";
            else
                language = "kk";

            context = LocaleHelper.setLocale(SettingsActivity.this, language);

//            Locale locale = new Locale(language);
//            Locale.setDefault(locale);
//            Resources resources = context.getResources();
//            Configuration config = resources.getConfiguration();
//            config.setLocale(locale);
//            resources.updateConfiguration(config, resources.getDisplayMetrics());

            dialog.cancel();
            refresh();
        });

        dialog.show();
    }

    private void refresh() {
        Intent refresh = new Intent(this, SettingsActivity.class);
        startActivity(refresh);
        finish();
    }

    private void createToast(String text, boolean success) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, (ViewGroup) findViewById(R.id.toast));
        Ius.showToast(layout, context, text, success);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}