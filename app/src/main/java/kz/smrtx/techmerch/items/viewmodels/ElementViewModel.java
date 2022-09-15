package kz.smrtx.techmerch.items.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import kz.smrtx.techmerch.items.entities.Element;
import kz.smrtx.techmerch.items.repositories.ElementRepository;

public class ElementViewModel extends AndroidViewModel {
    private ElementRepository elementRepository;

    public ElementViewModel(@NonNull Application application) {
        super(application);
        elementRepository = new ElementRepository(application);
    }
    public void insertElements(List<Element> elements) {
        elementRepository.insertElements(elements);
    }
    public void deleteElements() {
        elementRepository.deleteElements();
    }
    public List<Element> getElementList(int elementCode) {
        return elementRepository.getElementList(elementCode);
    }
}
