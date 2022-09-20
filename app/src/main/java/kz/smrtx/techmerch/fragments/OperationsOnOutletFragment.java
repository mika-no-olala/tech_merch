package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;

import kz.smrtx.techmerch.BuildConfig;
import kz.smrtx.techmerch.utils.GPSTracker;
import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.SessionActivity;
import kz.smrtx.techmerch.items.entities.Visit;
import kz.smrtx.techmerch.items.viewmodels.ChoosePointsViewModel;
import kz.smrtx.techmerch.items.viewmodels.VisitViewModel;

public class OperationsOnOutletFragment extends Fragment {

    private ChoosePointsViewModel choosePointsViewModel;
    private VisitViewModel visitViewModel;
    private Visit visit;
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
            Log.i("getOutlet", "by code " + outletCode);
            if (s!=null) {
                GPSTracker gps = new GPSTracker(this.getContext());
                String lat = "0";
                String lon = "0";
                if (gps.getIsGPSTrackingEnabled()) {
                    lat = String.valueOf(gps.getLatitude());
                    lon = String.valueOf(gps.getLongitude());
                }

                name.setText(s.getName());
                visit = new Visit();
                String started = Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss");
                String number = Ius.generateUniqueCode(this.getContext(), "v");
                Ius.writeSharedPreferences(getContext(), Ius.LAST_VISIT_NUMBER, number);

                visit.setNumber(number);
                visit.setUserCode(Integer.parseInt(Ius.readSharedPreferences(this.getContext(), Ius.USER_CODE)));
                visit.setSaleCode(Integer.parseInt(outletCode));
                visit.setSaleId(s.getId());
                visit.setStart(started);
                visit.setCreated(started);
                visit.setDeviceId(Ius.readSharedPreferences(this.getContext(), Ius.DEVICE_ID));
                visit.setLatitude(lat);
                visit.setLongitude(lon);
                visit.setAppVersion(BuildConfig.VERSION_NAME);
                visit.setSessionCode(Ius.readSharedPreferences(this.getContext(), Ius.LAST_SESSION_CODE));

                visitViewModel.insert(visit);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        visit.setFinish(Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss"));
        visitViewModel.update(visit);
    }
}