package kz.smrtx.techmerch.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.items.entities.Element;
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.entities.SalePoint;
import kz.smrtx.techmerch.items.entities.SalePointItem;
import kz.smrtx.techmerch.items.entities.User;
import kz.smrtx.techmerch.items.reqres.synctables.SyncTables;
import kz.smrtx.techmerch.items.reqres.synctables.Table;
import kz.smrtx.techmerch.items.viewmodels.ChoosePointsViewModel;
import kz.smrtx.techmerch.items.viewmodels.ElementViewModel;
import kz.smrtx.techmerch.items.viewmodels.RequestViewModel;
import kz.smrtx.techmerch.items.viewmodels.SalePointViewModel;
import kz.smrtx.techmerch.items.viewmodels.UserViewModel;
import kz.smrtx.techmerch.items.viewmodels.VisitViewModel;
import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncActivity extends AppCompatActivity {

    private int stepCounter = 0;
    private String date;
    private final Context context = this;
    private TextView syncInfo;
    private TextView lastSync;
    private Button startWork;
    private Button repeat;
    private ImageView animationStopped;
    private GifImageView animation;

    private VisitViewModel visitViewModel;
    private SalePointViewModel salePointViewModel;
    private ChoosePointsViewModel choosePointsViewModel;
    private UserViewModel userViewModel;
    private ElementViewModel elementViewModel;
    private RequestViewModel requestViewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        syncInfo = findViewById(R.id.textSyncInfo);
        lastSync = findViewById(R.id.lastSync);
        startWork = findViewById(R.id.start);
        repeat = findViewById(R.id.repeat);
        animationStopped = findViewById(R.id.animationStopped);
        animation = findViewById(R.id.animation);
        View back = findViewById(R.id.back);
        TextView pageName = findViewById(R.id.pageName);
        pageName.setText(getResources().getText(R.string.sync));
        
        String lastSyncString = Ius.readSharedPreferences(context, Ius.LAST_SYNC);
        if (lastSyncString==null)
            lastSync.setText(getResources().getString(R.string.last_sync) + ": " + getResources().getString(R.string.no_data));
        else
            lastSync.setText(getResources().getString(R.string.last_sync) + ": " + lastSyncString);

        visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);
        salePointViewModel = new ViewModelProvider(this).get(SalePointViewModel.class);
        choosePointsViewModel = new ViewModelProvider(this).get(ChoosePointsViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        elementViewModel = new ViewModelProvider(this).get(ElementViewModel.class);
        requestViewModel = new ViewModelProvider(this).get(RequestViewModel.class);

        startSync();

        startWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivitySession();
            }
        });

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSync();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    
    private void startSync() {
        startWork.setEnabled(false);
        repeat.setEnabled(false);
        animationStopped.setVisibility(View.GONE);
        animation.setVisibility(View.VISIBLE);
        getSyncTables(Ius.readSharedPreferences(context, Ius.USER_CODE));
    }

    private void getSyncTables(String userCode) {
        syncInfo.setText("Получение данных");

        Ius.getApiService().getSyncTables(userCode).enqueue(new Callback<SyncTables>() {
            @Override
            public void onResponse(Call<SyncTables> call, Response<SyncTables> response) {
                if (!response.isSuccessful()) {
                    createToast(String.valueOf(response.code()), false);
                    unlockButtons();
                    return;
                }
                for (Table table : response.body().getData())
                    Log.i("Get Tables", table.getMVLTABLENAME());
                getSyncData(response.body());
            }

            @Override
            public void onFailure(Call<SyncTables> call, Throwable t) {
                createToast(t.getMessage(), false);
                unlockButtons();
            }
        });
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    private void getSyncData(final SyncTables syncTables) {
        stepCounter = 0;
        ArrayList<String> tableNames = new ArrayList<>();
        final List<Observable<?>> requests = new ArrayList<>();

        for (Table table : syncTables.getData()) {
            tableNames.add(table.getMVLTABLENAME());
            requests.add(Ius.getApiService().getSyncData(table.getMVLVIEWNAME(), table.getUSECODE().toString(), table.getMVLREFERENCE().toString()));
        }

        Observable.concat(requests)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(o -> {
                Log.e("sss", "omg");
                handleResults(new JSONObject(((ResponseBody)o).string()), tableNames.get(stepCounter));
                stepCounter++;
                syncInfo.setText("Получение данных " + (stepCounter*100/requests.size()) + "/ 100%");
            }, throwable ->{
                if(Objects.equals(throwable.getLocalizedMessage(), "timeout"))
                    lastSync.setText(getResources().getString(R.string.server_timeout));
                else
                    lastSync.setText(getResources().getString(R.string.sync_server_connection));
                unlockButtons();
            }, this::endSync);
    }

    private void handleResults(JSONObject obj, String tableName) throws JSONException {
        if (obj.getString("status").trim().toUpperCase().equals("OK") && obj.getJSONArray("data").length() > 0) {
            try {
                switch (tableName) {
                    case "ST_USER":
                        Type typeUser = new TypeToken<List<User>>() {
                        }.getType();
                        List<User> users = new Gson().fromJson(obj.getJSONArray("data").toString(), typeUser);
                        if (users.size() > 0) {
                            userViewModel.insertUsers(users);
                        }
                        break;
                    case "ST_REQ_LIST_ELEMENTS":
                        Type typeElement = new TypeToken<List<Element>>() {
                        }.getType();
                        List<Element> elements = new Gson().fromJson(obj.getJSONArray("data").toString(), typeElement);
                        if (elements.size() > 0) {
                            elementViewModel.insertElements(elements);
                        }
                        break;
                    case "ST_REQUEST":
                        Type requestType = new TypeToken<List<Request>>() {
                        }.getType();
                        List<Request> requests = new Gson().fromJson(obj.getJSONArray("data").toString(), requestType);
                        if (requests.size() > 0) {
                            requestViewModel.insertRequests(requests);
                        }
                        break;
                    case "ST_SALEPOINT":
                        Type salepointType = new TypeToken<List<SalePoint>>() {
                        }.getType();
                        List<SalePoint> salePoints = new Gson().fromJson(obj.getJSONArray("data").toString(), salepointType);
                        if (salePoints.size() > 0) {
                            salePointViewModel.insertSalePoints(salePoints);
                        }
                        break;
                }
            }catch (Exception e){
                Log.e("getSyncData", e.toString());
            }
        }
    }

    private void endSync() {
        unlockButtons();
        startWork.setEnabled(true);
        date = Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss");
        Ius.writeSharedPreferences(context, Ius.LAST_SYNC, date);
        lastSync.setText(getResources().getString(R.string.sync_success));
    }

    private void unlockButtons() {
        animation.setVisibility(View.GONE);
        animationStopped.setVisibility(View.VISIBLE);
        repeat.setEnabled(true);
    }

    private void createToast(String text, boolean success) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, (ViewGroup) findViewById(R.id.toast));
        Ius.showToast(layout, context, text, success);
    }

    private void openActivitySession() {
        Intent intent = new Intent(this, SessionActivity.class);
        startActivity(intent);
        finish();
    }
}