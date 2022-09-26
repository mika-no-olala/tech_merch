package kz.smrtx.techmerch.items.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import kz.smrtx.techmerch.items.entities.Element;
import kz.smrtx.techmerch.items.entities.History;
import kz.smrtx.techmerch.items.repositories.ElementRepository;
import kz.smrtx.techmerch.items.repositories.HistoryRepository;

public class HistoryViewModel extends AndroidViewModel {
    private HistoryRepository historyRepository;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        historyRepository = new HistoryRepository(application);
    }

    public void insertHistory(List<History> histories) {
        historyRepository.insertHistory(histories);
    }

    public void deleteHistory() {
        historyRepository.deleteHistory();
    }

    public LiveData<List<History>> getHistoryListBySalCode(int salePointCode) {
        return historyRepository.getHistoryListBySalCode(salePointCode);
    }

    public LiveData<List<History>> getHistoryListWhichAreRelatedTo(int userCode) {
        return historyRepository.getHistoryListWhichAreRelatedTo(userCode);
    }

    public int getHistoryListWhichAreRelatedTo(int userCode, int salePointCode) {
        return historyRepository.getRequestsNumberOnSalePointByUser(userCode, salePointCode);
    }

    public int getTMRCodeByRequest(String requestCode) {
        return historyRepository.getTMRCodeByRequest(requestCode);
    }
}
