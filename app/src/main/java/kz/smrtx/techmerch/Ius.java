package kz.smrtx.techmerch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.Spanned;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import org.w3c.dom.Text;

import kz.smrtx.techmerch.utils.CustomTypefaceSpan;
import kz.smrtx.techmerch.utils.ZipManager;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.FileWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

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

    public static final String USER_ID = "USE_ID";
    public static final String USER_CODE = "USE_CODE";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_ROLE_CODE = "USER_ROLE_CODE";
    public static final String USER_ROLE_NAME = "USER_ROLE_NAME";

    public static final String BOTTOM_BAR_TEXT = "BOTTOM_BAR_TEXT";
    public static final String DEVICE_ID = "DEVICE_ID";
    public static final String DATE_LOGIN = "DATE_LOGIN";
    public static final String TOKEN = "TOKEN";
    public static final String LAST_SYNC = "LAST_SYNC";
    public static final String LAST_VISIT_NUMBER = "LAST_VISIT_NUMBER";
    public static final String LAST_SESSION_CODE = "LAST_SESSION_CODE";
    public static final String LAST_SALE_POINT_ADDRESS = "LAST_SALE_POINT_ADDRESS";
    public static final String DIRECTORY_FROM_SERVER = "prod";
    public static final String KEY_SYN_ID = "KEY_SYN_ID";

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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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

    public static Dialog createDialogList(Context context, CardAdapterString adapter, boolean multipleChoice) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        if (multipleChoice)
            dialog.setContentView(R.layout.dialog_window_list_checkbox);
        else
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

    public static Dialog createDialog(Context context, int layoutId, String title) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dialog.setContentView(layoutId);
        dialog.setCanceledOnTouchOutside(true);

        Button cancel = dialog.findViewById(R.id.cancel);
        TextView titleView = dialog.findViewById(R.id.title);

        if (title.length()>0)
            titleView.setText(title);

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

    public static SpannableString makeTextBold(Context context, String text) {
        SpannableString content = new SpannableString(text);
        Typeface font = ResourcesCompat.getFont(context, R.font.bandera_pro_bold);

        content.setSpan (new CustomTypefaceSpan("", font), 0, text.indexOf(":")+1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return content;
    }

    public static String getDateByFormat(Date date, String pattern) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(pattern, new Locale("ru"));
        return sdf.format(date);
    }

    public static Date getDateFromString(String text, String pattern) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        try {
            return formatter.parse(text);
        } catch (ParseException e) {
            // try NEW
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatterMillis = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
            try {
                return formatterMillis.parse(text);
            } catch (ParseException parseException) {
                // try ONE MORE
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatterOrdinary = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                try {
                    return formatterOrdinary.parse(text);
                } catch (ParseException exception) {
                    exception.printStackTrace();
                }
            }
        }
        return new Date();
    }

    public static Date plusDaysToDate(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.WEDNESDAY:
            case Calendar.FRIDAY:
            case Calendar.THURSDAY:
                days = days + 2;
                break;
            case Calendar.SUNDAY:
                days = days + 1;
        }

        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public static long getDifferenceBetweenDates(Date dateFirst, Date dateSecond, String timeValue) {
        long difference = dateSecond.getTime() - dateFirst.getTime();
        switch (timeValue) {
            case "s":
            default:
                return difference / 1000 % 60;
            case "m":
                return difference / (60 * 1000) % 60;
            case "h":
                return difference / (60 * 60 * 1000);
        }
    }

    public static String generateUniqueCode(Context context, String type) {
        Random random = new Random();
        int low = 100;
        int high = 999;
        int result = random.nextInt(high-low) + low;
        String currentDate = getDateByFormat(new Date(), "ddMMyyyyHHmmss");
        return type + result + "u" + readSharedPreferences(context, USER_CODE) + "t" + currentDate;
    }

    public static void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        }, 1);
    }

    public static void checkPermissions(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermission(activity);
        }
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

    public static void createAndWriteToFile(List<String> data, String path, String zipName){
        try {
            File root = new File(path);
            root.mkdirs();
            if (root.listFiles() != null){
                for(File f: Objects.requireNonNull(root.listFiles())){
                    if (f.exists()) f.delete();
                }
            }
            File file = new File(root, "sync.txt");
            if (file.exists()){ file.delete();}

            FileWriter writer = new FileWriter(file);
            for (String line: data){
                writer.append(line).append(";").append("\r\n");
            }
            writer.flush();
            writer.close();
            String[] files = {file.getAbsolutePath()};
            ZipManager.zip(files, path+File.separator+(zipName));
        }catch (Exception e){
            Log.e("WriteToFile", e.toString());
        }
    }

}
