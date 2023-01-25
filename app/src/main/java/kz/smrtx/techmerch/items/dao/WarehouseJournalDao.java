package kz.smrtx.techmerch.items.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import kz.smrtx.techmerch.items.entities.WarehouseJournal;

@Dao
public interface WarehouseJournalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<WarehouseJournal> journal);

    @Delete
    void delete(WarehouseJournal journal);

    @Query("delete from WAREHOUSE_JOURNAL")
    void deleteAll();

    @Query("select * from WAREHOUSE_JOURNAL")
    LiveData<List<WarehouseJournal>> getSupplies();

    @Query("select count(*) from WAREHOUSE_JOURNAL")
    int getSuppliesQuantity();
}
