package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.OutletInformationActivity;
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.viewmodels.ChoosePointsViewModel;
import kz.smrtx.techmerch.items.viewmodels.RequestViewModel;
import kz.smrtx.techmerch.utils.GPSTracker;

public class OITechnicFragment extends Fragment {

    private TextView codeSummary;
    private TextView createdSummary;
    private TextView deadlineSummary;
    private TextView typeSummary;
    private TextView equipmentTypeSummary;
    private TextView equipmentSubtypeSummary;
    private TextView workSummary;
    private TextView replaceSummary;
    private TextView additionalSummary;
    private TextView addressSummary;
    private TextView workSubtypeSummary;
    private TextView specialSummary;
    private TextView commentSummary;
    private Button send;
    private Button negative;

    private Request request;
    private RequestViewModel requestViewModel;

    private FragmentListener listener;
    public interface FragmentListener {
        void getPageName(String name);
    }

    public static OITechnicFragment getInstance(String requestCode) {
        OITechnicFragment fragment = new OITechnicFragment();
        Bundle bundle = new Bundle();
        bundle.putString("REQ_CODE", requestCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oi_technic, container, false);

        listener.getPageName(getResources().getString(R.string.request_completion));
        requestViewModel = new ViewModelProvider(this).get(RequestViewModel.class);
        initializeSummaryStuff(view);
        EditText myComment = view.findViewById(R.id.technicComment);

        if (getArguments()!=null) {
            getRequest(getArguments().getString("REQ_CODE"));
        }

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Ius.isEmpty(myComment)) {
                    createToast(view, getResources().getString(R.string.fill_field), false);
                    return;
                }
                ((OutletInformationActivity)requireActivity()).sendRequest(myComment.getText().toString(), false);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Ius.isEmpty(myComment)) {
                    createToast(view, getResources().getString(R.string.fill_field), false);
                    return;
                }
                ((OutletInformationActivity)requireActivity()).sendRequest(myComment.getText().toString(), true);
            }
        });

        return view;
    }

    private void initializeSummaryStuff(View view) {
        codeSummary = view.findViewById(R.id.code);
        createdSummary = view.findViewById(R.id.created);
        deadlineSummary = view.findViewById(R.id.deadline);
        typeSummary = view.findViewById(R.id.type);
        equipmentTypeSummary = view.findViewById(R.id.equipmentType);
        equipmentSubtypeSummary = view.findViewById(R.id.equipmentSubtype);
        workSummary = view.findViewById(R.id.work);
        replaceSummary = view.findViewById(R.id.replace);
        additionalSummary = view.findViewById(R.id.additional);
        addressSummary = view.findViewById(R.id.address);
        workSubtypeSummary = view.findViewById(R.id.workSubtype);
        specialSummary = view.findViewById(R.id.special);
        commentSummary = view.findViewById(R.id.comment);
        send = view.findViewById(R.id.send);
        negative = view.findViewById(R.id.negative);
    }

    private void getRequest(String requestCode) {
        requestViewModel.getRequestByCode(requestCode).observe(getViewLifecycleOwner(), r -> {
            if (r!=null) {
                request = r;
                setRequest();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setRequest() {
        String dateCreated = Ius.getDateByFormat(
                Ius.getDateFromString(
                        request.getREQ_CREATED(), "yyyy-MM-dd'T'HH:mm:ss"
                ), "dd.MM.yyyy, EEEE");
        String dateDeadline = Ius.getDateByFormat(
                Ius.getDateFromString(
                        request.getREQ_CREATED(), "yyyy-MM-dd'T'HH:mm:ss"
                ), "dd.MM.yyyy, EEEE");

        codeSummary.setText(request.getREQ_CODE());

        createdSummary.setText(Ius.makeTextBold(this.getContext(), getResources().getString(R.string.date_of_creation) + ": " +
                dateCreated));
        deadlineSummary.setText(Ius.makeTextBold(this.getContext(), getResources().getString(R.string.deadline) + ": " +
                dateDeadline));

        typeSummary.setText(Ius.makeTextBold(this.getContext(), getResources().getString(R.string.request_type) + ": " + request.getREQ_TYPE()));

        if (isNull(request.getREQ_EQUIPMENT())) {
            equipmentTypeSummary.setVisibility(View.GONE);
            equipmentSubtypeSummary.setVisibility(View.GONE);
        }
        else {
            equipmentTypeSummary.setText(Ius.makeTextBold(this.getContext(), getResources().getString(R.string.equipment_type) + ": " + request.getREQ_EQUIPMENT()));
            equipmentSubtypeSummary.setText(Ius.makeTextBold(this.getContext(), getResources().getString(R.string.equipment_subtype) + ": " + request.getREQ_EQU_SUBTYPE()));
            equipmentTypeSummary.setVisibility(View.VISIBLE);
            equipmentSubtypeSummary.setVisibility(View.VISIBLE);
        }

        workSummary.setText(Ius.makeTextBold(this.getContext(), getResources().getString(R.string.work_type) + ": " + request.getREQ_WORK()));

        if (isNull(request.getREQ_ADDITIONAL()))
            additionalSummary.setVisibility(View.GONE);
        else {
            additionalSummary.setText(Ius.makeTextBold(this.getContext(), getResources().getString(R.string.additional) + ": " + request.getREQ_ADDITIONAL()));
            additionalSummary.setVisibility(View.VISIBLE);
        }

        if (isNull(request.getREQ_REPLACE()))
            replaceSummary.setVisibility(View.GONE);
        else {
            replaceSummary.setText(Ius.makeTextBold(this.getContext(), getResources().getString(R.string.replace) + ": " + request.getREQ_REPLACE()));
            replaceSummary.setVisibility(View.VISIBLE);
        }

        if (isNull(request.getREQ_ADDRESS_SALEPOINT()))
            addressSummary.setVisibility(View.GONE);
        else {
            addressSummary.setText(Ius.makeTextBold(this.getContext(), getResources().getString(R.string.address) + ": " + request.getREQ_ADDRESS_SALEPOINT()));
            addressSummary.setVisibility(View.VISIBLE);
        }

        if (isNull(request.getREQ_WORK_SUBTYPE()))
            workSubtypeSummary.setVisibility(View.GONE);
        else {
            workSubtypeSummary.setText(Ius.makeTextBold(this.getContext(), getResources().getString(R.string.work_subtype) + ": " + request.getREQ_WORK_SUBTYPE()));
            workSubtypeSummary.setVisibility(View.VISIBLE);
        }

        if (isNull(request.getREQ_WORK_SPECIAL()))
            specialSummary.setVisibility(View.GONE);
        else {
            specialSummary.setText(Ius.makeTextBold(this.getContext(), getResources().getString(R.string.glo_equipment) + ": " + request.getREQ_WORK_SPECIAL()));
            specialSummary.setVisibility(View.VISIBLE);
        }

        if (isNull(request.getREQ_COMMENT()))
            commentSummary.setVisibility(View.GONE);
        else {
            commentSummary.setText(Ius.makeTextBold(this.getContext(), getResources().getString(R.string.comment) + ": " + request.getREQ_COMMENT()));
            commentSummary.setVisibility(View.VISIBLE);
        }
    }

    private boolean isNull(String text) {
        if (text==null)
            return true;
        return text.length() == 0;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OITechnicFragment.FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement onFragmentListener");
        }
    }

    private void createToast(View view, String text, boolean success) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, (ViewGroup) view.findViewById(R.id.toast));
        Ius.showToast(layout, this.getContext(), text, success);
    }
}