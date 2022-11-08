package kz.smrtx.techmerch.items.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import kz.smrtx.techmerch.items.dao.NoteDao;
import kz.smrtx.techmerch.items.dao.TablesUpdatedDao;
import kz.smrtx.techmerch.items.entities.Note;
import kz.smrtx.techmerch.items.entities.TableUpdated;
import kz.smrtx.techmerch.items.entities.TechDatabase;

public class TableUpdatedRepository {
    private TablesUpdatedDao tablesUpdatedDao;

    public TableUpdatedRepository(Application application) {
        TechDatabase database = TechDatabase.getInstance(application);
        tablesUpdatedDao = database.tablesUpdatedDao();
    }
    public void insert(List<TableUpdated> tablesInfo) { new InsertTableUpdatedAsyncTask(tablesUpdatedDao).execute(tablesInfo); }
    public void deleteAllTablesInfo() {
        new DeleteAllTablesInfoAsyncTask(tablesUpdatedDao).execute();
    }
    public List<TableUpdated> getTablesInfo() {
        return tablesUpdatedDao.getTablesInfo();
    }

    private static class InsertTableUpdatedAsyncTask extends AsyncTask<List<TableUpdated>, Void, Void> {
        private final TablesUpdatedDao tablesUpdatedDao;

        private InsertTableUpdatedAsyncTask(TablesUpdatedDao tablesUpdatedDao) { this.tablesUpdatedDao = tablesUpdatedDao; }

        @Override
        protected Void doInBackground(List<TableUpdated>[] tablesInfo) {
            tablesUpdatedDao.insert(tablesInfo[0]);
            return null;
        }
    }

    private static class DeleteAllTablesInfoAsyncTask extends AsyncTask<Void, Void, Void> {
        private final TablesUpdatedDao tablesUpdatedDao;

        private DeleteAllTablesInfoAsyncTask(TablesUpdatedDao tablesUpdatedDao) {
            this.tablesUpdatedDao = tablesUpdatedDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            tablesUpdatedDao.deleteAllTablesInfo();
            return null;
        }
    }
}
