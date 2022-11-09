package kz.smrtx.techmerch.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.activities.CreateRequestActivity;
import kz.smrtx.techmerch.adapters.CardAdapterImagePager;
import kz.smrtx.techmerch.adapters.CardAdapterString;
import kz.smrtx.techmerch.items.entities.User;
import kz.smrtx.techmerch.items.viewmodels.UserViewModel;

public class RCEndingFragment extends Fragment {

    private View view;
    private UserViewModel userViewModel;
    private List<User> users = new ArrayList<>();
    private EditText executor;
    private int executorRole;

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
        executor = view.findViewById(R.id.executor);
        EditText comment = view.findViewById(R.id.comment);
        photo_1 = view.findViewById(R.id.photo_1);
        photo_2 = view.findViewById(R.id.photo_2);
        photo_3 = view.findViewById(R.id.photo_3);
        photo_4 = view.findViewById(R.id.photo_4);
        photo_5 = view.findViewById(R.id.photo_5);

        String city = Ius.readSharedPreferences(getContext(), Ius.USER_CITIES);
        if (city.contains("-")) { // tmr should not have more than one city, but just in case
            int indexOfLine = city.indexOf("-");
            city = city.substring(0, indexOfLine);
        }
        new WhatRoleToGet(userViewModel, Integer.parseInt(city), 6).execute();

        executor.setOnClickListener(executorView -> {
            if (!users.isEmpty()) {
                openDialogUsers();
                return;
            }

            createToast(getString(R.string.no_executor_error), false);
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
            createDialog(photoNumber);
            return;
        }

        captureImage(photoNumber);
    }

    private void getList(int locationCode, int roleCode) {
        userViewModel.getUserList(locationCode, roleCode).observe(this, u -> {
            users = u;
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

    private void createDialog(int photoNumber) {
        Dialog dialog = Ius.createDialog(this.getContext(), R.layout.dialog_window_image, "");
        ViewPager viewPager = dialog.findViewById(R.id.imagePager);
        CardAdapterImagePager adapter = new CardAdapterImagePager();

        adapter.setAdapterWithString(this.getContext(), filterPhotoNames());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(photoNumber - 1);

        dialog.show();
    }

    private void openDialogUsers() {
        CardAdapterString adapter = new CardAdapterString();
        adapter.setAdapterUser(users);
        Dialog dialog = Ius.createDialogList(this.getContext(), adapter);
        SearchView search = dialog.findViewById(R.id.search);

        search.setVisibility(View.GONE);

        dialog.show();

        adapter.setOnItemClickListener(position -> {
            String userInfo = users.get(position).getCode() + " - " + users.get(position).getName();
            executor.setText(userInfo);
            ((CreateRequestActivity) requireActivity()).setExecutor(userInfo, executorRole);
            dialog.cancel();
        });
    }

    private List<String> filterPhotoNames() {
        List<String> filtered = new ArrayList<>();
        for (String s : photoNames) {
            if (s!=null)
                filtered.add(s);
        }
        return filtered;
    }

    @SuppressLint("StaticFieldLeak")
    class WhatRoleToGet extends AsyncTask<Void, Void, Void> {
        UserViewModel userViewModel;
        int locationCode;
        int roleCode;

        public WhatRoleToGet(UserViewModel userViewModel, int locationCode, int roleCode) {
            this.userViewModel = userViewModel;
            this.locationCode = locationCode;
            this.roleCode = roleCode;
        }

        @Override
        protected void onPostExecute(Void unused) {
            executorRole = roleCode;
            Log.i("WhatRoleToGet", "process ended, getting list of role " + roleCode);
            getList(locationCode, roleCode);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("WhatRoleToGet", "search managers in " + locationCode);

            if (roleCode==6) {
                searchInManagers();
            }
            else if (roleCode==7) {
                searchInCoordinators();
            }

            return null;
        }

        private void searchInManagers() {
            if (userViewModel.getNumberOfUsers(locationCode, roleCode)==0) {
                Log.w("WhatRoleToGet", "0 managers");
                searchInCoordinators();
                roleCode = 7;
            }
        }

        private void searchInCoordinators() {
            if (userViewModel.getNumberOfUsers(locationCode, roleCode)==0) {
                Log.w("WhatRoleToGet", "0 coordinators");
                roleCode = 4;
            }
        }
    }

    private void createToast(String text, boolean success) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, (ViewGroup) view.findViewById(R.id.toast));
        Ius.showToast(layout, this.getContext(), text, success);
    }
}