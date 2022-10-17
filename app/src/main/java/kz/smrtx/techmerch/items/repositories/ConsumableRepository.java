package kz.smrtx.techmerch.items.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import kz.smrtx.techmerch.items.dao.ConsumableDao;
import kz.smrtx.techmerch.items.dao.ElementDao;
import kz.smrtx.techmerch.items.entities.Consumable;
import kz.smrtx.techmerch.items.entities.Element;
import kz.smrtx.techmerch.items.entities.TechDatabase;

public class ConsumableRepository {
    private ConsumableDao consumableDao;

    public ConsumableRepository(Application application) {
        TechDatabase db = TechDatabase.getInstance(application);
        consumableDao = db.consumableDao();
    }
    public void insertReport(List<Consumable> report) {
        new insertReportAsyncTask(consumableDao).execute(report);
    }
    public void deleteReport() {
        new deleteReportAsyncTask(consumableDao).execute();
    }
    public LiveData<List<Consumable>> getReportByCode(String reportCode) {
        return consumableDao.getReportByCode(reportCode);
    }
    public LiveData<List<Consumable>> getReportByUser(int userCode) {
        return consumableDao.getReportByUser(userCode);
    }
    public LiveData<List<Consumable>> getReports() {
        return consumableDao.getReports();
    }

    private static class insertReportAsyncTask extends AsyncTask<List<Consumable>, Void, Void>{
        private ConsumableDao consumableDao;
        public insertReportAsyncTask(ConsumableDao consumableDao) {
            this.consumableDao = consumableDao;
        }

        @Override
        protected Void doInBackground(List<Consumable>... report) {
            consumableDao.insertReport(report[0]);
            return null;
        }
    }

    private static class deleteReportAsyncTask extends AsyncTask<Void, Void, Void>{
        private ConsumableDao consumableDao;
        public deleteReportAsyncTask(ConsumableDao consumableDao) {
            this.consumableDao = consumableDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            consumableDao.deleteReport();
            return null;
        }
    }
}
