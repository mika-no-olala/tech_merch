package kz.smrtx.techmerch.items.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import kz.smrtx.techmerch.items.entities.User;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(List<User> user);

    @Query("delete from ST_USER")
    void deleteAllUsers();

    @Query("select * from ST_USER where roleCode=:roleCode")
    LiveData<List<User>> getUserList(int roleCode);
}
