package kz.smrtx.techmerch.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
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
import kz.smrtx.techmerch.items.viewmodels.SessionViewModel;
import kz.smrtx.techmerch.items.viewmodels.UserViewModel;
import kz.smrtx.techmerch.items.viewmodels.VisitViewModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncActivity extends AppCompatActivity {

    private String syncCode;

    private int stepCounter = 0;
    private String date;
    private final Context context = this;
    private TextView syncInfo;
    private TextView lastSync;
    private Button startWork;
    private Button repeat;
    private ImageView animationStopped;
    private GifImageView animation;

    private RequestViewModel requestViewModel;
    private SessionViewModel sessionViewModel;
    private VisitViewModel visitViewModel;
    private SalePointViewModel salePointViewModel;
    private UserViewModel userViewModel;
    private ElementViewModel elementViewModel;

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
        TextView bottomBarText = findViewById(R.id.bottomBarText);
        pageName.setText(getResources().getText(R.string.sync));
        bottomBarText.setText(Ius.readSharedPreferences(this, Ius.BOTTOM_BAR_TEXT));
        
        String lastSyncString = Ius.readSharedPreferences(context, Ius.LAST_SYNC);
        if (lastSyncString==null)
            lastSync.setText(getResources().getString(R.string.last_sync) + ": \n" + getResources().getString(R.string.no_data));
        else
            lastSync.setText(getResources().getString(R.string.last_sync) + ": \n" + lastSyncString);

        visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);
        requestViewModel = new ViewModelProvider(this).get(RequestViewModel.class);
        sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);

        salePointViewModel = new ViewModelProvider(this).get(SalePointViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        elementViewModel = new ViewModelProvider(this).get(ElementViewModel.class);

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

        syncCode = Ius.generateUniqueCode(this, "syn");

        new GatherDataFromLocal(this, visitViewModel).execute();
    }

    private class GatherDataFromLocal extends AsyncTask<Void, Void, Void> {
        VisitViewModel visitViewModel;
        List<String> result = new ArrayList<>();
        Activity context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            syncInfo.setText("Отправка данных");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (result != null && result.size() > 0){
                String path = Objects.requireNonNull(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)).getAbsolutePath()+"/"+Ius.DIRECTORY_FROM_SERVER+File.separator+"sync"+File.separator;
                String zipName = syncCode + ".zip";

                Ius.createAndWriteToFile(result, path, zipName);
                uploadFile(zipName,  Ius.readSharedPreferences(context, Ius.USER_CODE));

            }else {
                clearTablesAndVariables();
                getSyncTables(Ius.readSharedPreferences(context, Ius.USER_CODE));
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            syncInfo.setText(getResources().getString(R.string.uploading_data_error));
            Log.e("GatherDataFromLocal", "onCancelled");
            unlockButtons();
        }

        public GatherDataFromLocal(Activity context, VisitViewModel visitViewModel) {
            this.context = context;
            this.visitViewModel = visitViewModel;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            updateSessionWithSyncCode();
            getAll();
            return null;
        }

        @SuppressLint("Range")
        public void getAll(){
            String[] tables = {"ST_SESSION", "ST_VISIT", "ST_REQUEST"};
            String[] tablesOnServer = {"sync.WT_SYNC_A_ST_SESSION", "sync.WT_SYNC_A_ST_VISIT", "sync.WT_SYNC_ST_REQUEST"};

            for(int i = 0; i < tables.length; i++){
                SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM "+tables[i], null);
                Cursor cursor = visitViewModel.getCursor(query);
                if (cursor != null){
                    if (cursor.getCount() > 0){
                        while (cursor.moveToNext()){
                            StringBuilder columns = new StringBuilder();
                            StringBuilder values = new StringBuilder();

                            for(String name: cursor.getColumnNames()){
                                columns.append(name);
                                columns.append(",");
                                values.append("N'").append(cursor.getString(cursor.getColumnIndex(name))).append("'");
                                values.append(",");
                            }

                            String statement1 = "INSERT INTO "+
                                    tablesOnServer[i] +
                                    " (" +
                                    columns.substring(0, columns.length()-1)+
                                    ") " +
                                    "VALUES(" +
                                    values.substring(0, values.length()-1)+
                                    ")";

                            if (!statement1.contains(",N'no'")) {
                                statement1 = statement1.replace("N'null'", "null");
                                result.add(statement1);
                            }
                        }
                    }
                    cursor.close();
                }
            }
        }
    }

    private void getSyncTables(String userCode) {
        syncInfo.setText("Получение данных");

        Ius.getApiService().getSyncTables(userCode).enqueue(new Callback<SyncTables>() {
            @Override
            public void onResponse(Call<SyncTables> call, Response<SyncTables> response) {
                if (!response.isSuccessful()) {
                    createToast(String.valueOf(response.code()), false);
                    Log.e("getSyncTables", "response is not successful - " + response.code());
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
                Log.e("getSyncTables", "onFailure - " + t.getMessage());
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

    private void uploadFile(String zipName, String useCode) {
        File file = new File(Objects.requireNonNull(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)).getAbsolutePath()+"/"+Ius.DIRECTORY_FROM_SERVER+"/sync/"+zipName);
        RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("db", zipName, fbody);
        RequestBody useCodeBody = RequestBody.create(MediaType.parse("multipart/form-data"), useCode);

        Ius.getApiService().uploadSyncFile(useCodeBody, body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObj = new JSONObject(response.body().string());
                        Log.e("sss", response.body().string());
                        Log.e("sssJ", jsonObj.getString("data"));
                        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
                        //Get the checksum
                        String checksum = getFileChecksum(md5Digest, file);
                        if (jsonObj.getString("data").equals(checksum)){
                            Log.e("sss", checksum);
                            clearTablesAndVariables();
                        }
                    }catch (Exception e){
                        Log.e("uploadFile", "Checksum - " + e);
                    }

                    getSyncTables(Ius.readSharedPreferences(context, Ius.USER_CODE));
                }else {
                    lastSync.setText(getResources().getString(R.string.sync_server_connection));
                    Log.e("uploadFile", "response is not successful");
                    unlockButtons();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                lastSync.setText(getResources().getString(R.string.sync_server_error));
                Log.e("uploadFile", "onFailure");
                unlockButtons();
            }
        });
    }

    public static String getFileChecksum(MessageDigest digest, File file) throws IOException {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString().toUpperCase();
    }

    public void updateSessionWithSyncCode() {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("UPDATE ST_SESSION SET SES_SYNC_ID='" + syncCode + "'", null);
        Cursor cursor = sessionViewModel.getCursor(query);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Log.i("SessionUpdating", String.valueOf(cursor.getCount()));
                }
            }
            cursor.close();
        }
    }

    private void clearTablesAndVariables(){
//        imageUrlListForDownload.clear();
//        fileUrlListForDownload.clear();
//        allImageUrlList.clear();
//        folders.clear();

        visitViewModel.deleteAllVisits();
        sessionViewModel.deleteAllSessions();
        requestViewModel.deleteAllRequests();

        userViewModel.deleteUsers();
        salePointViewModel.deleteSalePoints();
        elementViewModel.deleteElements();
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