package kz.smrtx.techmerch.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.items.entities.Photo;

public class CardAdapterImagePager extends PagerAdapter {

    private Context context;
    private List<String> imageList = new ArrayList<>();

    public void setAdapterWithString(Context context, List<String> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    public void setAdapterWithPhoto(Context context, List<Photo> photoList) {
        this.context = context;
        for (Photo p : photoList)
            imageList.add(p.getREP_PHOTO());
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }

    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        File file;

        if (imageList.get(position).contains("."))
            file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imageList.get(position));
        else
            file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imageList.get(position) + ".jpg");

        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
            container.addView(imageView, 0);
        }

        return imageView;
    }
}
