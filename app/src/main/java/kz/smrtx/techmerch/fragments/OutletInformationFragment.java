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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.SessionActivity;
import kz.smrtx.techmerch.items.viewmodels.ChoosePointsViewModel;
import kz.smrtx.techmerch.items.viewmodels.VisitViewModel;

public class OutletInformationFragment extends Fragment {

    private String outletCode = "";
    private TextView name;
    private TextView code;
    private TextView requests;
    private TextView tasks;
    private TextView entity;
    private TextView category;
    private TextView salesChannel;
    private TextView type;
    private TextView phone;
    private TextView comment;
    private TextView address;
    private TextView distance;
    private TextView showRoute;
    private ChoosePointsViewModel choosePointsViewModel;
    private VisitViewModel visitViewModel;
    private FragmentListener listener;
    public interface FragmentListener {
        void getPageName(String name);
    }

    public static OutletInformationFragment getInstance(String role, String outletCode) {
        OutletInformationFragment fragment = new OutletInformationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("USER_ROLE", role);
        bundle.putString("OUT_CODE", outletCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outlet_information, container, false);

        listener.getPageName(getResources().getString(R.string.outlet_information));
        initializeTextViews(view);
        showRoute.setText(Ius.makeTextUnderlined(showRoute.getText().toString()));
        Button startWork = view.findViewById(R.id.start);
        choosePointsViewModel = new ViewModelProvider(this).get(ChoosePointsViewModel.class);
        visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);

        if (getArguments()!=null) {
            outletCode = getArguments().getString("OUT_CODE");
            getOutlet(outletCode);
        }

        startWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SessionActivity)requireActivity()).openOperationsOnOutlet(outletCode);
            }
        });

        return view;
    }
    
    private void initializeTextViews(View view) {
        name = view.findViewById(R.id.name);
        code = view.findViewById(R.id.code);
        requests = view.findViewById(R.id.requests);
        tasks = view.findViewById(R.id.tasks);
        entity = view.findViewById(R.id.entity);
        category = view.findViewById(R.id.category);
        salesChannel = view.findViewById(R.id.salesChannel);
        type = view.findViewById(R.id.type);
        phone = view.findViewById(R.id.phone);
        comment = view.findViewById(R.id.comment);
        address = view.findViewById(R.id.address);
        distance = view.findViewById(R.id.distance);
        showRoute = view.findViewById(R.id.showRoute);
    }
    
    @SuppressLint("SetTextI18n")
    private void getOutlet(String outletCode) {
        choosePointsViewModel.getSalePointByCode(outletCode).observe(getViewLifecycleOwner(), s -> {
            if (s!=null) {
                name.setText(s.getName());
                code.setText(": " + s.getId());
                requests.setText(": " + "0");
                tasks.setText(": " + "0");
                entity.setText(": " + s.getOwner());
                category.setText(": " + getResources().getString(R.string.no_data));
                salesChannel.setText(": " + getResources().getString(R.string.no_data));
                type.setText(": " + getResources().getString(R.string.no_data));
                phone.setText(": " + s.getPhone());
                comment.setText(getResources().getString(R.string.no_data));
                address.setText(s.getHouse());
                distance.setText("0 " + getResources().getString(R.string.km_from_you));
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OutletInformationFragment.FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement onFragmentListener");
        }
    }
}