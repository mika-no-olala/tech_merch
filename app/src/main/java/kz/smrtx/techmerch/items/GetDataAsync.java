package kz.smrtx.techmerch.items;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import java.util.List;

import kz.smrtx.techmerch.items.entities.Element;
import kz.smrtx.techmerch.items.viewmodels.ElementViewModel;

@SuppressLint("StaticFieldLeak")
public class GetDataAsync extends AsyncTask<Void, Void, Void> {
    private final ElementViewModel elementViewModel;
    private final int code;

    @Override
    protected void onPostExecute(Void aVoid) {}

    public GetDataAsync(ElementViewModel elementViewModel, int code) {
        this.elementViewModel = elementViewModel;
        this.code = code;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        getLists(code);
        return null;
    }

    @SuppressLint("Range")
    public List<Element> getLists(int elementCode) {
        return elementViewModel.getElementList(elementCode);
    }
}
