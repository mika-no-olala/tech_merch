package kz.smrtx.techmerch.items.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import kz.smrtx.techmerch.items.entities.Note;
import kz.smrtx.techmerch.items.entities.Request;

@Dao
public interface NoteDao {
    @Insert
    void insert(Note note);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotes(List<Note> notes);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("delete from ST_NOTES")
    void deleteAllNotes();

    @Query("select count(*) from ST_NOTES where NOT_SAL_CODE=:salePointCode AND NES_TO_UPDATE!='delete'")
    int getNumberFromSalePoint(int salePointCode);

    @Query("select * from ST_NOTES where NOT_SAL_CODE=:salePointCode AND NES_TO_UPDATE!='delete' " +
            "order by NOT_CREATED desc")
    LiveData<List<Note>> getNotesFromSalePoint(int salePointCode);
}
