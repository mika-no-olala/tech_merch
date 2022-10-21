package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import kz.smrtx.techmerch.items.entities.SalePointItem;
import kz.smrtx.techmerch.items.viewmodels.HistoryViewModel;
import kz.smrtx.techmerch.items.viewmodels.NoteViewModel;
import kz.smrtx.techmerch.utils.GPSTracker;
import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.SessionActivity;
import kz.smrtx.techmerch.items.viewmodels.ChoosePointsViewModel;
import kz.smrtx.techmerch.items.viewmodels.VisitViewModel;

public class OutletInformationFragment extends Fragment {

    private String outletCode = "";
    private ImageView image;
    private TextView name;
    private TextView code;
    private TextView requests;
    private TextView notes;
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
    private NoteViewModel noteViewModel;
    private HistoryViewModel historyViewModel;

    private SalePointItem salePoint;

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
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        if (getArguments()!=null) {
            outletCode = getArguments().getString("OUT_CODE");
            getOutlet(outletCode);
        }

        startWork.setOnClickListener(startWorkView -> ((SessionActivity)requireActivity()).openFragment(OperationsOnOutletFragment.getInstance("tmr", outletCode), true));

        showRoute.setOnClickListener(route -> {
            ((SessionActivity)requireActivity()).openFragment(MapsFragment.getInstance(
                    Double.parseDouble(salePoint.getLongitude()), Double.parseDouble(salePoint.getLatitude()), salePoint.getName()), false);
        });

        return view;
    }
    
    private void initializeTextViews(View view) {
        image = view.findViewById(R.id.image);
        name = view.findViewById(R.id.name);
        code = view.findViewById(R.id.code);
        requests = view.findViewById(R.id.requests);
        notes = view.findViewById(R.id.notes);
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
                salePoint = s;
                new GetDataNotes(this.getActivity(), noteViewModel, historyViewModel).execute();

                name.setText(s.getName());
                code.setText(": " + s.getId());
                entity.setText(": " + s.getLegalEntity());
                category.setText(": " + s.getCategory());
                salesChannel.setText(": " + s.getChannel());
                type.setText(": " + s.getType());
                phone.setText(": " + s.getContact());
                comment.setText(": " + s.getNote());
                address.setText(s.getStreet());
                Ius.writeSharedPreferences(this.getContext(), Ius.LAST_SALE_POINT_ADDRESS, s.getStreet());
                if (s.getLongitude()==null || s.getLatitude()==null)
                    distance.setText("? " + getResources().getString(R.string.km_from_you));
                else
                    distance.setText(getDistance(Double.parseDouble(s.getLatitude()),
                        Double.parseDouble(s.getLongitude())) + " " + getResources().getString(R.string.km_from_you));
            }
        });
    }

    private double getDistance(double latPoint, double lonPoint) {
        GPSTracker gps = new GPSTracker(this.getContext());
        double lat = 0;
        double lon = 0;
        double distance = 0;
        if (gps.getIsGPSTrackingEnabled()) {
            lat = gps.getLatitude();
            lon = gps.getLongitude();
        }
        if (lat!=0 && lon!=0) {
            double latKM = Math.abs((latPoint - lat)*111.32);
            double lonKM = Math.abs(lonPoint - lon) * 40075 * Math.cos(Math.abs(latPoint-lat))/360;
            distance = Math.pow(Math.pow(latKM, 2) + Math.pow(lonKM, 2), 0.5);
            distance = Math.floor(distance * 10) / 10;
        }
        return distance;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class GetDataNotes extends AsyncTask<Void, Void, Void> {
        NoteViewModel noteViewModel;
        HistoryViewModel historyViewModel;
        Activity context;
        int quantityNotes = 0;
        int quantityRequests = 0;
        Bitmap bitmap = null;

        public GetDataNotes(Activity context, NoteViewModel noteViewModel, HistoryViewModel historyViewModel) {
            this.context = context;
            this.noteViewModel = noteViewModel;
            this.historyViewModel = historyViewModel;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void unused) {
            if (bitmap!=null) {
                image.setImageBitmap(bitmap);
                image.setPadding(0, 0, 0, 0);
            }
            requests.setText(": " + quantityRequests);
            notes.setText(": " + quantityNotes);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            setImage();
            quantityNotes = noteViewModel.getNumberFromSalePoint(Integer.parseInt(salePoint.getCode()));
            quantityRequests = historyViewModel.getRequestsNumberOnSalePointByUser(
                    Integer.parseInt(Ius.readSharedPreferences(context, Ius.USER_CODE)), Integer.parseInt(salePoint.getCode()));
            return null;
        }

        private void setImage() {
            try {
                URL url = new URL(Ius.SP_PHOTO_URL + salePoint.getId() + ".jpg");
                try {
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    Log.w("setImage", "no image for sale point");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("setImage", "can't do url");
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OutletInformationFragment.FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement onFragmentListener");
        }
    }
}