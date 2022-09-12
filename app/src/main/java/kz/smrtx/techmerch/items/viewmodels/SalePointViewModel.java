package kz.smrtx.techmerch.items.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import kz.smrtx.techmerch.items.entities.SalePoint;
import kz.smrtx.techmerch.items.repositories.SalePointRepository;

public class SalePointViewModel extends AndroidViewModel {

    private SalePointRepository salePointRepository;

    public SalePointViewModel(@NonNull Application application) {
        super(application);

        salePointRepository = new SalePointRepository(application);
    }

    public void insertSalePoints(List<SalePoint> salePoints){
        salePointRepository.insertSalePoints(salePoints);
    }

    public void deleteSalePoints(){
        salePointRepository.deleteSalePoints();
    }
}
