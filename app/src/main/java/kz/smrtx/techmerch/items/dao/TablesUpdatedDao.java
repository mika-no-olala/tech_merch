package kz.smrtx.techmerch.items.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import kz.smrtx.techmerch.items.entities.TableUpdated;

@Dao
public interface TablesUpdatedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<TableUpdated> tablesInfo);

    @Query("delete from WT_TABLES_UPDATED")
    void deleteAllTablesInfo();

    @Query("select * from WT_TABLES_UPDATED")
    List<TableUpdated> getTablesInfo();
}
