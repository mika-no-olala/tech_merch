package kz.smrtx.techmerch.items.repositories;

import android.app.Application;
import android.database.Cursor;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import kz.smrtx.techmerch.items.dao.VisitDao;
import kz.smrtx.techmerch.items.entities.TechDatabase;
import kz.smrtx.techmerch.items.entities.Visit;

public class VisitRepository {
    private VisitDao visitDao;

    public VisitRepository(Application application) {
        TechDatabase database = TechDatabase.getInstance(application);
        visitDao = database.visitDao();

    }
    public void insert(Visit visit) {
        new InsertVisitAsyncTask(visitDao).execute(visit);
    }
    public void update(Visit visit) {
        new UpdateVisitAsyncTask(visitDao).execute(visit);
    }
    public void delete(Visit visit) {
        new DeleteVisitAsyncTask(visitDao).execute(visit);
    }
    public void deleteAllVisits() {
        new DeleteAllVisitsAsyncTask(visitDao).execute();
    }
    public Cursor getSyncData(SimpleSQLiteQuery query){ return visitDao.getSyncCursor(query);}

    private static class InsertVisitAsyncTask extends AsyncTask<Visit, Void, Void> {
        private final VisitDao visitDao;

        private InsertVisitAsyncTask(VisitDao visitDao) {
            this.visitDao = visitDao;
        }

        @Override
        protected Void doInBackground(Visit... visits) {
            visitDao.insert(visits[0]);
            return null;
        }
    }

    private static class UpdateVisitAsyncTask extends AsyncTask<Visit, Void, Void> {
        private final VisitDao visitDao;

        private UpdateVisitAsyncTask(VisitDao visitDao) {
            this.visitDao = visitDao;
        }

        @Override
        protected Void doInBackground(Visit... visits) {
            visitDao.update(visits[0]);
            return null;
        }
    }

    private static class DeleteVisitAsyncTask extends AsyncTask<Visit, Void, Void> {
        private final VisitDao visitDao;

        private DeleteVisitAsyncTask(VisitDao visitDao) {
            this.visitDao = visitDao;
        }

        @Override
        protected Void doInBackground(Visit... visits) {
            visitDao.delete(visits[0]);
            return null;
        }
    }

    private static class DeleteAllVisitsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final VisitDao visitDao;

        private DeleteAllVisitsAsyncTask(VisitDao visitDao) {
            this.visitDao = visitDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            visitDao.deleteAllVisits();
            return null;
        }
    }
}
