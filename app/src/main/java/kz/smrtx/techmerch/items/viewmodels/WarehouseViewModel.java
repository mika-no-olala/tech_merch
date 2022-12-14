package kz.smrtx.techmerch.items.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import kz.smrtx.techmerch.items.entities.Warehouse;
import kz.smrtx.techmerch.items.repositories.WarehouseRepository;

public class WarehouseViewModel extends AndroidViewModel {
    private WarehouseRepository warehouseRepository;

    public WarehouseViewModel(@NonNull Application application) {
        super(application);
        warehouseRepository = new WarehouseRepository(application);
    }

    public void insert(List<Warehouse> warehouseList) { warehouseRepository.insertWarehouses(warehouseList); }
    public void update(Warehouse warehouse) {
        warehouseRepository.update(warehouse);
    }
    public void deleteAllWarehouses() {
        warehouseRepository.deleteAllWarehouses();
    }
    public LiveData<List<Warehouse>> getWarehouseByIdAndEquipment(String equipment, int warehouseId) { return warehouseRepository.getWarehouseByIdAndEquipment(equipment, warehouseId);}
    public LiveData<List<Warehouse>> getAllWarehouseContent() {
        return warehouseRepository.getAllWarehouseContent();
    }
    public LiveData<List<Warehouse>> getAllWarehouses() {
        return warehouseRepository.getAllWarehouses();
    }
    public LiveData<List<Warehouse>> getAllWarehousesByCity(int[] locationCode) {
        return warehouseRepository.getAllWarehousesByCity(locationCode);
    }
    public LiveData<List<Warehouse>> getAllWarehouseContentByCity(int[] locationCode) {
        return warehouseRepository.getAllWarehouseContentByCity(locationCode);
    }
}
