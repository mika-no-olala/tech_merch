package kz.smrtx.techmerch.items.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import kz.smrtx.techmerch.items.dao.WarehouseDao;
import kz.smrtx.techmerch.items.dao.WarehouseJournalDao;
import kz.smrtx.techmerch.items.entities.TechDatabase;
import kz.smrtx.techmerch.items.entities.Warehouse;
import kz.smrtx.techmerch.items.entities.WarehouseJournal;

public class WarehouseJournalRepository {
    private WarehouseJournalDao warehouseJournalDao;

    public WarehouseJournalRepository(Application application) {
        TechDatabase database = TechDatabase.getInstance(application);
        warehouseJournalDao = database.warehouseJournalDao();
    }
    public void insert(List<WarehouseJournal> journal) { new InsertAsyncTask(warehouseJournalDao).execute(journal); }

    public void deleteEntry(WarehouseJournal journal) { new DeleteAsyncTask(warehouseJournalDao).execute(journal); }

    public void deleteAll() {
        new DeleteAllAsyncTask(warehouseJournalDao).execute();
    }

    public LiveData<List<WarehouseJournal>> getSupplies() {
        return warehouseJournalDao.getSupplies();
    }

    public int getSuppliesQuantity() {
        return warehouseJournalDao.getSuppliesQuantity();
    }

    private static class InsertAsyncTask extends AsyncTask<List<WarehouseJournal>, Void, Void> {
        private final WarehouseJournalDao warehouseJournalDao;

        private InsertAsyncTask(WarehouseJournalDao warehouseJournalDao) { this.warehouseJournalDao = warehouseJournalDao; }

        @Override
        protected Void doInBackground(List<WarehouseJournal>[] journal) {
            warehouseJournalDao.insert(journal[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<WarehouseJournal, Void, Void> {
        private final WarehouseJournalDao warehouseJournalDao;

        private DeleteAsyncTask(WarehouseJournalDao warehouseJournalDao) { this.warehouseJournalDao = warehouseJournalDao; }

        @Override
        protected Void doInBackground(WarehouseJournal... journal) {
            warehouseJournalDao.delete(journal[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private final WarehouseJournalDao warehouseJournalDao;

        private DeleteAllAsyncTask(WarehouseJournalDao warehouseJournalDao) {
            this.warehouseJournalDao = warehouseJournalDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            warehouseJournalDao.deleteAll();
            return null;
        }
    }
}
