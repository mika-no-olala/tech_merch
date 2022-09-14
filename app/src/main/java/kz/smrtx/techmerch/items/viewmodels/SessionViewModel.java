package kz.smrtx.techmerch.items.viewmodels;

import android.app.Application;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.sqlite.db.SimpleSQLiteQuery;

import kz.smrtx.techmerch.items.entities.Session;
import kz.smrtx.techmerch.items.entities.Visit;
import kz.smrtx.techmerch.items.repositories.SessionRepository;
import kz.smrtx.techmerch.items.repositories.VisitRepository;

public class SessionViewModel extends AndroidViewModel {
    private SessionRepository repository;

    public SessionViewModel(@NonNull Application application) {
        super(application);
        repository = new SessionRepository(application);
    }

    public void insert(Session session) {
        repository.insert(session);
    }
    public void update(Session session) {
        repository.update(session);
    }
    public void delete(Session session) {
        repository.delete(session);
    }
    public void deleteAllSessions() {
        repository.deleteAllSessions();
    }
    public Cursor getCursor(SimpleSQLiteQuery query){ return repository.getSyncData(query);}
}
