package kz.smrtx.techmerch.items.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import kz.smrtx.techmerch.items.entities.Note;
import kz.smrtx.techmerch.items.entities.TableUpdated;
import kz.smrtx.techmerch.items.repositories.NoteRepository;
import kz.smrtx.techmerch.items.repositories.TableUpdatedRepository;

public class TableUpdatedViewModel extends AndroidViewModel {
    private TableUpdatedRepository tableUpdatedRepository;

    public TableUpdatedViewModel(@NonNull Application application) {
        super(application);
        tableUpdatedRepository = new TableUpdatedRepository(application);
    }

    public void insert(List<TableUpdated> tablesInfo) { tableUpdatedRepository.insert(tablesInfo); }
    public void deleteAll() {
        tableUpdatedRepository.deleteAllTablesInfo();
    }
    public List<TableUpdated> getTablesInfo() {
        return tableUpdatedRepository.getTablesInfo();
    }
}
