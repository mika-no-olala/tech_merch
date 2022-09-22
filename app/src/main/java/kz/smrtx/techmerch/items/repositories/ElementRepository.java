package kz.smrtx.techmerch.items.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import kz.smrtx.techmerch.items.dao.ElementDao;
import kz.smrtx.techmerch.items.entities.Element;
import kz.smrtx.techmerch.items.entities.TechDatabase;

public class ElementRepository {
    private ElementDao elementDao;

    public ElementRepository(Application application) {
        TechDatabase db = TechDatabase.getInstance(application);
        elementDao = db.elementDao();
    }
    public void insertElements(List<Element> elements) {
        new insertElementAsyncTask(elementDao).execute(elements);
    }
    public void deleteElements() {
        new deleteElementsAsyncTask(elementDao).execute();
    }
    public List<Element> getElementList(int elementCode) {
        return elementDao.getElementList(elementCode);
    }

    private static class insertElementAsyncTask extends AsyncTask<List<Element>, Void, Void>{
        private ElementDao elementDao;
        public insertElementAsyncTask(ElementDao elementDao) {
            this.elementDao = elementDao;
        }

        @Override
        protected Void doInBackground(List<Element>... lists) {
            elementDao.insertElements(lists[0]);
            return null;
        }
    }

    private static class deleteElementsAsyncTask extends AsyncTask<Void, Void, Void>{
        private ElementDao elementDao;
        public deleteElementsAsyncTask(ElementDao elementDao) {
            this.elementDao = elementDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            elementDao.deleteAllElements();
            return null;
        }
    }
}
