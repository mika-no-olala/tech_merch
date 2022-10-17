package kz.smrtx.techmerch.items.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import kz.smrtx.techmerch.items.entities.Consumable;
import kz.smrtx.techmerch.items.entities.Element;
import kz.smrtx.techmerch.items.repositories.ConsumableRepository;
import kz.smrtx.techmerch.items.repositories.ElementRepository;

public class ConsumableViewModel extends AndroidViewModel {
    private ConsumableRepository consumableRepository;

    public ConsumableViewModel(@NonNull Application application) {
        super(application);
        consumableRepository = new ConsumableRepository(application);
    }
    public void insertReport(List<Consumable> report) {
        consumableRepository.insertReport(report);
    }
    public void deleteReport() {
        consumableRepository.deleteReport();
    }
    public LiveData<List<Consumable>> getReportByCode(String reportCode) {
        return consumableRepository.getReportByCode(reportCode);
    }
    public LiveData<List<Consumable>> getReportByUser(int userCode) {
        return consumableRepository.getReportByUser(userCode);
    }
    public LiveData<List<Consumable>> getReports() {
        return consumableRepository.getReports();
    }
}
