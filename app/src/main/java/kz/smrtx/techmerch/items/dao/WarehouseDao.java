package kz.smrtx.techmerch.items.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import kz.smrtx.techmerch.items.entities.Warehouse;

@Dao
public interface WarehouseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Warehouse> warehouses);

    @Update
    void update(Warehouse warehouse);

    @Query("delete from ST_WAREHOUSE_CONTENT")
    void deleteAllWarehouseContent();

    @Query("select * from ST_WAREHOUSE_CONTENT where WAC_CONTENT_NAME=:equipment and WAC_WAR_ID=:warehouseId")
    LiveData<List<Warehouse>> getWarehouseByIdAndEquipment(String equipment, int warehouseId);

    @Query("select * from ST_WAREHOUSE_CONTENT")
    LiveData<List<Warehouse>> getAllWarehouseContent();

    @Query("select * from ST_WAREHOUSE_CONTENT group by WAC_WAR_ID")
    LiveData<List<Warehouse>> getAllWarehouses();

    @Query("select * from ST_WAREHOUSE_CONTENT where WAC_LOC_CODE in (:locationCode) group by WAC_WAR_ID")
    LiveData<List<Warehouse>> getAllWarehousesByCity(int[] locationCode);

    @Query("select * from ST_WAREHOUSE_CONTENT where WAC_LOC_CODE in (:locationCode)")
    LiveData<List<Warehouse>> getAllWarehouseContentByCity(int[] locationCode);
}
