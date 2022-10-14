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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
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
import kz.smrtx.techmerch.items.entities.History;
import kz.smrtx.techmerch.items.entities.Note;
import kz.smrtx.techmerch.items.entities.Photo;
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.entities.SalePoint;
import kz.smrtx.techmerch.items.entities.User;
import kz.smrtx.techmerch.items.reqres.synctables.SyncTables;
import kz.smrtx.techmerch.items.reqres.synctables.Table;
import kz.smrtx.techmerch.items.viewmodels.ConsumableViewModel;
import kz.smrtx.techmerch.items.viewmodels.ElementViewModel;
import kz.smrtx.techmerch.items.viewmodels.HistoryViewModel;
import kz.smrtx.techmerch.items.viewmodels.NoteViewModel;
import kz.smrtx.techmerch.items.viewmodels.PhotoViewModel;
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

    private int processImagesToUpload = 1;
    private int stepCounter = 0;
    private final Context context = this;
    private TextView syncInfo;
    private TextView lastSync;
    private Button startWork;
    private Button repeat;
    private ImageView animationStopped;
    private GifImageView animation;

    private final ArrayList<String> imagesUrlForDownload = new ArrayList<>();
    private final ArrayList<String> allImagesUrl = new ArrayList<>();
    private final List<String> allImagesToUpload = new ArrayList<>();

    private RequestViewModel requestViewModel;
    private SessionViewModel sessionViewModel;
    private VisitViewModel visitViewModel;
    private SalePointViewModel salePointViewModel;
    private UserViewModel userViewModel;
    private ElementViewModel elementViewModel;
    private HistoryViewModel historyViewModel;
    private NoteViewModel noteViewModel;
    private PhotoViewModel photoViewModel;
    private ConsumableViewModel consumableViewModel;

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
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        photoViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);
        consumableViewModel = new ViewModelProvider(this).get(ConsumableViewModel.class);

        startSync();

        startWork.setOnClickListener(view -> openActivitySession());

        repeat.setOnClickListener(view -> startSync());

        back.setOnClickListener(view -> finish());
    }

    private void getAllPhotos() {
        photoViewModel.getPhotosForUpload().observe(this, photos -> {
            for (Photo p : photos) {
                allImagesToUpload.add(p.getREP_PHOTO());
            }
        });
    }
    
    private void startSync() {
        startWork.setEnabled(false);
        repeat.setEnabled(false);
        animationStopped.setVisibility(View.GONE);
        animation.setVisibility(View.VISIBLE);

        getAllPhotos();

        Ius.refreshToken(this);

        syncCode = Ius.generateUniqueCode(this, "syn");

        new GatherDataFromLocal(this, visitViewModel).execute();
    }

    @SuppressLint("StaticFieldLeak")
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

                if (allImagesToUpload.size()>0)
                    uploadPhotoFiles();

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

        @SuppressWarnings("deprecation")
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
            String[] tables = {"ST_SESSION", "ST_VISIT", "ST_REQUEST", "ST_NOTES", "ST_REQUEST_PHOTO", "ST_TECHNIC_REPORT"};
            String[] tablesOnServer = {"sync.WT_SYNC_A_ST_SESSION", "sync.WT_SYNC_A_ST_VISIT", "sync.WT_SYNC_ST_REQUEST", "sync.WT_SYNC_ST_NOTES", "sync.WT_SYNC_ST_REQUEST_PHOTO", "sync.WT_SYNC_ST_TECHNIC_REPORT"};

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
            public void onResponse(@NonNull Call<SyncTables> call, @NonNull Response<SyncTables> response) {
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
            public void onFailure(@NonNull Call<SyncTables> call, @NonNull Throwable t) {
                createToast(t.getMessage(), false);
                Log.e("getSyncTables", "onFailure - " + t.getMessage());
                unlockButtons();
            }
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
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
                Log.i("getSyncData", "om-nom, get data " + stepCounter);
                handleResults(new JSONObject(((ResponseBody)o).string()), tableNames.get(stepCounter));
                stepCounter++;
                syncInfo.setText("Получение данных " + (stepCounter*100/requests.size()) + "/ 100%");
            }, throwable ->{
                Log.e("getSyncData", throwable.getLocalizedMessage());
                if(Objects.equals(throwable.getLocalizedMessage(), "timeout"))
                    lastSync.setText(getResources().getString(R.string.server_timeout));
                else
                    lastSync.setText(getResources().getString(R.string.sync_server_connection));
                unlockButtons();
            }, () -> new DownloadImages().execute());
    }

    private void handleResults(JSONObject obj, String tableName) throws JSONException {
        if (obj.getString("status").trim().equalsIgnoreCase("OK") && obj.getJSONArray("data").length() > 0) {
            try {
                Log.i("tableName", tableName);
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
                    case "ST_REQUEST_HISTORY":
                        Type historyType = new TypeToken<List<History>>() {
                        }.getType();
                        List<History> histories = new Gson().fromJson(obj.getJSONArray("data").toString(), historyType);
                        if (histories.size() > 0) {
                            historyViewModel.insertHistory(histories);
                        }
                        break;
                    case "ST_NOTES":
                        Type noteType = new TypeToken<List<Note>>() {
                        }.getType();
                        List<Note> notes = new Gson().fromJson(obj.getJSONArray("data").toString(), noteType);
                        if (notes.size() > 0) {
                            noteViewModel.insertNotes(notes);
                        }
                        break;
                    case "ST_REQUEST_PHOTO":
                        Type photoType = new TypeToken<List<Photo>>() {
                        }.getType();
                        List<Photo> photos = new Gson().fromJson(obj.getJSONArray("data").toString(), photoType);
                        if (photos.size() > 0) {
                            photoViewModel.insertPhotos(photos);
                            makeListForDownload(photos);
                        }
                        break;
                }
            }catch (Exception e){
                Log.e("getSyncData", e.toString());
            }
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class DownloadImages extends AsyncTask<Void, Void, Void> {
        
        int progressPercentage = 0;
        int progress = 0;

        @SuppressLint("SetTextI18n")
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            syncInfo.setText("Скачивание фото: " + progress + "/" + imagesUrlForDownload.size());
        }

        @Override
        protected void onPostExecute(Void result) {
            syncInfo.setText("Очистка мусора...");
            dataActualization();
            endSync(true);
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            downloadImages();
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            syncInfo.setText(getResources().getString(R.string.downloading_images_data_error));
            Log.e("DownloadImages", "onCancelled");
            unlockButtons();
        }
        
        @SuppressWarnings("ResultOfMethodCallIgnored")
        private void downloadImages() {
            try {
                for (String downloadUrl: imagesUrlForDownload) {
                    URL url = new URL(Ius.PHOTO_URL+downloadUrl);
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("GET");
                    c.connect();
                    
                    if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.e("downloadImages", "Server returned HTTP " + c.getResponseCode()
                                + " " + c.getResponseMessage() + " - " + downloadUrl);
                        continue;
                    }

                    if (!isExternalStorageWritable()) {
                        syncInfo.setText(getResources().getString(R.string.no_write_permission_error));
                        unlockButtons();
                        return;
                    }
                    String root = Objects.requireNonNull(getExternalFilesDir(Environment.DIRECTORY_PICTURES)).toString();

                    File file = new File(root, downloadUrl);
                    if (file.exists()) {
                        file.delete();
                    }

                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            syncInfo.setText(getResources().getString(R.string.not_all_photos_error));
                            Log.e("downloadImages-no-exist", e.getLocalizedMessage());
                            unlockButtons();
                        }
                    }

                    FileOutputStream fos = new FileOutputStream(file);
                    InputStream is = c.getInputStream();

                    int fileLength = c.getContentLength();

                    byte[] buffer = new byte[1024];
                    int len1;
                    long total = 0;
                    progressPercentage = 0;
                    progress++;

                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);
                        total += len1;
                        if (fileLength > 0){
                            progressPercentage = (int)(total*100 / fileLength);
                            publishProgress();
                        }
                    }

                    fos.close();
                    is.close();
                }
            } catch (Exception e) {

                Log.e("downloadImages-catch", e.getLocalizedMessage());
            }
        }
    }

    private void makeListForDownload(List<Photo> photoList) {
        for (Photo p : photoList) {
            if (p.getREP_PHOTO()==null)
                continue;

            allImagesUrl.add(p.getREP_PHOTO());

            if (isFileExist(p.getREP_PHOTO()))
                continue;
            
            imagesUrlForDownload.add(p.getREP_PHOTO());
        }

        Log.i("makeListForDownload", "need to download " + imagesUrlForDownload.size() + " photos");
    }

    private boolean isFileExist(String photoName) {
        String path = String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        File file = new File(path + "/" + photoName);
        return file.exists();
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private void endSync(boolean success) {
        unlockButtons();
        startWork.setEnabled(true);
        String date = Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss");
        Ius.writeSharedPreferences(context, Ius.LAST_SYNC, date);
        syncInfo.setText(getResources().getString(R.string.finished));
        if (success)
            lastSync.setText(getResources().getString(R.string.sync_success));
        else
            lastSync.setText(getResources().getString(R.string.sync_ended_errors));
    }

    private void dataActualization(){
        String path = Objects.requireNonNull(getExternalFilesDir(Environment.DIRECTORY_PICTURES)).toString();
        File myDir = new File(path);
        try {
            File[] files = myDir.listFiles();
            if(files != null) {
                for (File file : files) {
                    boolean existInSyncFile = false;
                    for (String str : allImagesUrl) {
                        if (file.getName().equals(str)) {
                            existInSyncFile = true;
                            break;
                        }
                    }
                    if (!existInSyncFile) {
                        if(file.delete())
                            Log.i("dataActualization", "delete file " + file.getName());
                        else
                            Log.w("dataActualization", "can't delete " + file.getName());
                    }
                }
            }
        }catch (NullPointerException e){
            Log.e("dataActualization", e.getLocalizedMessage());
            syncInfo.setText(getResources().getString(R.string.sync_ended_errors));
            endSync(false);
        }
    }

    @SuppressWarnings("deprecation")
    private void uploadFile(String zipName, String useCode) {
        File file = new File(Objects.requireNonNull(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)).getAbsolutePath()+"/"+Ius.DIRECTORY_FROM_SERVER+"/sync/"+zipName);
        RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("db", zipName, fbody);
        RequestBody useCodeBody = RequestBody.create(MediaType.parse("multipart/form-data"), useCode);

        Ius.getApiService().uploadSyncFile(useCodeBody, body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObj = new JSONObject(response.body().string());
                        Log.i("uploadSyncFile", "There is data that needs to be uploaded");
                        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
                        //Get the checksum
                        String checksum = getFileChecksum(md5Digest, file);
                        if (jsonObj.getString("data").equals(checksum)){
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
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                lastSync.setText(getResources().getString(R.string.sync_server_error));
                Log.e("uploadFile", "onFailure");
                unlockButtons();
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void uploadPhotoFiles(){
        String path = String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        int totalImages = allImagesToUpload.size();

        for (String img : allImagesToUpload) {
            File image = new File(path + "/" + img);
            if (!image.exists()) {
                Log.e("uploadPhotoFiles", "image does not exist");
                continue;
            }

            RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), image);
            MultipartBody.Part body = MultipartBody.Part.createFormData("photo", img, fbody);

            RequestBody fileName = RequestBody.create(MediaType.parse("multipart/form-data"), img);
            RequestBody folderName = RequestBody.create(MediaType.parse("multipart/form-data"), "photo");
            Ius.getApiService().uploadFile(fileName, folderName, body).enqueue(new Callback<JSONObject>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<JSONObject> call, @NonNull Response<JSONObject> response) {
                    if (!response.isSuccessful()) {
                        Log.e("uploadPhoto Response", String.valueOf(response.code()));
                        return;
                    }
                    syncInfo.setText("Отправка фото: " + processImagesToUpload + "/" + totalImages);
                    Log.e("sss", "Фото " + img + " отправлено - " + response.code());
                    processImagesToUpload++;
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onFailure(@NonNull Call<JSONObject> call, @NonNull Throwable t) {
                    Log.e("uploadPhoto Failure", t.getMessage());
                }
            });
        }
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
        imagesUrlForDownload.clear();
        allImagesUrl.clear();
        allImagesToUpload.clear();

        visitViewModel.deleteAllVisits();
        sessionViewModel.deleteAllSessions();
        requestViewModel.deleteAllRequests();

        userViewModel.deleteUsers();
        salePointViewModel.deleteSalePoints();
        elementViewModel.deleteElements();
        historyViewModel.deleteHistory();
        noteViewModel.deleteAllNotes();
        photoViewModel.deleteAllPhotos();
        consumableViewModel.deleteReport();
    }

    private void unlockButtons() {
        animation.setVisibility(View.GONE);
        animationStopped.setVisibility(View.VISIBLE);
        repeat.setEnabled(true);
    }

    private void createToast(String text, boolean success) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, findViewById(R.id.toast));
        Ius.showToast(layout, context, text, success);
    }

    private void openActivitySession() {
        Intent intent = new Intent(this, SessionActivity.class);
        startActivity(intent);
        finish();
    }
}