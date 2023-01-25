package kz.smrtx.techmerch.items.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import kz.smrtx.techmerch.items.entities.WarehouseJournal;
import kz.smrtx.techmerch.items.repositories.WarehouseJournalRepository;

public class WarehouseJournalViewModel extends AndroidViewModel {
    private WarehouseJournalRepository warehouseJournalRepository;

    public WarehouseJournalViewModel(@NonNull Application application) {
        super(application);
        warehouseJournalRepository = new WarehouseJournalRepository(application);
    }

    public void insert(List<WarehouseJournal> journal) { warehouseJournalRepository.insert(journal); }
    public void deleteAll() {
        warehouseJournalRepository.deleteAll();
    }
    public void deleteEntry(WarehouseJournal journal) { warehouseJournalRepository.deleteEntry(journal); }
    public LiveData<List<WarehouseJournal>> getSupplies() {
        return warehouseJournalRepository.getSupplies();
    }
    public int getSuppliesQuantity() {
        return warehouseJournalRepository.getSuppliesQuantity();
    }

}
