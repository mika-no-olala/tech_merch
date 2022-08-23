package kz.smrtx.techmerch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.SessionActivity;

public class OperationsOnOutletFragment extends Fragment {

    private FragmentListener listener;
    public interface FragmentListener {
        void getPageName(String name);
    }

    public static OperationsOnOutletFragment getInstance(String role, String outletName) {
        OperationsOnOutletFragment fragment = new OperationsOnOutletFragment();
        Bundle bundle = new Bundle();
        bundle.putString("USER_ROLE", role);
        bundle.putString("OUT_NAME", outletName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operations_on_outlet, container, false);

        listener.getPageName(getResources().getString(R.string.operations_on_outlet));
        TextView name = view.findViewById(R.id.name);
        TextView detailInformation = view.findViewById(R.id.detailInformation);
        detailInformation.setText(Ius.makeTextUnderlined(detailInformation.getText().toString()));
        CardView createRequest = view.findViewById(R.id.createRequest);

        if (getArguments()!=null) {
            name.setText(getArguments().getString("OUT_NAME"));
            if (getArguments().get("USER_ROLE").equals("tmr")) {
                // do smth
            }
        }

        createRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SessionActivity)requireActivity()).openActivityCreateRequest();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OperationsOnOutletFragment.FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement onFragmentListener");
        }
    }
}