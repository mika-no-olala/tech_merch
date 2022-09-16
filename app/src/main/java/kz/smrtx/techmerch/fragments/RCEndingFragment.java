package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.CreateRequestActivity;
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.entities.User;
import kz.smrtx.techmerch.items.viewmodels.UserViewModel;

public class RCEndingFragment extends Fragment {

    private AutoCompleteTextView executor;
    private EditText comment;
    private UserViewModel userViewModel;
    private ArrayList<String> users = new ArrayList<>();

    public static RCEndingFragment getInstance() {
        RCEndingFragment fragment = new RCEndingFragment();
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rc_ending, container, false);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        executor = view.findViewById(R.id.executor);
        comment = view.findViewById(R.id.comment);

        getList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, users);
        executor.setAdapter(adapter);

        executor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                boolean correct = users.contains(editable.toString());
                ((CreateRequestActivity)requireActivity()).setExecutor(editable.toString().trim(), correct);
            }
        });

        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                ((CreateRequestActivity)requireActivity()).setComment(editable.toString().trim());
            }
        });

        return view;
    }

    private void getList() {
        userViewModel.getUserList(4).observe(getViewLifecycleOwner(), u -> {
            for (User user : u) {
                users.add(user.getCode() + " - " + user.getName());
            }
        });
    }
}