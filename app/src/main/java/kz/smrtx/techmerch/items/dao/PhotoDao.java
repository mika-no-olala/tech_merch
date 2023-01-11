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

    @Query("update ST_REQUEST_PHOTO set NES_TO_UPDATE='no' where REP_PHOTO=:photoName")
    void updateAfterPartialSync(String photoName);

    @Delete
    void delete(Photo photo);

    @Query("delete from ST_REQUEST_PHOTO")
    void deleteAllPhotos();

    @Query("delete from ST_REQUEST_PHOTO where REP_REQ_CODE=:requestCode")
    void deleteRequestPhotos(String requestCode);

    @Query("select * from ST_REQUEST_PHOTO where REP_REQ_CODE=:requestCode and REP_USE_ROLE=5")
    LiveData<List<Photo>> getPhotosByTMR(String requestCode);

    @Query("select * from ST_REQUEST_PHOTO where REP_REQ_CODE=:requestCode and REP_USE_ROLE=4")
    LiveData<List<Photo>> getPhotosByTech(String requestCode);

    @Query("select * from ST_REQUEST_PHOTO where NES_TO_UPDATE='yes'")
    LiveData<List<Photo>> getPhotosForUpload();

    @Query("select REP_PHOTO from ST_REQUEST_PHOTO where NES_TO_UPDATE='yes'")
    List<String> getPhotosForUploadNoAsync();
}
