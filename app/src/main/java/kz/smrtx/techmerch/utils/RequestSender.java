package kz.smrtx.techmerch.utils;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.sqlite.db.SimpleSQLiteQuery;

import org.json.JSONObject;

import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.activities.SessionActivity;
import kz.smrtx.techmerch.activities.SyncActivity;
import kz.smrtx.techmerch.items.entities.Photo;
import kz.smrtx.techmerch.items.viewmodels.PhotoViewModel;
import kz.smrtx.techmerch.items.viewmodels.RequestViewModel;
import kz.smrtx.techmerch.items.viewmodels.VisitViewModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RequestSender extends JobService {
    private Context sessionContext;
    private VisitViewModel visitViewModel;
    private RequestViewModel requestViewModel;
    private PhotoViewModel photoViewModel;
    private boolean jobCancelled = false;
    private final List<String> result = new ArrayList<>();
    private List<String> allImagesToUpload = new ArrayList<>();
    public static final String TAG = "RequestJobService";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i("RequestSender", "Job for sending request is started");
        sessionContext = SessionActivity.getInstance();
        visitViewModel = new ViewModelProvider(SessionActivity.getInstance()).get(VisitViewModel.class);
        requestViewModel = new ViewModelProvider(SessionActivity.getInstance()).get(RequestViewModel.class);
        photoViewModel = new ViewModelProvider(SessionActivity.getInstance()).get(PhotoViewModel.class);

        doDestiny(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i("RequestSender", "Job has finished");
        jobCancelled = true;
        return true;
    }

    @SuppressLint("Range")
    private void doDestiny(final JobParameters parameters) {
        new Thread(() -> {
            String[] tables = {"ST_REQUEST", "ST_REQUEST_PHOTO"};
            String[] tablesOnServer = {"sync.WT_SYNC_ST_REQUEST", "sync.WT_SYNC_ST_REQUEST_PHOTO"};

            for(int i = 0; i < tables.length; i++) {
                SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM " + tables[i], null);
                Cursor cursor = visitViewModel.getCursor(query);
                if (cursor == null) {
                    Log.e("doDestiny", "cursor is null");
                    return;
                }
                if (cursor.getCount() <= 0) {
                    Log.e("doDestiny", "cursor is 0");
                    return;
                }

                while (cursor.moveToNext()) {
                    StringBuilder columns = new StringBuilder();
                    StringBuilder values = new StringBuilder();

                    for (String name : cursor.getColumnNames()) {
                        columns.append(name);
                        columns.append(",");
                        values.append("N'").append(cursor.getString(cursor.getColumnIndex(name))).append("'");
                        values.append(",");
                    }

                    String statement = "INSERT INTO " + tablesOnServer[i] +
                            " (" +
                            columns.substring(0, columns.length() - 1) +
                            ") " +
                            "VALUES(" +
                            values.substring(0, values.length() - 1) +
                            ")";

                    if (!statement.contains(",N'no')")) {
                        statement = statement.replace("N'null'", "null");
                        result.add(statement);
                    }
                }
                cursor.close();
            }

            if(!result.isEmpty()) {
                String path = Objects.requireNonNull(
                        SessionActivity.getInstance().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS))
                        .getAbsolutePath()+"/"+Ius.DIRECTORY_FROM_SERVER+ File.separator+"request_sync"+File.separator;

                String zipName = Ius.generateUniqueCode(SessionActivity.getInstance(), "zip") + ".zip";

                Ius.createAndWriteToFile(result, path, zipName);
                uploadFile(zipName, Ius.readSharedPreferences(SessionActivity.getInstance(), Ius.USER_CODE));
            }

            Log.w("sss", "jobFinished");

            jobFinished(parameters, false);
        }).start();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class GetImages extends AsyncTask<Void, Void, Void> {
        private final PhotoViewModel photoViewModel;

        public GetImages(PhotoViewModel photoViewModel) {
            this.photoViewModel = photoViewModel;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("uploadFile", "images quantity: " + allImagesToUpload.size());
            if (!allImagesToUpload.isEmpty())
                uploadPhotoFiles();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            allImagesToUpload = photoViewModel.getPhotosForUploadNoAsync();
            return null;
        }
    }

    private void uploadFile(String zipName, String useCode) {
        File file = new File(Objects.requireNonNull(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)).getAbsolutePath()+"/"+Ius.DIRECTORY_FROM_SERVER+"/request_sync/"+zipName);
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
                        String checksum = SyncActivity.getFileChecksum(md5Digest, file);
                        if (jsonObj.getString("data").equals(checksum)){
                            requestViewModel.updateAfterPartialSync();
                            new GetImages(photoViewModel).execute();
                        }
                    }catch (Exception e){
                        Log.e("RequestSender", "uploadFile - Checksum - " + e);
                    }
                }else {
                    Log.e("RequestSender", "uploadFile - response is not successful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("RequestSender", "uploadFile - onFailure");
            }
        });
    }

    private void uploadPhotoFiles(){
        String path = String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES));

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
                    photoViewModel.updateAfterPartialSync(img);
                    Log.i("uploadPhoto", "Photo " + img + " has send - " + response.code());
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onFailure(@NonNull Call<JSONObject> call, @NonNull Throwable t) {
                    Log.e("uploadPhoto Failure", t.getMessage());
                }
            });
        }
    }
}
