package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.w3c.dom.Text;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.SessionActivity;
import kz.smrtx.techmerch.items.entities.Visit;
import kz.smrtx.techmerch.items.viewmodels.ChoosePointsViewModel;
import kz.smrtx.techmerch.items.viewmodels.VisitViewModel;

public class OperationsOnOutletFragment extends Fragment {

    private ChoosePointsViewModel choosePointsViewModel;
    private VisitViewModel visitViewModel;
    private TextView name;

    private FragmentListener listener;
    public interface FragmentListener {
        void getPageName(String name);
    }

    public static OperationsOnOutletFragment getInstance(String role, String outletCode) {
        OperationsOnOutletFragment fragment = new OperationsOnOutletFragment();
        Bundle bundle = new Bundle();
        bundle.putString("USER_ROLE", role);
        bundle.putString("OUT_CODE", outletCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operations_on_outlet, container, false);

        listener.getPageName(getResources().getString(R.string.operations_on_outlet));
        name = view.findViewById(R.id.name);
        TextView detailInformation = view.findViewById(R.id.detailInformation);
        detailInformation.setText(Ius.makeTextUnderlined(detailInformation.getText().toString()));
        CardView createRequest = view.findViewById(R.id.createRequest);

        choosePointsViewModel = new ViewModelProvider(this).get(ChoosePointsViewModel.class);
        visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);

        if (getArguments()!=null) {
            getOutlet(getArguments().getString("OUT_CODE"));
        }

        createRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SessionActivity)requireActivity()).openActivityCreateRequest();
            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void getOutlet(String outletCode) {
        choosePointsViewModel.getSalePointByCode(outletCode).observe(getViewLifecycleOwner(), s -> {
            if (s!=null) {
                name.setText(s.getName());
                Visit visit = new Visit();
                visit.setNumber(Ius.generateUniqueCode(this.getContext(), "v"));
//                visit.setUserCode(Ius.readSharedPreferences(thi));
            }
        });
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