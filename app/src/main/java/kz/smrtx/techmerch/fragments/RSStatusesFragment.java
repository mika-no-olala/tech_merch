package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
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
import kz.smrtx.techmerch.activities.SessionActivity;

public class RSStatusesFragment extends Fragment {

    private String dateStarted;

    private FragmentListener listener;
    public interface FragmentListener {
        void getPageName(String name);
    }

    public static RSStatusesFragment getInstance(String dateStarted) {
        RSStatusesFragment fragment = new RSStatusesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("DATE_STARTED", dateStarted);
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operations, container, false);

        if (getArguments()!=null) {
            dateStarted = getArguments().getString("DATE_STARTED");
        }

        TextView startWork = view.findViewById(R.id.startWorkCheckPoint);
        Button workIsOver = view.findViewById(R.id.workIsOver);
        CardView outlet = view.findViewById(R.id.outlet);
        CardView tmrsRequests = view.findViewById(R.id.tmrsRequests);
        listener.getPageName(getResources().getString(R.string.operations));
        startWork.setText(getResources().getString(R.string.start_work_check_point) + ": " + dateStarted);

        if (Ius.readSharedPreferences(this.getContext(), Ius.USER_ROLE_CODE).equals("5"))
            tmrsRequests.setVisibility(View.GONE);

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
                ((SessionActivity)requireActivity()).openDialog();
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
}