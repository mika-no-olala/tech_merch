package kz.smrtx.techmerch.items.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import kz.smrtx.techmerch.items.entities.Consumable;
import kz.smrtx.techmerch.items.entities.Element;

@Dao
public interface ConsumableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReport(List<Consumable> report);

    @Query("delete from ST_TECHNIC_REPORT")
    void deleteReport();

    @Query("select * from ST_TECHNIC_REPORT where TER_CODE=:reportCode")
    LiveData<List<Consumable>> getReportByCode(String reportCode);

    @Query("select * from ST_TECHNIC_REPORT where TER_CODE=:userCode")
    LiveData<List<Consumable>> getReportByUser(int userCode);
}
