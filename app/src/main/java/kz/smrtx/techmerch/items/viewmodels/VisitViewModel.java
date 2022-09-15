package kz.smrtx.techmerch.items.viewmodels;

import android.app.Application;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import kz.smrtx.techmerch.items.repositories.VisitRepository;
import kz.smrtx.techmerch.items.entities.Visit;

public class VisitViewModel extends AndroidViewModel {
    private VisitRepository repository;

    public VisitViewModel(@NonNull Application application) {
        super(application);
        repository = new VisitRepository(application);
    }

    public void insert(Visit visit) {
        repository.insert(visit);
    }
    public void update(Visit visit) {
        repository.update(visit);
    }
    public void delete(Visit visit) {
        repository.delete(visit);
    }
    public void deleteAllVisits() {
        repository.deleteAllVisits();
    }
    public LiveData<Visit> getVisitByNumber(String number) { return repository.getVisitByNumber(number); }
    public Cursor getCursor(SimpleSQLiteQuery query){ return repository.getSyncData(query);}
}
