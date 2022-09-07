package kz.smrtx.techmerch.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.api.StringQuery;
import kz.smrtx.techmerch.items.reqres.StringResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        View back = findViewById(R.id.back);
        LinearLayout password = findViewById(R.id.password);
        LinearLayout language = findViewById(R.id.language);
        TextView deviceIdCard = findViewById(R.id.deviceIdCard);
        TextView pageName = findViewById(R.id.pageName);
        pageName.setText(getResources().getText(R.string.settings));
        deviceIdCard.setText(Ius.readSharedPreferences(context, Ius.DEVICE_ID));

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogPassword();
            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogLanguage();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void openDialogPassword() {
        Dialog dialog = Ius.createDialog(context, R.layout.dialog_window_password);

        EditText old = dialog.findViewById(R.id.passwordOld);
        EditText newP = dialog.findViewById(R.id.passwordNew);
        EditText newR = dialog.findViewById(R.id.passwordNewRepeat);
        Button change = dialog.findViewById(R.id.positive);
        dialog.show();

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
    }

    private void openDialogLanguage() {
        Dialog dialog = Ius.createDialog(context, R.layout.dialog_window_language);

        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
        Button apply = dialog.findViewById(R.id.positive);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // soon
//                Locale myLocale = new Locale(lang);
//                Resources res = getResources();
//                DisplayMetrics dm = res.getDisplayMetrics();
//                Configuration conf = res.getConfiguration();
//                conf.locale = myLocale;
//                res.updateConfiguration(conf, dm);
//                Intent refresh = new Intent(this, AndroidLocalize.class);
//                finish();
//                startActivity(refresh);
            }
        });
    }

    private void createToast(String text, boolean success) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, (ViewGroup) findViewById(R.id.toast));
        Ius.showToast(layout, context, text, success);
    }
}