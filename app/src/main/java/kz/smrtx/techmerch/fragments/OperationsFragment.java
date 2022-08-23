package kz.smrtx.techmerch.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.SessionActivity;

public class OperationsFragment extends Fragment {
    private FragmentListener listener;
    public interface FragmentListener {
        void getPageName(String name);
    }

    public static OperationsFragment getInstance(String role) {
        OperationsFragment fragment = new OperationsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("USER_ROLE", role);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operations, container, false);

        Button workIsOver = view.findViewById(R.id.workIsOver);
        CardView outlet = view.findViewById(R.id.outlet);
        CardView tmrsRequests = view.findViewById(R.id.tmrsRequests);
        listener.getPageName(getResources().getString(R.string.operations));

        if (getArguments()!=null) {
            if (getArguments().get("USER_ROLE").equals("tmr")) {
                tmrsRequests.setVisibility(View.INVISIBLE);
            }
        }

        outlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SessionActivity)requireActivity()).openOutlets();
//                Navigation.findNavController(view).navigate(R.id.action_operationsFragment_to_outletsFragment);
            }
        });
        workIsOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement onFragmentListener");
        }
    }

    private void openDialog() {
        Dialog dialog = Ius.createDialogAcception(getActivity(), getResources().getString(R.string.finishing_work),
                getResources().getString(R.string.finishing_work_question), true);

        Button yes = dialog.findViewById(R.id.positive);
        Button no = dialog.findViewById(R.id.negative);

        dialog.show();

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                requireActivity().finish();
            }
        });
    }
}