package kz.smrtx.techmerch.items.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import kz.smrtx.techmerch.items.entities.SalePointItem;

@Dao
public interface ChoosePointsDao {

    @Query("select id, " +
            "name,  " +
            "house, " +
            "code, " +
            "owner, " +
            "phone," +
            "longitude, " +
            "latitude " +
            "from ST_SALEPOINT")
    LiveData<List<SalePointItem>> getSalePoints();

    @Query("select id, " +
            "name,  " +
            "house, " +
            "code, " +
            "owner, " +
            "phone," +
            "longitude, " +
            "latitude " +
            "from ST_SALEPOINT_FTS sp " +
            "where ST_SALEPOINT_FTS match :statement")
    LiveData<List<SalePointItem>> getSalePointsByFilter(String statement);

//    @Query("select coalesce(sp.id, 'Не задан'), " +
//            "coalesce(sp.name,'Не задан'),  " +
//            "coalesce(sp.house, 'Не задан'), " +
//            "coalesce(sp.code, 'Не задан'), " +
//            "coalesce(sp.owner, 'Не задан'), " +
//            "coalesce(sp.phone,'Не задан')," +
//            "coalesce(sp.longitude, 'Не задан')," +
//            "coalesce(sp.latitude,'Не задан') " +
//            "from ST_SALEPOINT sp " +
//            "inner join ST_ROUTE_CALENDAR src on src.ROU_SAL_CODE = sp.SALCODE " +
//            "where src.ROU_DATE = :date ")
//    LiveData<List<SalePointItem>> getAppointedSalePoints(String date);

//    @Query("select coalesce(sp.id, 'Не задан'), " +
//            "coalesce(sp.name,'Не задан'),  " +
//            "coalesce(sp.house, 'Не задан'), " +
//            "coalesce(sp.code, 'Не задан'), " +
//            "coalesce(sp.owner, 'Не задан'), " +
//            "coalesce(sp.phone,'Не задан')," +
//            "coalesce(sp.longitude, 'Не задан')," +
//            "coalesce(sp.latitude,'Не задан') " +
//            "from ST_SALEPOINT_FTS sp " +
//            "inner join ST_ROUTE_CALENDAR src on src.ROU_SAL_CODE like sp.sALCODE " +
//            "where src.ROU_DATE = :date and ST_SALEPOINT_FTS match :statement")
//    LiveData<List<SalePointItem>> getAppointedSalePointsByFilter(String date, String statement);

    @Query("select id, " +
            "name,  " +
            "house, " +
            "code, " +
            "owner, " +
            "phone," +
            "longitude, " +
            "latitude " +
            "from ST_SALEPOINT sp " +
            "where sp.code like :salCode ")
    LiveData<SalePointItem> getSalePointByCode(String salCode);
}
