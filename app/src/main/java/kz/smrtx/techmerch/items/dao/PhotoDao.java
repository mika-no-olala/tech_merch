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
import kz.smrtx.techmerch.items.entities.Photo;

@Dao
public interface PhotoDao {
    @Insert
    void insert(Photo photo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPhotos(List<Photo> photos);

    @Delete
    void delete(Photo photo);

    @Query("delete from ST_REQUEST_PHOTO")
    void deleteAllPhotos();

    @Query("select * from ST_REQUEST_PHOTO where REP_REQ_CODE=:requestCode and REP_USE_ROLE=5")
    LiveData<List<Photo>> getPhotosByTMR(String requestCode);

    @Query("select * from ST_REQUEST_PHOTO where REP_REQ_CODE=:requestCode and REP_USE_ROLE=4")
    LiveData<List<Photo>> getPhotosByTech(String requestCode);
}
