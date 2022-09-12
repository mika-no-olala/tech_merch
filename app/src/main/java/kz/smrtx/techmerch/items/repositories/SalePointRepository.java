package kz.smrtx.techmerch.items.repositories;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import kz.smrtx.techmerch.items.dao.SalePointDao;
import kz.smrtx.techmerch.items.entities.SalePoint;
import kz.smrtx.techmerch.items.entities.TechDatabase;

public class SalePointRepository {

    private final SalePointDao salePoint;

    public SalePointRepository(Application application) {
        TechDatabase db = TechDatabase.getInstance(application);
        salePoint = db.salePointDao();
    }

    public void insertSalePoints(List<SalePoint> salePoints){
        new insertSalePointAsyncTask(salePoint).execute(salePoints);
    }

    public void deleteSalePoints(){
        new deleteSalePointAsyncTask(salePoint).execute();
    }

    private static class insertSalePointAsyncTask extends AsyncTask<List<SalePoint>, Void, Void>{

        private SalePointDao salePointDao;

        public insertSalePointAsyncTask(SalePointDao salePointDao) {
            this.salePointDao = salePointDao;
        }

        @Override
        protected Void doInBackground(List<SalePoint>[] lists) {
            salePointDao.insertSalePoints(lists[0]);
            return null;
        }
    }

    private static class deleteSalePointAsyncTask extends AsyncTask<Void, Void, Void>{
        private SalePointDao salePointDao;

        public deleteSalePointAsyncTask(SalePointDao salePointDao) {
            this.salePointDao = salePointDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            salePointDao.deleteAllSalePoints();
            return null;
        }
    }
}
