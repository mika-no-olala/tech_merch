package kz.smrtx.techmerch;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.SSLContext;

import kz.smrtx.techmerch.adapters.CardAdapterString;
import kz.smrtx.techmerch.api.ApiService;
import kz.smrtx.techmerch.api.BasicAuthInterceptor;
import kz.smrtx.techmerch.items.reqres.LoginResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Ius extends Application {
    private static Ius singleton;
    private static ApiService apiService;

    public static final String LOGIN = "SUPERADMIN";
    public static final String PASSWORD = "c1b4f8de804cb1ac668a0e56b5b67b0a8b7c96d3fb0c7828691b941b0e553583";
    public static final String USER_LOGIN = "USER_LOGIN";
    public static final String USER_ID = "USE_ID";
    public static final String USER_CODE = "USE_CODE";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_ROLE_CODE = "USER_ROLE_CODE";
    public static final String USER_ROLE_NAME = "USER_ROLE_NAME";

    public static final String DEVICE_ID = "DEVICE_ID";
    public static final String DATE_LOGIN = "DATE_LOGIN";
    public static final String TOKEN = "TOKEN";

    public static Ius getSingleton() {
        return singleton;
    }

    public static ApiService getApiService() {
        return apiService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            // Google Play will install latest OpenSSL
            ProviderInstaller.installIfNeeded(getApplicationContext());
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException
                | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        setRetrofit();
    }

    public BasicAuthInterceptor basicAuthInterceptor(){
        return new BasicAuthInterceptor("iViewUser", "hVta7B");
    }


    private void setRetrofit() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(basicAuthInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://videobank.t2m.kz/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    public static void writeSharedPreferences(Context context, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String readSharedPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, "noData");
    }

    public static void showToast(View layout, Context context, String text, boolean success) {
        if (!success) {
            if (Build.VERSION.SDK_INT >= 21) {
                CardView toastCard = layout.findViewById(R.id.toastCard);
                toastCard.setCardBackgroundColor(context.getResources().getColor(R.color.pink_antique_transparent));
            }
            else {
                layout.setBackgroundColor(context.getResources().getColor(R.color.pink_antique_transparent));
            }
        }
        TextView toastText = layout.findViewById(R.id.toastText);

        toastText.setText(text);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 80);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static Dialog createDialogAcception(Context context, String title, String text, boolean twoVariants) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dialog.setContentView(R.layout.dialog_window_acception);
        dialog.setCanceledOnTouchOutside(true);

        TextView titleDialog = dialog.findViewById(R.id.title);
        TextView textDialog = dialog.findViewById(R.id.text);
        Button oneButton = dialog.findViewById(R.id.ok);
        LinearLayout twoButtons = dialog.findViewById(R.id.twoVariants);

        titleDialog.setText(title);
        textDialog.setText(text);

        if (twoVariants) {
            oneButton.setVisibility(View.INVISIBLE);
            twoButtons.setVisibility(View.VISIBLE);
        }

        oneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        return dialog;
    }

    public static Dialog createDialogList(Context context, CardAdapterString adapter) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dialog.setContentView(R.layout.dialog_window_list);
        dialog.setCanceledOnTouchOutside(true);

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(dialog.getContext());

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return dialog;
    }

    public static Dialog createDialog(Context context, int layoutId) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dialog.setContentView(layoutId);
        dialog.setCanceledOnTouchOutside(true);

        Button cancel = dialog.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        return dialog;
    }

    public static SpannableString makeTextUnderlined(String text) {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        return content;
    }

    @SuppressLint("SetTextI18n")
    public static void setBottomText(TextView text, Context context) {
        text.setText(
                readSharedPreferences(context, USER_ID) + " | " +
                BuildConfig.VERSION_NAME  + " | " +
                readSharedPreferences(context, DEVICE_ID) + " | " +
                readSharedPreferences(context, DATE_LOGIN)
        );
    }

    public static String getDateByFormat(Date date, String pattern) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(pattern, new Locale("ru"));
        return sdf.format(date);
    }

    public static Date plusDaysToDate(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public static void refreshToken(Context context) {
        getApiService().getToken(LOGIN, PASSWORD).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (!response.isSuccessful()) {
                    Log.e("getToken", String.valueOf(response.code()));
                    createDialogAcception(context, context.getResources().getString(R.string.no_server_connection),
                            context.getResources().getString(R.string.no_server_connection_description), false);
                    return;
                }
                writeSharedPreferences(context, TOKEN, response.body().getToken());
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("onFailure - getToken", t.toString());
                createDialogAcception(context, context.getResources().getString(R.string.no_server_connection),
                        context.getResources().getString(R.string.no_server_connection_description), false);
            }
        });
    }

}
