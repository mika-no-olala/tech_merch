package kz.smrtx.techmerch.items.repositories;

import android.app.Application;
import android.database.Cursor;
import android.os.AsyncTask;

import androidx.sqlite.db.SimpleSQLiteQuery;

import kz.smrtx.techmerch.items.dao.SessionDao;
import kz.smrtx.techmerch.items.dao.VisitDao;
import kz.smrtx.techmerch.items.entities.Session;
import kz.smrtx.techmerch.items.entities.TechDatabase;
import kz.smrtx.techmerch.items.entities.Visit;

public class SessionRepository {
    private SessionDao sessionDao;

    public SessionRepository(Application application) {
        TechDatabase database = TechDatabase.getInstance(application);
        sessionDao = database.sessionDao();

    }
    public void insert(Session session) {
        new InsertSessionAsyncTask(sessionDao).execute(session);
    }
    public void update(Session session) {
        new UpdateSessionAsyncTask(sessionDao).execute(session);
    }
    public void delete(Session session) {
        new DeleteSessionAsyncTask(sessionDao).execute(session);
    }
    public void deleteAllSessions() {
        new DeleteAllSessionsAsyncTask(sessionDao).execute();
    }
    public Cursor getSyncData(SimpleSQLiteQuery query){ return sessionDao.getSyncCursor(query);}

    private static class InsertSessionAsyncTask extends AsyncTask<Session, Void, Void> {
        private final SessionDao sessionDao;

        private InsertSessionAsyncTask(SessionDao sessionDao) {
            this.sessionDao = sessionDao;
        }

        @Override
        protected Void doInBackground(Session... sessions) {
            sessionDao.insert(sessions[0]);
            return null;
        }
    }

    private static class UpdateSessionAsyncTask extends AsyncTask<Session, Void, Void> {
        private final SessionDao sessionDao;

        private UpdateSessionAsyncTask(SessionDao sessionDao) {
            this.sessionDao = sessionDao;
        }

        @Override
        protected Void doInBackground(Session... sessions) {
            sessionDao.update(sessions[0]);
            return null;
        }
    }

    private static class DeleteSessionAsyncTask extends AsyncTask<Session, Void, Void> {
        private final SessionDao sessionDao;

        private DeleteSessionAsyncTask(SessionDao sessionDao) {
            this.sessionDao = sessionDao;
        }

        @Override
        protected Void doInBackground(Session... sessions) {
            sessionDao.delete(sessions[0]);
            return null;
        }
    }

    private static class DeleteAllSessionsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final SessionDao sessionDao;

        private DeleteAllSessionsAsyncTask(SessionDao sessionDao) {
            this.sessionDao = sessionDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            sessionDao.deleteAllSessions();
            return null;
        }
    }
}
