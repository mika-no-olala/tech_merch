package kz.smrtx.techmerch.items.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import kz.smrtx.techmerch.items.entities.SalePointItem;
import kz.smrtx.techmerch.items.repositories.ChoosePointsRepository;

public class ChoosePointsViewModel extends AndroidViewModel{

    private ChoosePointsRepository choosePointsRepository;

    public ChoosePointsViewModel(@NonNull Application application) {
        super(application);
        choosePointsRepository = new ChoosePointsRepository(application);
    }

    public LiveData<List<SalePointItem>> getSalePoints(){return choosePointsRepository.getSalePoints();}

    public LiveData<List<SalePointItem>> getSalePointsWithRequestsNumber(int useCode) {
        return choosePointsRepository.getSalePointsWithRequestsNumber(useCode);
    }

    public LiveData<List<SalePointItem>> getSalePointsByFilter(String statement){return choosePointsRepository.getSalePointsByFilter(statement);}

    public LiveData<SalePointItem> getSalePointByCode(String salCode){return choosePointsRepository.getSalePointByCode(salCode);}

//    public LiveData<List<SalePointItem>> getAppointedSalePoints(String date){ return  choosePointsRepository.getAppointedSalePoints(date); }
//
//    public LiveData<List<SalePointItem>> getAppointedSalePointsByFilter(String date, String statement){ return choosePointsRepository.getAppointedSalePointsByFilter(date, statement);}

}
