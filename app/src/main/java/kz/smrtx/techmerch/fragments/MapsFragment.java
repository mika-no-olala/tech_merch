package kz.smrtx.techmerch.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import kz.smrtx.techmerch.R;

public class MapsFragment extends Fragment {

    private double lon = 0;
    private double lat = 0;
    private String markName;

    public static MapsFragment getInstance(double lon, double lat, String name) {
        MapsFragment fragment = new MapsFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble("LON", lon);
        bundle.putDouble("LAT", lat);
        bundle.putString("SALE_POINT_NAME", name);
        fragment.setArguments(bundle);
        return fragment;
    }

    private MapsFragment.FragmentListener listener;
    public interface FragmentListener {
        void getPageName(String name);
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng salePoint = new LatLng(lat, lon);
            googleMap.addMarker(new MarkerOptions().position(salePoint).title(markName));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(salePoint, 14f));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        listener.getPageName(getResources().getString(R.string.sale_point_on_map));

        if (getArguments()!=null) {
            lon = getArguments().getDouble("LON");
            lat = getArguments().getDouble("LAT");
            markName = getArguments().getString("SALE_POINT_NAME");
        }

        return inflater.inflate(R.layout.fragment_maps, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (MapsFragment.FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement onFragmentListener");
        }
    }
}