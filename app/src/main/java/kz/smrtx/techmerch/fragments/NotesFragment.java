package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.SessionActivity;
import kz.smrtx.techmerch.adapters.CardAdapterNotes;
import kz.smrtx.techmerch.items.entities.Note;
import kz.smrtx.techmerch.items.viewmodels.NoteViewModel;


public class NotesFragment extends Fragment {

    private View view;
    private NoteViewModel noteViewModel;
    private RecyclerView recyclerView;
    private TextView noNotes;
    private List<Note> notes = new ArrayList<>();
    private int salePointCode;
    private int userCode;

    private FragmentListener listener;
    public interface FragmentListener {
        void getPageName(String name);
    }

    public static NotesFragment getInstance(String outletCode) {
        NotesFragment fragment = new NotesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("OUT_CODE", outletCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notes, container, false);

        listener.getPageName(getResources().getString(R.string.notes_on_outlet));
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        recyclerView = view.findViewById(R.id.recyclerView);

        if (getArguments()!=null) {
            salePointCode = Integer.parseInt(getArguments().getString("OUT_CODE"));
            getNotes(salePointCode);
        }

        noNotes = view.findViewById(R.id.noNotes);
        Button create = view.findViewById(R.id.create);
        userCode = Integer.parseInt(Ius.readSharedPreferences(this.getContext(), Ius.USER_CODE));

        create.setOnClickListener(view -> openDialogComment());

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void getNotes(int outletCode) {
        noteViewModel.getNotesFromSalePoint(outletCode).observe(getViewLifecycleOwner(), s -> {
            Log.i("getNotes", "by code " + outletCode);
            if (s!=null) {
                notes = s;
                setAdapter();
            }
        });
    }

    private void setAdapter() {
        RecyclerView.LayoutManager layoutManager;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        if (notes.isEmpty()) {
            noNotes.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            noNotes.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        CardAdapterNotes cardAdapter = new CardAdapterNotes(notes, this.getContext());
        recyclerView.setAdapter(cardAdapter);
        cardAdapter.setOnItemClickListener(position -> {
            Note note = notes.get(position);
            if (note.getNOT_USE_CODE()!=userCode) {
                SessionActivity.getInstance().createToast(getString(R.string.note_delete_warning), false);
                return;
            }

            openDialogAcception(note);
        });
    }

    private void openDialogComment() {
        Dialog dialog = Ius.createDialog(this.getContext(), R.layout.dialog_window_request_acception,
                getResources().getString(R.string.note));

        EditText comment = dialog.findViewById(R.id.comment);
        Button positive = dialog.findViewById(R.id.positive);

        dialog.show();

        positive.setOnClickListener(view -> {
            String commentStr = comment.getText().toString().trim();
            if (commentStr.length()==0) {
                SessionActivity.getInstance().createToast(getString(R.string.fill_field), false);
                return;
            }

            String date = Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss");
            String name = Ius.readSharedPreferences(this.getContext(), Ius.USER_NAME);
            String visitNumber = Ius.readSharedPreferences(this.getContext(), Ius.LAST_VISIT_NUMBER);

            Note note = new Note(date, salePointCode, userCode, name, commentStr, visitNumber,"yes");
            noteViewModel.insert(note);

            SessionActivity.getInstance().createToast(getString(R.string.note_success), true);
            dialog.cancel();
            OperationsOnOutletFragment.getInstance().cancelVisitClear();
        });
    }

    private void openDialogAcception(Note note) {
        Dialog dialog = Ius.createDialogAcception(this.getContext(),
                getResources().getString(R.string.note_deleting),
                getResources().getString(R.string.note_deleting_desc),
                true);

        Button positive = dialog.findViewById(R.id.positive);
        Button negative = dialog.findViewById(R.id.negative);

        dialog.show();

        negative.setOnClickListener(view -> dialog.cancel());

        positive.setOnClickListener(view -> {
            if (note.getNES_TO_UPDATE().equals("yes"))
                noteViewModel.delete(note);
            else {
                String visitNumber = Ius.readSharedPreferences(this.getContext(), Ius.LAST_VISIT_NUMBER);

                note.setNES_TO_UPDATE("delete");
                note.setNOT_VIS_NUMBER(visitNumber);
                note.setNOT_CREATED(Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss")); //need for deleting unnec visit data
                noteViewModel.update(note);
            }

            dialog.cancel();
            OperationsOnOutletFragment.getInstance().cancelVisitClear();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (NotesFragment.FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement onFragmentListener");
        }
    }
}