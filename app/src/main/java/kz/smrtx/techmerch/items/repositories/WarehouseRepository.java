package kz.smrtx.techmerch.items.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import kz.smrtx.techmerch.items.dao.NoteDao;
import kz.smrtx.techmerch.items.dao.WarehouseDao;
import kz.smrtx.techmerch.items.entities.Note;
import kz.smrtx.techmerch.items.entities.TechDatabase;
import kz.smrtx.techmerch.items.entities.Warehouse;

public class WarehouseRepository {
    private WarehouseDao warehouseDao;

    public WarehouseRepository(Application application) {
        TechDatabase database = TechDatabase.getInstance(application);
        warehouseDao = database.warehouseDao();
    }
    public void insertWarehouses(List<Warehouse> warehouseList) { new InsertWarehousesAsyncTask(warehouseDao).execute(warehouseList); }
    public void update(Warehouse warehouse) {
        new UpdateWarehouseAsyncTask(warehouseDao).execute(warehouse);
    }
    public void deleteAllWarehouses() {
        new DeleteAllWarehousesAsyncTask(warehouseDao).execute();
    }

    public LiveData<List<Warehouse>> getWarehouseByIdAndEquipment(String equipment, int warehouseId) {
        return warehouseDao.getWarehouseByIdAndEquipment(equipment, warehouseId);
    }
    public LiveData<List<Warehouse>> getAllWarehouseContent() {
        return warehouseDao.getAllWarehouseContent();
    }
    public LiveData<List<Warehouse>> getAllWarehouses() {
        return warehouseDao.getAllWarehouses();
    }
    public LiveData<List<Warehouse>> getAllWarehousesByCity(int[] locationCode) {
        return warehouseDao.getAllWarehousesByCity(locationCode);
    }
    public LiveData<List<Warehouse>> getAllWarehouseContentByCity(int[] locationCode) {
        return warehouseDao.getAllWarehouseContentByCity(locationCode);
    }

    private static class InsertWarehousesAsyncTask extends AsyncTask<List<Warehouse>, Void, Void> {
        private final WarehouseDao warehouseDao;

        private InsertWarehousesAsyncTask(WarehouseDao warehouseDao) { this.warehouseDao = warehouseDao; }

        @Override
        protected Void doInBackground(List<Warehouse>[] warehouseList) {
            warehouseDao.insert(warehouseList[0]);
            return null;
        }
    }

    private static class UpdateWarehouseAsyncTask extends AsyncTask<Warehouse, Void, Void> {
        private final WarehouseDao warehouseDao;

        private UpdateWarehouseAsyncTask(WarehouseDao warehouseDao) {
            this.warehouseDao = warehouseDao;
        }

        @Override
        protected Void doInBackground(Warehouse... warehouses) {
            warehouseDao.update(warehouses[0]);
            return null;
        }
    }

    private static class DeleteAllWarehousesAsyncTask extends AsyncTask<Void, Void, Void> {
        private final WarehouseDao warehouseDao;

        private DeleteAllWarehousesAsyncTask(WarehouseDao warehouseDao) {
            this.warehouseDao = warehouseDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            warehouseDao.deleteAllWarehouseContent();
            return null;
        }
    }
}
