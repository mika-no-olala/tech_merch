package kz.smrtx.techmerch.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.items.entities.Photo;
import kz.smrtx.techmerch.items.entities.Request;

public class CardAdapterImages extends RecyclerView.Adapter<CardAdapterImages.CardViewHolder> {

    private final Context context;
    private List<Photo> photos;
    private onItemClickListener listener;

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public ImageView photo;

        public CardViewHolder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public CardAdapterImages(List<Photo> photos, Context context) {
        this.photos = photos;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_image, parent, false);
        return new CardViewHolder(v, listener);
    }

    @SuppressLint({"UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Photo photo = photos.get(position);
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + photo.getREP_PHOTO());

        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            holder.photo.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

}
