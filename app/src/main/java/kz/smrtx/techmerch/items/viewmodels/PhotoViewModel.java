package kz.smrtx.techmerch.items.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import kz.smrtx.techmerch.items.entities.Note;
import kz.smrtx.techmerch.items.entities.Photo;
import kz.smrtx.techmerch.items.repositories.NoteRepository;
import kz.smrtx.techmerch.items.repositories.PhotoRepository;

public class PhotoViewModel extends AndroidViewModel {
    private PhotoRepository photoRepository;

    public PhotoViewModel(@NonNull Application application) {
        super(application);
        photoRepository = new PhotoRepository(application);
    }

    public void insert(Photo photo) {
        photoRepository.insert(photo);
    }
    public void insertPhotos(List<Photo> photos) { photoRepository.insertPhotos(photos); }
    public void delete(Photo photo) {
        photoRepository.delete(photo);
    }
    public void deleteAllPhotos() {
        photoRepository.deleteAllPhotos();
    }
    public void deleteRequestPhotos(String requestCode) {
        photoRepository.deleteRequestPhotos(requestCode);
    }
    public LiveData<List<Photo>> getPhotosByTMR(String requestCode) {
        return photoRepository.getPhotosByTMR(requestCode);
    }
    public LiveData<List<Photo>> getPhotosByTech(String requestCode) {
        return photoRepository.getPhotosByTech(requestCode);
    }
    public LiveData<List<Photo>> getPhotosForUpload() {
        return photoRepository.getPhotosForUpload();
    }
}
