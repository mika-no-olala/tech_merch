package kz.smrtx.techmerch.items.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import kz.smrtx.techmerch.items.dao.NoteDao;
import kz.smrtx.techmerch.items.dao.RequestDao;
import kz.smrtx.techmerch.items.entities.Note;
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.entities.TechDatabase;

public class NoteRepository {
    private NoteDao noteDao;

    public NoteRepository(Application application) {
        TechDatabase database = TechDatabase.getInstance(application);
        noteDao = database.noteDao();
    }
    public void insert(Note note) {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }
    public void insertNotes(List<Note> notes) { new InsertNotesAsyncTask(noteDao).execute(notes); }
    public void update(Note note) {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }
    public void delete(Note note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }
    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(noteDao).execute();
    }
    public int getNumberFromSalePoint(int salePointCode) { return noteDao.getNumberFromSalePoint(salePointCode); }
    public LiveData<List<Note>> getNotesFromSalePoint(int salePointCode) { return noteDao.getNotesFromSalePoint(salePointCode); }

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private final NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class InsertNotesAsyncTask extends AsyncTask<List<Note>, Void, Void> {
        private final NoteDao noteDao;

        private InsertNotesAsyncTask(NoteDao noteDao) { this.noteDao = noteDao; }

        @Override
        protected Void doInBackground(List<Note>[] notes) {
            noteDao.insertNotes(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private final NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private final NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private final NoteDao noteDao;

        private DeleteAllNotesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
}
