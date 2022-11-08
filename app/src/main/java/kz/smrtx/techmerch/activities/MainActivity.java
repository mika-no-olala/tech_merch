package kz.smrtx.techmerch.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.api.StringQuery;
import kz.smrtx.techmerch.items.entities.User;
import kz.smrtx.techmerch.items.reqres.JsonResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final Context context = this;
    private EditText login;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkTodaysLogin();

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        Button signIn = findViewById(R.id.signIn);
        TextView forgotPassword = findViewById(R.id.forgotPassword);

        Ius.refreshToken(context);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSignIn();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
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

        Ius.getApiService().getUser(Ius.readSharedPreferences(context, Ius.TOKEN), StringQuery.getUser(login.getText().toString().trim(), password.getText().toString().trim()))
            .enqueue(new Callback<JsonResponse>() {
                @SuppressLint("HardwareIds")
                @Override
                public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                    if (!response.isSuccessful()) {
                        Log.e("MainActivity - auth", String.valueOf(response.code()));
                        createToast(getResources().getString(R.string.error)  + ": " + response.code());
                        return;
                    }

                    if (response.body().getDataList()==null) {
                        Log.e("MainActivity - getUser", "dataList is null");
                        createToast(getResources().getString(R.string.error)  + ": " + response.code());
                        return;
                    }

                    if (response.body().getDataList().isEmpty()) {
                        createToast(getResources().getString(R.string.wrong_data_log_in));
                        return;
                    }

                    User user = response.body().getDataList().get(0);
                    Ius.writeSharedPreferences(context, Ius.USER_ID, String.valueOf(user.getId()));
                    Ius.writeSharedPreferences(context, Ius.USER_CODE, String.valueOf(user.getCode()));
                    Ius.writeSharedPreferences(context, Ius.USER_NAME, user.getName());
                    Ius.writeSharedPreferences(context, Ius.USER_ROLE_CODE, String.valueOf(user.getRoleCode()));
                    Ius.writeSharedPreferences(context, Ius.USER_ROLE_NAME, user.getRoleName());
                    Ius.writeSharedPreferences(context, Ius.DEVICE_ID, Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                    Ius.writeSharedPreferences(context, Ius.DATE_LOGIN, Ius.getDateByFormat(new Date(), "dd.MM.yyyy"));

                    getUserCity(user.getCode());

                    openActivityStart(true);
                }

                @Override
                public void onFailure(Call<JsonResponse> call, Throwable t) {
                    Log.e("MainActivity - auth", t.getMessage());
                    if(t.getMessage().contains("certification")) {
                        createToast(getResources().getString(R.string.error_certification));
                        return;
                    }
                    createToast(getResources().getString(R.string.error)  + ": " + t.getMessage());
                }
            });
    }

    private void getUserCity(int userCode) {
        Ius.getApiService().getQuery(Ius.readSharedPreferences(context, Ius.TOKEN), StringQuery.getUserCity(userCode))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (!response.isSuccessful()) {
                            Log.e("MainActivity - userCity", String.valueOf(response.code()));
                            return;
                        }

                        StringBuilder cities = new StringBuilder();

                        try {
                            JSONObject jsonObj = new JSONObject(response.body().string());
                            JSONArray array = jsonObj.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                int id = object.getInt("USC_LOC_CODE");
                                Log.i("getUserCity", "one of the city of user - " + id);

                                if (i==0)
                                    cities = new StringBuilder(String.valueOf(id));

                                else
                                    cities.append("-").append(id);
                            }

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                        Log.i("User's cities", String.valueOf(cities));
                        Ius.writeSharedPreferences(context, Ius.USER_CITIES, String.valueOf(cities));
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("MainActivity - userCity", "onFailure " + t.getMessage());
                    }
                });
    }


    private void openDialog() {
        Ius.createDialogAcception(context, getResources().getString(R.string.forgot_password),
                getResources().getString(R.string.forgot_password_description), false).show();
    }

    private void openActivityStart(boolean needSync) {
        Intent intent = new Intent(context, StartActivity.class);
        intent.putExtra("SYNC", needSync);
        startActivity(intent);
        finish();
    }

    private void createToast(String text) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, (ViewGroup) findViewById(R.id.toast));
        Ius.showToast(layout, context, text, false);
    }

    private void checkTodaysLogin() {
        String writtenDate = Ius.readSharedPreferences(context, Ius.DATE_LOGIN);
        if (writtenDate == null || writtenDate.equals(""))
            return;

        String currentDate = Ius.getDateByFormat(new Date(), "dd.MM.yyyy");
        if (currentDate.equals(Ius.readSharedPreferences(context, Ius.DATE_LOGIN)))
            openActivityStart(false);
    }
}