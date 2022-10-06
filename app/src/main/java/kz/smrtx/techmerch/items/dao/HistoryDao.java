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

    @Query("SELECT * FROM ST_REQUEST_HISTORY srh " +
            "WHERE srh.userCode = :userCode AND srh.userCodeAppointed != :userCode " +
            "  AND srh.userCodeAppointed != 8 " +
            "  AND srh.created = " +
            "    (" +
            "      SELECT MAX(created) " +
            "      FROM ST_REQUEST_HISTORY srhd " +
            "      WHERE srh.requestCode = srhd.requestCode " +
            "      GROUP BY srhd.requestCode " +
            "    )")
    LiveData<List<History>> getHistoryListWhichAreRelatedTo(int userCode);

    @Query("SELECT * FROM ST_REQUEST_HISTORY srh " +
            "WHERE srh.userCode = :userCode AND srh.userCodeAppointed != :userCode " +
            "  AND srh.userCodeAppointed != 8 " +
            "  AND srh.salePointCode=:salePointCode AND srh.created = " +
            "    (" +
            "      SELECT MAX(created) " +
            "      FROM ST_REQUEST_HISTORY srhd " +
            "      WHERE srh.requestCode = srhd.requestCode " +
            "      GROUP BY srhd.requestCode " +
            "    )")
    LiveData<List<History>> getHistoryListWhichAreRelatedTo(int userCode, int salePointCode);

    @Query("SELECT count(*) FROM ST_REQUEST_HISTORY srh " +
            "WHERE srh.userCode = :userCode AND srh.userCodeAppointed != :userCode " +
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

    @Query("SELECT * FROM ST_REQUEST_HISTORY srh " +
            "   WHERE srh.userCodeAppointed = 8 " +
            "   AND srh.requestCode IN " +
            "       (" +
            "           SELECT srhd.requestCode FROM ST_REQUEST_HISTORY srhd " +
            "           WHERE srhd.userCode = :userCode " +
            "       )")
    LiveData<List<History>> getHistoryListWhichAreRelatedToAndFinished(int userCode);

    @Query("SELECT * FROM ST_REQUEST_HISTORY srh " +
            "   WHERE srh.userCodeAppointed = 8 " +
            "   AND srh.salePointCode= :salePointCode " +
            "   AND srh.requestCode IN " +
            "       (" +
            "           SELECT srhd.requestCode FROM ST_REQUEST_HISTORY srhd " +
            "           WHERE srhd.userCode = :userCode " +
            "       )")
    LiveData<List<History>> getHistoryListWhichAreRelatedToAndFinished(int userCode, int salePointCode);
}
/*

@Query("SELECT * FROM ST_REQUEST_HISTORY srh " +
        "WHERE (srh.userCode = :userCode OR srh.userCodeAppointed = :userCode) " +
        "  AND srh.salePointCode=:salePointCode AND srh.created = " +
        "    (" +
        "      SELECT MAX(created) " +
        "      FROM ST_REQUEST_HISTORY srhd " +
        "      WHERE srh.requestCode = srhd.requestCode " +
        "      AND srhd.userCodeAppointed != :userCode " +
        "      AND srhd.userCodeAppointed != 8 " +
        "      GROUP BY srhd.requestCode " +
        "    )")

 */