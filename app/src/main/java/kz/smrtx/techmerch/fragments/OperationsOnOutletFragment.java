package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import org.w3c.dom.Text;

import java.util.Date;

import kz.smrtx.techmerch.BuildConfig;
import kz.smrtx.techmerch.activities.OutletInformationActivity;
import kz.smrtx.techmerch.activities.StatusesActivity;
import kz.smrtx.techmerch.items.viewmodels.HistoryViewModel;
import kz.smrtx.techmerch.items.viewmodels.NoteViewModel;
import kz.smrtx.techmerch.items.viewmodels.UserViewModel;
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
    private NoteViewModel noteViewModel;
    private HistoryViewModel historyViewModel;
    private Visit visit;
    private TextView name;
    private TextView requestsTitle;
    private TextView notesTitle;
    private String salePointCode;
    private Date dateStarted;

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
        requestsTitle = view.findViewById(R.id.requestsTitle);
        notesTitle = view.findViewById(R.id.notesTitle);
        TextView detailInformation = view.findViewById(R.id.detailInformation);
        detailInformation.setText(Ius.makeTextUnderlined(detailInformation.getText().toString()));
        CardView createRequest = view.findViewById(R.id.createRequest);
        CardView requests = view.findViewById(R.id.requests);
        CardView notes = view.findViewById(R.id.notes);

        choosePointsViewModel = new ViewModelProvider(this).get(ChoosePointsViewModel.class);
        visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        if (getArguments()!=null) {
            salePointCode = getArguments().getString("OUT_CODE");
            new GetDataNotes(this.getActivity(), noteViewModel,historyViewModel).execute();
            getOutlet(salePointCode);
        }

        createRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SessionActivity)requireActivity()).openActivityCreateRequest();
            }
        });

        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityStatuses();
            }
        });

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SessionActivity)requireActivity()).openFragment(NotesFragment.getInstance(salePointCode), false);
            }
        });

        detailInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityOutletInformation();
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

                dateStarted = new Date();
                String started = Ius.getDateByFormat(dateStarted, "dd.MM.yyyy HH:mm:ss");
                String number = Ius.generateUniqueCode(this.getContext(), "v");
                Ius.writeSharedPreferences(getContext(), Ius.LAST_VISIT_NUMBER, number);

                visit.setVIS_NUMBER(number);
                visit.setVIS_USE_CODE(Integer.parseInt(Ius.readSharedPreferences(this.getContext(), Ius.USER_CODE)));
                visit.setVIS_SAL_CODE(Integer.parseInt(outletCode));
                visit.setVIS_SAL_ID(s.getId());
                visit.setVIS_START_DATE(started);
                visit.setVIS_CREATED(started);
                visit.setVIS_DEVICE_ID(Ius.readSharedPreferences(this.getContext(), Ius.DEVICE_ID));
                visit.setVIS_LATITUDE(lat);
                visit.setVIS_LONGITUDE(lon);
                visit.setVIS_SES_CODE(Ius.readSharedPreferences(this.getContext(), Ius.LAST_SESSION_CODE));

                visitViewModel.insert(visit);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class GetDataNotes extends AsyncTask<Void, Void, Void> {
        NoteViewModel noteViewModel;
        HistoryViewModel historyViewModel;
        Activity context;
        int quantityNotes = 0;
        int quantityRequests = 0;

        public GetDataNotes(Activity context, NoteViewModel noteViewModel, HistoryViewModel historyViewModel) {
            this.context = context;
            this.noteViewModel = noteViewModel;
            this.historyViewModel = historyViewModel;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void unused) {
            notesTitle.setText(getResources().getString(R.string.notes) + " (" + quantityNotes + ")");
            requestsTitle.setText(getResources().getString(R.string.requests) + " (" + quantityRequests + ")");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            quantityNotes = noteViewModel.getNumberFromSalePoint(Integer.parseInt(salePointCode));
            quantityRequests = historyViewModel.getRequestsNumberOnSalePointByUser(
                    Integer.parseInt(Ius.readSharedPreferences(context, Ius.USER_CODE)), Integer.parseInt(salePointCode));
            return null;
        }
    }

    private void openActivityOutletInformation() {
        Intent intent = new Intent(this.getContext(), OutletInformationActivity.class);
        intent.putExtra("scenario", "detail");
        intent.putExtra("salePointCode", salePointCode);
        startActivity(intent);
    }

    private void openActivityStatuses() {
        Intent intent = new Intent(this.getContext(), StatusesActivity.class);
        intent.putExtra("salePointCode", salePointCode);
        startActivity(intent);
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
    public void onResume() {
        super.onResume();
        if (getArguments()!=null) {
            new GetDataNotes(this.getActivity(), noteViewModel,historyViewModel).execute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //  sss CHECK NOTES, REQUESTS AND HISTORY. IF THERE IS NO, THEN DELETE

//        Date dateFinished = new Date();
//        long minutes = Ius.getDifferenceBetweenDates(dateStarted, dateFinished, "m");
//        if (minutes>=5) {
            visit.setVIS_FINISH_DATE(Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss"));
            visitViewModel.update(visit);
//        }
//        else {
//            visitViewModel.delete(visit);
//        }
    }
}