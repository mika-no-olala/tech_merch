package kz.smrtx.techmerch.items.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import kz.smrtx.techmerch.items.dao.HistoryDao;
import kz.smrtx.techmerch.items.entities.History;
import kz.smrtx.techmerch.items.entities.TechDatabase;

public class HistoryRepository {
    private HistoryDao historyDao;

    public HistoryRepository(Application application) {
        TechDatabase db = TechDatabase.getInstance(application);
        historyDao = db.historyDao();
    }
    public void insertHistory(List<History> histories) {
        new insertHistoryAsyncTask(historyDao).execute(histories);
    }
    public void deleteHistory() {
        new deleteHistoryAsyncTask(historyDao).execute();
    }
    public LiveData<List<History>> getHistoryListBySalCode(int salePointCode) {
        return historyDao.getHistoryListBySalCode(salePointCode);
    }
    public LiveData<List<History>> getHistoryListWhichAreRelatedTo(int userCode) {
        return historyDao.getHistoryListWhichAreRelatedTo(userCode);
    }
    public int getRequestsNumberOnSalePointByUser(int userCode, int salePointCode) {
        return historyDao.getRequestsNumberOnSalePointByUser(userCode, salePointCode);
    }


    private static class insertHistoryAsyncTask extends AsyncTask<List<History>, Void, Void>{
        private HistoryDao historyDao;
        public insertHistoryAsyncTask(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        @Override
        protected Void doInBackground(List<History>... histories) {
            historyDao.insertHistory(histories[0]);
            return null;
        }
    }

    private static class deleteHistoryAsyncTask extends AsyncTask<Void, Void, Void>{
        private HistoryDao historyDao;
        public deleteHistoryAsyncTask(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            historyDao.deleteAllElements();
            return null;
        }
    }
}
