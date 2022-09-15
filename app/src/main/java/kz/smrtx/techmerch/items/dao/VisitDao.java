package kz.smrtx.techmerch.items.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import kz.smrtx.techmerch.items.entities.SalePointItem;
import kz.smrtx.techmerch.items.entities.Visit;

@Dao
public interface VisitDao {
    @Insert
    void insert(Visit visit);

    @Update
    void update(Visit visit);

    @Delete
    void delete(Visit visit);

    @Query("delete from ST_VISIT")
    void deleteAllVisits();

    @Query("select * from ST_VISIT sp " +
            "where sp.number like :number ")
    LiveData<Visit> getVisitByNumber(String number);

    @RawQuery
    Cursor getSyncCursor(SupportSQLiteQuery supportSQLiteQuery);
}
