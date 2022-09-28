package kz.smrtx.techmerch.items.viewmodels;

import android.app.Application;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.ArrayList;
import java.util.List;

import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.entities.Visit;
import kz.smrtx.techmerch.items.repositories.RequestRepository;
import kz.smrtx.techmerch.items.repositories.VisitRepository;

public class RequestViewModel extends AndroidViewModel {
    private RequestRepository requestRepository;

    public RequestViewModel(@NonNull Application application) {
        super(application);
        requestRepository = new RequestRepository(application);
    }

    public void insert(Request request) {
        requestRepository.insert(request);
    }
    public void insertRequests(List<Request> requests) { requestRepository.insertRequests(requests); }
    public void update(Request request) {
        requestRepository.update(request);
    }
    public void delete(Request request) {
        requestRepository.delete(request);
    }
    public void deleteAllRequests() {
        requestRepository.deleteAllRequests();
    }
    public int selectNumberFromOutlets(String code) {
        return requestRepository.selectNumberFromOutlets(code);
    }
    public LiveData<List<Request>> getRequestsByAppointed(int userCode) {
        return requestRepository.getRequestsByAppointed(userCode);
    }
    public LiveData<List<Request>> getRequestsByAppointed(int userCode, int salePointCode) {
        return requestRepository.getRequestsByAppointed(userCode, salePointCode);
    }

    public LiveData<Request> getRequestByCode(String code) {
        return requestRepository.getRequestByCode(code);
    }
}
