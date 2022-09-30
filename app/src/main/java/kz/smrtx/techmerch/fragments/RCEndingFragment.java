package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.CreateRequestActivity;
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.entities.User;
import kz.smrtx.techmerch.items.viewmodels.UserViewModel;

public class RCEndingFragment extends Fragment {

    private View view;
    private UserViewModel userViewModel;
    private ArrayList<String> users = new ArrayList<>();

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
    private String photoName_1 = "";
    private String photoName_2 = "";
    private String photoName_3 = "";
    private String photoName_4 = "";
    private String photoName_5 = "";

    public static RCEndingFragment getInstance() {
        RCEndingFragment fragment = new RCEndingFragment();
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rc_ending, container, false);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        AutoCompleteTextView executor = view.findViewById(R.id.executor);
        EditText comment = view.findViewById(R.id.comment);
        photo_1 = view.findViewById(R.id.photo_1);
        photo_2 = view.findViewById(R.id.photo_2);
        photo_3 = view.findViewById(R.id.photo_3);
        photo_4 = view.findViewById(R.id.photo_4);
        photo_5 = view.findViewById(R.id.photo_5);

        getList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, users);
        executor.setAdapter(adapter);

        executor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                boolean correct = users.contains(editable.toString());
                ((CreateRequestActivity)requireActivity()).setExecutor(editable.toString().trim(), correct);
            }
        });

        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                ((CreateRequestActivity)requireActivity()).setComment(editable.toString().trim());
            }
        });

        photo_1.setOnClickListener(photo -> {
            captureImage(1);
        });
        photo_2.setOnClickListener(photo -> {
            captureImage(2);
        });
        photo_3.setOnClickListener(photo -> {
            captureImage(3);
        });
        photo_4.setOnClickListener(photo -> {
            captureImage(4);
        });
        photo_5.setOnClickListener(photo -> {
            captureImage(5);
        });

        return view;
    }

    private void getList() {
        userViewModel.getUserList(4).observe(getViewLifecycleOwner(), u -> {
            for (User user : u) {
                users.add(user.getCode() + " - " + user.getName());
            }
        });
    }

    private void captureImage(int photoNumber) {
        if (!((CreateRequestActivity)requireActivity()).checkPermissions()) {
            Log.w("captureImage", "NO PERMISSION");
            return;
        }

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(this.getActivity().getPackageManager())!=null) {
            File imageFile = null;

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        parseImage();
    }

    private File getImageFile(int photoNumber) {
        String fileName = Ius.generateUniqueCode(this.getContext(), "img");
        switch (photoNumber) {
            case 1:
                photoName_1 = fileName;
                currentImageView = photo_1;
                break;
            case 2:
                photoName_2 = fileName;
                currentImageView = photo_2;
                break;
            case 3:
                photoName_3 = fileName;
                currentImageView = photo_3;
                break;
            case 4:
                photoName_4 = fileName;
                currentImageView = photo_4;
                break;
            case 5:
                photoName_5 = fileName;
                currentImageView = photo_5;
                break;
        }

        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(fileName, "jpg", storageDir);
            currentImagePath = imageFile.getAbsolutePath();
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
        changeNumberOfActiveImg(1);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void changeNumberOfActiveImg(int changing) {
        numberOfActiveImg = numberOfActiveImg + changing;

        if (changing>0) {
            if (numberOfActiveImg==5)
                return;

            chooseImageByNumber(numberOfActiveImg);
            chosenCardView.setVisibility(View.VISIBLE);
        }
        else if (changing<0) {
            if (numberOfActiveImg==1)
                return;

            chooseImageByNumber(numberOfActiveImg+1);
            chosenCardView.setVisibility(View.INVISIBLE);

            chooseImageByNumber(numberOfActiveImg);
            chosenImageView.setPadding(22, 22, 22, 22);
            chosenImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo));
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

    private void createToast(String text, boolean success) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, (ViewGroup) view.findViewById(R.id.toast));
        Ius.showToast(layout, this.getContext(), text, success);
    }
}