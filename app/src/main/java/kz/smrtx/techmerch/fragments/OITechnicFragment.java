package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.OutletInformationActivity;
import kz.smrtx.techmerch.adapters.CardAdapterImages;
import kz.smrtx.techmerch.items.entities.Photo;
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.viewmodels.PhotoViewModel;
import kz.smrtx.techmerch.items.viewmodels.RequestViewModel;

public class OITechnicFragment extends Fragment {

    private List<Photo> photoListTMR = new ArrayList<>();
    private List<Photo> photoListTech = new ArrayList<>();
    private RecyclerView recyclerViewTMR;
    private RecyclerView recyclerViewTech;
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

    private View view;
    private Request request;
    private RequestViewModel requestViewModel;
    private PhotoViewModel photoViewModel;

//  <--- uploading images --->
    private int numberOfActiveImg = 1;
    private String currentImagePath = null;
    private ImageView currentImageView;
    private ImageView chosenImageView;
    private CardView chosenCardView;
    private ImageView photo_1;
    private ImageView photo_2;
    private ImageView photo_3;
    private ImageView photo_4;
    private ImageView photo_5;
    private final String[] photoNames = new String[5];

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
        view = inflater.inflate(R.layout.fragment_oi_technic, container, false);

        listener.getPageName(getResources().getString(R.string.request_completion));
        requestViewModel = new ViewModelProvider(this).get(RequestViewModel.class);
        photoViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);
        initializeSummaryStuff(view);
        EditText myComment = view.findViewById(R.id.technicComment);

        if (getArguments()!=null) {
            getRequest(getArguments().getString("REQ_CODE"));
        }

        negative.setOnClickListener(view -> {
            if (Ius.isEmpty(myComment)) {
                createToast(getResources().getString(R.string.fill_field), false);
                return;
            }
            ((OutletInformationActivity)requireActivity()).sendRequest(myComment.getText().toString(), photoNames, false);
        });

        send.setOnClickListener(view -> {
            if (Ius.isEmpty(myComment)) {
                createToast(getResources().getString(R.string.fill_field), false);
                return;
            }
            ((OutletInformationActivity)requireActivity()).sendRequest(myComment.getText().toString(), photoNames, true);
        });

        return view;
    }

    private void initializeSummaryStuff(View view) {
        recyclerViewTMR = view.findViewById(R.id.recyclerViewTMR);
        recyclerViewTech = view.findViewById(R.id.recyclerViewTech);
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

        photo_1 = view.findViewById(R.id.photo_1);
        photo_2 = view.findViewById(R.id.photo_2);
        photo_3 = view.findViewById(R.id.photo_3);
        photo_4 = view.findViewById(R.id.photo_4);
        photo_5 = view.findViewById(R.id.photo_5);
        photo_1.setOnClickListener(photo -> redirectClick(1));
        photo_2.setOnClickListener(photo -> redirectClick(2));
        photo_3.setOnClickListener(photo -> redirectClick(3));
        photo_4.setOnClickListener(photo -> redirectClick(4));
        photo_5.setOnClickListener(photo -> redirectClick(5));
    }

    private void getRequest(String requestCode) {
        requestViewModel.getRequestByCode(requestCode).observe(getViewLifecycleOwner(), r -> {
            if (r!=null) {
                request = r;
                setRequest();
            }
        });
        photoViewModel.getPhotosByTMR(requestCode).observe(getViewLifecycleOwner(), ph -> {
            if (ph!=null) {
                photoListTMR = ph;
                Ius.setAdapterImagesList(this.getContext(), recyclerViewTMR, photoListTMR);
            }
        });
        photoViewModel.getPhotosByTech(requestCode).observe(getViewLifecycleOwner(), ph -> {
            if (ph!=null) {
                photoListTech = ph;
                Ius.setAdapterImagesList(this.getContext(), recyclerViewTech, photoListTech);
            }
        });
    }

    private void createDialog(ImageView imageClicked) {
        Dialog dialog = Ius.createDialog(this.getContext(), R.layout.dialog_window_image, "");
        ImageView image = dialog.findViewById(R.id.image);

        image.setImageDrawable(imageClicked.getDrawable());

        dialog.show();
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

        if (isNull(request.getREQ_SECONDARY_ADDRESS()))
            addressSummary.setVisibility(View.GONE);
        else {
            addressSummary.setText(Ius.makeTextBold(this.getContext(), getResources().getString(R.string.address) + ": " + request.getREQ_SECONDARY_ADDRESS()));
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

    private void redirectClick(int photoNumber) {
        if (photoListTech.size() + photoNumber > 5) {
            createToast(getString(R.string.photo_limit_error), false);
            return;
        }
        chooseImageByNumber(photoNumber);
        String tag = String.valueOf(chosenImageView.getTag());
        if(tag.equals("changed")) {
            createDialog(chosenImageView);
            return;
        }

        captureImage(photoNumber);
    }

    @SuppressWarnings("deprecation")
    private void captureImage(int photoNumber) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(this.requireActivity().getPackageManager())!=null) {
            File imageFile;

            imageFile = getImageFile(photoNumber);
            if (imageFile==null) {
                createToast(getResources().getString(R.string.error), false);
                return;
            }

            Uri uri = FileProvider.getUriForFile(this.requireContext(), "com.example.android.fileprovider", imageFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            startActivityForResult(cameraIntent, 121);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==0) {
            File file = new File(currentImagePath);
            if (file.delete())
                Log.i("onActivityResult", "camera cancelled, image deleted");

            return;
        }
        parseImage();
    }

    private File getImageFile(int photoNumber) {
        String userCode = Ius.readSharedPreferences(this.getContext(), Ius.USER_CODE);

        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile("u"+userCode+"r", ".jpg", storageDir);
            currentImagePath = imageFile.getAbsolutePath();

            int indexOfLastSlash = currentImagePath.lastIndexOf('/') + 1;
            int indexOfDot = currentImagePath.lastIndexOf('.');
            String fileName = currentImagePath.substring(indexOfLastSlash, indexOfDot);

            switch (photoNumber) {
                case 1:
                    photoNames[0] = fileName;
                    currentImageView = photo_1;
                    break;
                case 2:
                    photoNames[1] = fileName;
                    currentImageView = photo_2;
                    break;
                case 3:
                    photoNames[2] = fileName;
                    currentImageView = photo_3;
                    break;
                case 4:
                    photoNames[3] = fileName;
                    currentImageView = photo_4;
                    break;
                case 5:
                    photoNames[4] = fileName;
                    currentImageView = photo_5;
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
            createToast(getResources().getString(R.string.error), false);
        }

        return imageFile;
    }

    private void parseImage() {
        Bitmap bitmap = BitmapFactory.decodeFile(currentImagePath);
        currentImageView.setImageBitmap(bitmap);
        currentImageView.setPadding(0, 0, 0, 0);

        String tag = String.valueOf(currentImageView.getTag());
        if (!tag.equals("changed")) {
            currentImageView.setTag("changed");
            changeNumberOfActiveImg(1);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void changeNumberOfActiveImg(int changing) {
        if ((numberOfActiveImg==5 && changing>0) || (numberOfActiveImg==1 && changing<0))
            return;

        numberOfActiveImg = numberOfActiveImg + changing;

        Log.i("ActiveImg", "number of active images: " + numberOfActiveImg);

        if (changing>0) {
            chooseImageByNumber(numberOfActiveImg);
            chosenCardView.setVisibility(View.VISIBLE);
        }
        else if (changing<0) {
            chooseImageByNumber(numberOfActiveImg+1);
            chosenCardView.setVisibility(View.INVISIBLE);

            chooseImageByNumber(numberOfActiveImg);
            chosenImageView.setPadding(22, 22, 22, 22);
            chosenImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo));
            chosenImageView.setTag("");
        }
    }

    private void chooseImageByNumber(int imgNumber) {
        switch (imgNumber) {
            case 1:
                chosenImageView = photo_1;
                chosenCardView = view.findViewById(R.id.card_1);
                break;
            case 2:
                chosenImageView = photo_2;
                chosenCardView = view.findViewById(R.id.card_2);
                break;
            case 3:
                chosenImageView = photo_3;
                chosenCardView = view.findViewById(R.id.card_3);
                break;
            case 4:
                chosenImageView = photo_4;
                chosenCardView = view.findViewById(R.id.card_4);
                break;
            case 5:
                chosenImageView = photo_5;
                chosenCardView = view.findViewById(R.id.card_5);
                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OITechnicFragment.FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement onFragmentListener");
        }
    }

    private void createToast(String text, boolean success) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, view.findViewById(R.id.toast));
        Ius.showToast(layout, this.getContext(), text, success);
    }
}