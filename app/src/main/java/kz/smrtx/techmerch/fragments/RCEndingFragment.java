package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.Button;
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
import java.nio.file.Files;
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
    private String[] photoNames = new String[5];

    public static RCEndingFragment getInstance() {
        return new RCEndingFragment();
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

        photo_1.setOnClickListener(photo -> redirectClick(1));
        photo_2.setOnClickListener(photo -> redirectClick(2));
        photo_3.setOnClickListener(photo -> redirectClick(3));
        photo_4.setOnClickListener(photo -> redirectClick(4));
        photo_5.setOnClickListener(photo -> redirectClick(5));

        return view;
    }

    private void redirectClick(int photoNumber) {
        chooseImageByNumber(photoNumber);
        String tag = String.valueOf(chosenImageView.getTag());
        if(tag.equals("changed")) {
            createDialog(chosenImageView);
            return;
        }

        captureImage(photoNumber);
    }

    private void getList() {
        userViewModel.getUserList(4).observe(getViewLifecycleOwner(), u -> {
            for (User user : u) {
                users.add(user.getCode() + " - " + user.getName());
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void captureImage(int photoNumber) {
        if (!((CreateRequestActivity)requireActivity()).checkPermissions()) {
            Log.w("captureImage", "NO PERMISSION");
            return;
        }

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(this.getActivity().getPackageManager())!=null) {
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
        ((CreateRequestActivity)requireActivity()).setPhotos(photoNames);

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

    private void createDialog(ImageView imageClicked) {
        Dialog dialog = Ius.createDialog(this.getContext(), R.layout.dialog_window_image, "");
        ImageView image = dialog.findViewById(R.id.image);

        image.setImageDrawable(imageClicked.getDrawable());

        dialog.show();
    }

    private void createToast(String text, boolean success) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, (ViewGroup) view.findViewById(R.id.toast));
        Ius.showToast(layout, this.getContext(), text, success);
    }
}