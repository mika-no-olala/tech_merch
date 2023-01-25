package kz.smrtx.techmerch.utils;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.activities.StartActivity;
import kz.smrtx.techmerch.api.StringQuery;
import kz.smrtx.techmerch.items.entities.WarehouseJournal;
import kz.smrtx.techmerch.items.viewmodels.WarehouseJournalViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CheckEquipmentSupply extends JobService {
    private Context context = this;
    private boolean jobCancelled = false;
    private WarehouseJournalViewModel warehouseJournalViewModel;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i("CheckEquipmentSupply", "onStartJob");
        warehouseJournalViewModel = new ViewModelProvider(StartActivity.getInstance()).get(WarehouseJournalViewModel.class);
        doDestiny(jobParameters);
        return true;
    }

    private void doDestiny(JobParameters jobParameters) {
        new Thread(() -> {
            Ius.getApiService().getQuery(
                    Ius.readSharedPreferences(this, Ius.TOKEN),
                    StringQuery.getNewSupplies(Ius.readSharedPreferences(this, Ius.USER_CITIES)))
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            if (!response.isSuccessful()) {
                                Log.e("CheckEquipmentSupply", "not Successful - " + response.code());
                                return;
                            }
                            warehouseJournalViewModel.deleteAll();
                            try {
                                JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                                Type type = new TypeToken<List<WarehouseJournal>>() {}.getType();
                                List<WarehouseJournal> journal = new Gson().fromJson(jsonObject.getJSONArray("data").toString(), type);
                                Log.e("CheckEquipmentSupply", "data size: " + journal.size() + " " + new Date());
                                if (journal.size() > 0) {
                                    warehouseJournalViewModel.insert(journal);
                                }
                            } catch (JSONException | IOException e) {
                                Log.e("CheckEquipmentSupply", e.getMessage());
                                e.printStackTrace();
                            }

                            jobFinished(jobParameters, false);
                        }
                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                            Log.e("CheckEquipmentSupply", "onFailure - " + t.getMessage());
                            jobFinished(jobParameters, false);
                        }
                    });
        }).start();

    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i("CheckEquipmentSupply", "onStopJob");
        jobCancelled = true;
        return true;
    }
}
