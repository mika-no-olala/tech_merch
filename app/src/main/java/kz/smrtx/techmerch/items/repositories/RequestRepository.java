package kz.smrtx.techmerch.items.repositories;

import android.app.Application;
import android.database.Cursor;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.ArrayList;
import java.util.List;

import kz.smrtx.techmerch.items.dao.RequestDao;
import kz.smrtx.techmerch.items.dao.VisitDao;
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.entities.SalePoint;
import kz.smrtx.techmerch.items.entities.TechDatabase;
import kz.smrtx.techmerch.items.entities.Visit;

public class RequestRepository {
    private RequestDao requestDao;

    public RequestRepository(Application application) {
        TechDatabase database = TechDatabase.getInstance(application);
        requestDao = database.requestDao();
    }
    public void insert(Request request) {
        new InsertRequestAsyncTask(requestDao).execute(request);
    }
    public void insertRequests(List<Request> requests) { new InsertRequestsAsyncTask(requestDao).execute(requests); }
    public void update(Request request) {
        new UpdateRequestAsyncTask(requestDao).execute(request);
    }
    public void delete(Request request) {
        new DeleteRequestAsyncTask(requestDao).execute(request);
    }
    public void deleteAllRequests() {
        new DeleteAllRequestsAsyncTask(requestDao).execute();
    }
    public int selectNumberFromOutlets(String code) { return requestDao.selectNumberFromOutlet(code); }
    public LiveData<List<Request>> getRequestsByAppointed(int userCode) { return requestDao.getRequestsByAppointed(userCode); }
    public LiveData<List<Request>> getRequestsByAppointed(int userCode, int salePointCode) { return requestDao.getRequestsByAppointed(userCode, salePointCode); }
    public LiveData<Request> getRequestByCode(String code) { return requestDao.getRequestByCode(code); }

    private static class InsertRequestAsyncTask extends AsyncTask<Request, Void, Void> {
        private final RequestDao requestDao;

        private InsertRequestAsyncTask(RequestDao requestDao) {
            this.requestDao = requestDao;
        }

        @Override
        protected Void doInBackground(Request... requests) {
            requestDao.insert(requests[0]);
            return null;
        }
    }

    private static class InsertRequestsAsyncTask extends AsyncTask<List<Request>, Void, Void> {
        private final RequestDao requestDao;

        private InsertRequestsAsyncTask(RequestDao requestDao) { this.requestDao = requestDao; }

        @Override
        protected Void doInBackground(List<Request>[] lists) {
            requestDao.insertRequests(lists[0]);
            return null;
        }
    }

    private static class UpdateRequestAsyncTask extends AsyncTask<Request, Void, Void> {
        private final RequestDao requestDao;

        private UpdateRequestAsyncTask(RequestDao requestDao) {
            this.requestDao = requestDao;
        }

        @Override
        protected Void doInBackground(Request... requests) {
            requestDao.update(requests[0]);
            return null;
        }
    }

    private static class DeleteRequestAsyncTask extends AsyncTask<Request, Void, Void> {
        private final RequestDao requestDao;

        private DeleteRequestAsyncTask(RequestDao requestDao) {
            this.requestDao = requestDao;
        }

        @Override
        protected Void doInBackground(Request... requests) {
            requestDao.delete(requests[0]);
            return null;
        }
    }

    private static class DeleteAllRequestsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final RequestDao requestDao;

        private DeleteAllRequestsAsyncTask(RequestDao requestDao) {
            this.requestDao = requestDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            requestDao.deleteAllRequests();
            return null;
        }
    }
}
