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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kz.smrtx.techmerch.adapters.CardAdapterString;

public class Ius extends Application {
    private static Ius singleton;
    public static final String USER_LOGIN = "USER_LOGIN";

    public static Ius getSingleton() {
        return singleton;
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

    public static SpannableString makeTextUnderlined(String text) {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        return content;
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
}
