package kz.smrtx.techmerch.items.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import kz.smrtx.techmerch.items.entities.SalePoint;

@Dao
public interface SalePointDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSalePoints(List<SalePoint> salePoints);

    @Query("delete from ST_SALEPOINT")
    void deleteAllSalePoints();
}
