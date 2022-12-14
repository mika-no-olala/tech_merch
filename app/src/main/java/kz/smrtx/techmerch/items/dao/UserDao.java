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

    @Query("select * from ST_USER where locationCode=:locationCode and roleCode=:roleCode")
    LiveData<List<User>> getUsersByCityAndRole(int locationCode, int roleCode);

    @Query("select count(*) from ST_USER where locationCode=:locationCode and roleCode=:roleCode")
    int getNumberOfUsers(int locationCode, int roleCode);

    @Query("select roleCode from ST_USER where code=:userCode")
    int getUserRole(int userCode);
}
