package kz.smrtx.techmerch.items.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import kz.smrtx.techmerch.items.entities.Session;

@Dao
public interface SessionDao {
    @Insert
    void insert(Session session);

    @Update
    void update(Session session);

    @Delete
    void delete(Session session);

    @Query("delete from ST_SESSION")
    void deleteAllSessions();

    @RawQuery
    Cursor getSyncCursor(SupportSQLiteQuery supportSQLiteQuery);
}
