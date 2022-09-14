package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.SessionActivity;
import kz.smrtx.techmerch.items.entities.Session;
import kz.smrtx.techmerch.items.viewmodels.SessionViewModel;

public class OperationsFragment extends Fragment {

    private Session session;
    private SessionViewModel sessionViewModel;
    private String dateStarted;

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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operations, container, false);

        TextView startWork = view.findViewById(R.id.startWorkCheckPoint);
        Button workIsOver = view.findViewById(R.id.workIsOver);
        CardView outlet = view.findViewById(R.id.outlet);
        CardView tmrsRequests = view.findViewById(R.id.tmrsRequests);
        listener.getPageName(getResources().getString(R.string.operations));
        dateStarted = Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss");
        startWork.setText(getResources().getString(R.string.start_work_check_point) + ": " + dateStarted);

        sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);

//        if (getArguments()!=null) {
//            if (getArguments().get("USER_ROLE").equals("tmr")) {
//                tmrsRequests.setVisibility(View.GONE);
//            }
//        }
        if (Ius.readSharedPreferences(this.getContext(), Ius.USER_ROLE_CODE).equals("5"))
            tmrsRequests.setVisibility(View.GONE);

        generateSession();

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

    public void generateSession() {
        session = new Session();
        session.setCode(Ius.generateUniqueCode(this.getContext(), "s"));
        session.setStarted(dateStarted);
        session.setUserId(Integer.parseInt(Ius.readSharedPreferences(this.getContext(), Ius.USER_ID)));
        sessionViewModel.insert(session);
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
                session.setFinished(Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss"));
                sessionViewModel.update(session);
                dialog.cancel();
                requireActivity().finish();
            }
        });
    }
}