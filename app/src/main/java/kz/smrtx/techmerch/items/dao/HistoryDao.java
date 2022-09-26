package kz.smrtx.techmerch.items.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import kz.smrtx.techmerch.items.entities.History;

@Dao
public interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistory(List<History> histories);

    @Query("delete from ST_REQUEST_HISTORY")
    void deleteAllElements();

    @Query("select * from ST_REQUEST_HISTORY where salePointCode=:salePointCode")
    LiveData<List<History>> getHistoryListBySalCode(int salePointCode);

    // remove ended requests from here
    @Query("SELECT * FROM ST_REQUEST_HISTORY srh " +
            "WHERE (srh.userCode = :userCode OR srh.userCodeAppointed = :userCode) " +
            "  AND srh.created = " +
            "    (" +
            "      SELECT MAX(created) " +
            "      FROM ST_REQUEST_HISTORY srhd " +
            "      WHERE srh.requestCode = srhd.requestCode " +
            "      AND srhd.userCodeAppointed != :userCode " +
            "      GROUP BY srhd.requestCode " +
            "    )")
    LiveData<List<History>> getHistoryListWhichAreRelatedTo(int userCode);

    @Query("SELECT count(*) FROM ST_REQUEST_HISTORY srh " +
            "WHERE (srh.userCode = :userCode OR srh.userCodeAppointed = :userCode) " +
            "  AND srh.salePointCode = :salePointCode " +
            "  AND srh.created = " +
            "    (" +
            "      SELECT MAX(created) " +
            "      FROM ST_REQUEST_HISTORY srhd " +
            "      WHERE srh.requestCode = srhd.requestCode " +
            "      GROUP BY srhd.requestCode " +
            "    )")
    int getRequestsNumberOnSalePointByUser(int userCode, int salePointCode);

    @Query("SELECT userCode FROM ST_REQUEST_HISTORY WHERE requestCode=:requestCode AND userRole=5")
    int getTMRCodeByRequest(String requestCode);
}