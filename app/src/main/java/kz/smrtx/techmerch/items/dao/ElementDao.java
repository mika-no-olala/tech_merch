package kz.smrtx.techmerch.items.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import kz.smrtx.techmerch.items.entities.Element;
import kz.smrtx.techmerch.items.entities.User;

@Dao
public interface ElementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertElements(List<Element> user);

    @Query("delete from ST_LIST_ELEMENTS")
    void deleteAllElements();

    @Query("select * from ST_LIST_ELEMENTS where categoryCode=:categoryCode")
    List<Element> getElementList(int categoryCode);
}
