package kz.smrtx.techmerch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import kz.smrtx.techmerch.R;

public class CreateSalePointFragment extends Fragment {

    private FragmentListener listener;
    public interface FragmentListener {
        void getPageName(String name);
    }

    public static CreateSalePointFragment getInstance() {
        CreateSalePointFragment fragment = new CreateSalePointFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_sale_point, container, false);
        listener.getPageName(getResources().getString(R.string.new_sale_point));

        ImageView image = view.findViewById(R.id.image);
        TextView photoText = view.findViewById(R.id.photoText);
        View address = view.findViewById(R.id.address);
        TextView addressText = view.findViewById(R.id.addressText);
        EditText name = view.findViewById(R.id.name);
        EditText entity = view.findViewById(R.id.entity);
        EditText category = view.findViewById(R.id.category);
        EditText salesChannel = view.findViewById(R.id.salesChannel);
        EditText type = view.findViewById(R.id.type);
        EditText phone = view.findViewById(R.id.phone);
        EditText comment = view.findViewById(R.id.comment);
        Button start = view.findViewById(R.id.start);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (CreateSalePointFragment.FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement onFragmentListener");
        }
    }

}