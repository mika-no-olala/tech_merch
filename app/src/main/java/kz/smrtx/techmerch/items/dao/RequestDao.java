package kz.smrtx.techmerch.items.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import kz.smrtx.techmerch.items.entities.Request;

@Dao
public interface RequestDao {
    @Insert
    void insert(Request request);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRequests(List<Request> requests);

    @Update
    void update(Request request);

    @Delete
    void delete(Request request);

    @Query("delete from ST_REQUEST")
    void deleteAllRequests();

}
