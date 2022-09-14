package kz.smrtx.techmerch.items.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import kz.smrtx.techmerch.items.dao.ChoosePointsDao;
import kz.smrtx.techmerch.items.entities.SalePointItem;
import kz.smrtx.techmerch.items.entities.TechDatabase;

public class ChoosePointsRepository {

    private ChoosePointsDao choosePointsDao;

    public ChoosePointsRepository(Application application) {
        TechDatabase db = TechDatabase.getInstance(application);
        choosePointsDao = db.choosePointsDao();
    }

    public LiveData<List<SalePointItem>> getSalePoints(){return choosePointsDao.getSalePoints();}

    public LiveData<List<SalePointItem>> getSalePointsByFilter(String statement){return choosePointsDao.getSalePointsByFilter(statement);}

    public LiveData<SalePointItem> getSalePointByCode(String salCode){return choosePointsDao.getSalePointByCode(salCode);}

//    public LiveData<List<SalePointItem>> getAppointedSalePoints(String date)
//    {
//        return  choosePointsDao.getAppointedSalePoints(date);
//    }

//    public LiveData<List<SalePointItem>> getAppointedSalePointsByFilter(String date, String statement){ return choosePointsDao.getAppointedSalePointsByFilter(date, statement);}

}
