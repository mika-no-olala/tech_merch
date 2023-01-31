package kz.smrtx.techmerch.utils;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.activities.SessionActivity;
import kz.smrtx.techmerch.activities.SyncActivity;
import kz.smrtx.techmerch.items.entities.History;
import kz.smrtx.techmerch.items.entities.Photo;
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.reqres.synctables.SyncTables;
import kz.smrtx.techmerch.items.reqres.synctables.Table;
import kz.smrtx.techmerch.items.viewmodels.HistoryViewModel;
import kz.smrtx.techmerch.items.viewmodels.PhotoViewModel;
import kz.smrtx.techmerch.items.viewmodels.RequestViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestSync {

    private  int stepCounter = 0;
    private final RequestViewModel requestViewModel;
    private final HistoryViewModel historyViewModel;
    private final PhotoViewModel photoViewModel;
    private final List<String> imagesUrlForDownload = new ArrayList<>();
    private SwipeRefreshLayout refresher;

    public RequestSync(SwipeRefreshLayout refresher) {
        requestViewModel = new ViewModelProvider(SessionActivity.getInstance()).get(RequestViewModel.class);
        historyViewModel = new ViewModelProvider(SessionActivity.getInstance()).get(HistoryViewModel.class);
        photoViewModel = new ViewModelProvider(SessionActivity.getInstance()).get(PhotoViewModel.class);
        this.refresher = refresher;

        getSyncTables(Ius.readSharedPreferences(SessionActivity.getInstance(), Ius.USER_CODE));
    }

    private void getSyncTables(String userCode) {
        Ius.getApiService().getSyncTables(userCode).enqueue(new Callback<SyncTables>() {
            @Override
            public void onResponse(@NonNull Call<SyncTables> call, @NonNull Response<SyncTables> response) {
                if (!response.isSuccessful()) {
                    Log.e("getSyncTables", "response is not successful - " + response.code());
                    return;
                }
                for (Table table : response.body().getData())
                    Log.i("Get Tables", table.getMVLTABLENAME());

                getRequestsFromDB(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<SyncTables> call, @NonNull Throwable t) {
                Log.e("getSyncTables", "onFailure - " + t.getMessage());
            }
        });
    }

    @SuppressLint("CheckResult")
    private void getRequestsFromDB(SyncTables syncTables) {
        ArrayList<String> tableNames = new ArrayList<>();
        final List<Observable<?>> requests = new ArrayList<>();

        for (Table table : syncTables.getData()) {
            if (!table.getMVLTABLENAME().contains("REQUEST"))
                continue;
            tableNames.add(table.getMVLTABLENAME());
            Log.e("sss", table.getMVLTABLENAME());
            requests.add(Ius.getApiService().getSyncData(table.getMVLVIEWNAME(), table.getUSECODE().toString(), table.getMVLREFERENCE().toString()));
        }

        Observable.concat(requests)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    Log.i("getRequestsFromDB", "om-nom, get data " + stepCounter);
                    handleResults(new JSONObject(((ResponseBody)o).string()), tableNames.get(stepCounter));
                    stepCounter++;
                }, throwable ->{
                    Log.e("getRequestsFromDB-" + stepCounter + "-step", throwable.getLocalizedMessage());
                    Log.e("getRequestsFromDB-" + stepCounter + "-step", throwable.toString());
                }, () -> new DownloadImages().execute());
    }

    private void handleResults(JSONObject obj, String tableName) throws JSONException {
        int arraySize = obj.getJSONArray("data").length();
        Log.i("handleResults", "got " + tableName + " with size " + arraySize);
        if (obj.getString("status").trim().equalsIgnoreCase("OK") && arraySize > 0) {
            try {
                switch (tableName) {
                    case "ST_REQUEST":
                        Type requestType = new TypeToken<List<Request>>() {
                        }.getType();
                        List<Request> requests = new Gson().fromJson(obj.getJSONArray("data").toString(), requestType);
                        if (requests.size() > 0) {
                            requestViewModel.insertRequests(requests);
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
                Log.e("getSyncData switch", e.toString());
            }
        }
    }

    private void makeListForDownload(List<Photo> photoList) {
        for (Photo p : photoList) {
            if (p.getREP_PHOTO()==null)
                continue;

            if (isFileExist(p.getREP_PHOTO()))
                continue;

            imagesUrlForDownload.add(p.getREP_PHOTO());
        }

        Log.i("makeListForDownload", "need to download " + imagesUrlForDownload.size() + " photos");
    }

    private boolean isFileExist(String photoName) {
        String path = String.valueOf(SessionActivity.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        File file = new File(path + "/" + photoName);
        return file.exists();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class DownloadImages extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void result) {
            refresher.setRefreshing(false);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            downloadImages();
            return null;
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

                    if (!SyncActivity.isExternalStorageWritable()) {
                        Log.e("RequestSync", "no access to file system");
                        return;
                    }
                    String root = Objects.requireNonNull(SessionActivity.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES)).toString();

                    File file = new File(root, downloadUrl);
                    if (file.exists()) {
                        file.delete();
                    }

                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            Log.e("downloadImages-no-exist", e.getLocalizedMessage());
                        }
                    }

                    FileOutputStream fos = new FileOutputStream(file);
                    InputStream is = c.getInputStream();

                    int fileLength = c.getContentLength();

                    byte[] buffer = new byte[1024];
                    int len1;

                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);
                        if (fileLength > 0){
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
}
