package kz.smrtx.techmerch.items.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import kz.smrtx.techmerch.items.entities.Note;
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.repositories.NoteRepository;
import kz.smrtx.techmerch.items.repositories.RequestRepository;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository noteRepository;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
    }

    public void insert(Note note) {
        noteRepository.insert(note);
    }
    public void insertNotes(List<Note> notes) { noteRepository.insertNotes(notes); }
    public void update(Note note) {
        noteRepository.update(note);
    }
    public void delete(Note note) {
        noteRepository.delete(note);
    }
    public void deleteAllNotes() {
        noteRepository.deleteAllNotes();
    }
    public int getNumberFromSalePoint(int salePointCode) {
        return noteRepository.getNumberFromSalePoint(salePointCode);
    }
    public LiveData<List<Note>> getNotesFromSalePoint(int salePointCode) {
        return noteRepository.getNotesFromSalePoint(salePointCode);
    }
}
