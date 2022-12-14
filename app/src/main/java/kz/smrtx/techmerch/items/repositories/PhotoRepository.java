package kz.smrtx.techmerch.items.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import kz.smrtx.techmerch.items.dao.NoteDao;
import kz.smrtx.techmerch.items.dao.PhotoDao;
import kz.smrtx.techmerch.items.entities.Note;
import kz.smrtx.techmerch.items.entities.Photo;
import kz.smrtx.techmerch.items.entities.TechDatabase;

public class PhotoRepository {
    private PhotoDao photoDao;

    public PhotoRepository(Application application) {
        TechDatabase database = TechDatabase.getInstance(application);
        photoDao = database.photoDao();
    }
    public void insert(Photo photo) {
        new InsertPhotoAsyncTask(photoDao).execute(photo);
    }
    public void insertPhotos(List<Photo> photos) { new InsertPhotosAsyncTask(photoDao).execute(photos); }
    public void updateAfterPartialSync(String photoName) { new UpdatePhotosAsyncTask(photoDao).execute(photoName); }
    public void delete(Photo photo) {
        new DeletePhotoAsyncTask(photoDao).execute(photo);
    }
    public void deleteAllPhotos() {
        new DeleteAllPhotosAsyncTask(photoDao).execute();
    }
    public void deleteRequestPhotos(String requestCode) {
        new DeleteRequestPhotosAsyncTask(photoDao).execute(requestCode);
    }
    public LiveData<List<Photo>> getPhotosByTech(String requestCode) { return photoDao.getPhotosByTech(requestCode); }
    public LiveData<List<Photo>> getPhotosByTMR(String requestCode) { return photoDao.getPhotosByTMR(requestCode); }
    public LiveData<List<Photo>> getPhotosForUpload() { return photoDao.getPhotosForUpload(); }
    public List<String> getPhotosForUploadNoAsync() {
        return photoDao.getPhotosForUploadNoAsync();
    }

    private static class InsertPhotoAsyncTask extends AsyncTask<Photo, Void, Void> {
        private final PhotoDao photoDao;

        private InsertPhotoAsyncTask(PhotoDao photoDao) {
            this.photoDao = photoDao;
        }

        @Override
        protected Void doInBackground(Photo... photos) {
            photoDao.insert(photos[0]);
            return null;
        }
    }

    private static class InsertPhotosAsyncTask extends AsyncTask<List<Photo>, Void, Void> {
        private final PhotoDao photoDao;

        private InsertPhotosAsyncTask(PhotoDao photoDao) { this.photoDao = photoDao; }

        @Override
        protected Void doInBackground(List<Photo>[] notes) {
            photoDao.insertPhotos(notes[0]);
            return null;
        }
    }

    private static class UpdatePhotosAsyncTask extends AsyncTask<String, Void, Void> {
        private final PhotoDao photoDao;

        private UpdatePhotosAsyncTask(PhotoDao photoDao) { this.photoDao = photoDao; }

        @Override
        protected Void doInBackground(String... photoName) {
            photoDao.updateAfterPartialSync(photoName[0]);
            return null;
        }
    }

    private static class DeletePhotoAsyncTask extends AsyncTask<Photo, Void, Void> {
        private final PhotoDao photoDao;

        private DeletePhotoAsyncTask(PhotoDao photoDao) {
            this.photoDao = photoDao;
        }

        @Override
        protected Void doInBackground(Photo... photos) {
            photoDao.delete(photos[0]);
            return null;
        }
    }

    private static class DeleteAllPhotosAsyncTask extends AsyncTask<Void, Void, Void> {
        private final PhotoDao photoDao;

        private DeleteAllPhotosAsyncTask(PhotoDao photoDao) {
            this.photoDao = photoDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            photoDao.deleteAllPhotos();
            return null;
        }
    }
    private static class DeleteRequestPhotosAsyncTask extends AsyncTask<String, Void, Void> {
        private final PhotoDao photoDao;

        private DeleteRequestPhotosAsyncTask(PhotoDao photoDao) {
            this.photoDao = photoDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            photoDao.deleteRequestPhotos(strings[0]);
            return null;
        }
    }
}
