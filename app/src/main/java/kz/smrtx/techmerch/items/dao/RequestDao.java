package kz.smrtx.techmerch.items.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
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

    @Query("select count(*) from ST_REQUEST where REQ_SAL_CODE=:salePointCode")
    int selectNumberFromOutlet(String salePointCode);

    @Query("select * from ST_REQUEST where REQ_USE_CODE_APPOINTED=:userCode")
    LiveData<List<Request>> getRequestsByAppointed(int userCode);

    @Query("select * from ST_REQUEST where REQ_USE_CODE_APPOINTED=:userCode and REQ_SAL_CODE=:salePointCode")
    LiveData<List<Request>> getRequestsByAppointed(int userCode, int salePointCode);

    @Query("select * from ST_REQUEST where REQ_CODE=:code")
    LiveData<Request> getRequestByCode(String code);

}
