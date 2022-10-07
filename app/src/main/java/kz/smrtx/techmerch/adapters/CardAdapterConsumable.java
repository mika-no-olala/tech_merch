package kz.smrtx.techmerch.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.items.entities.Consumable;
import kz.smrtx.techmerch.items.entities.Request;

public class CardAdapterConsumable extends RecyclerView.Adapter<CardAdapterConsumable.CardViewHolder> {

    private final Context context;
    private List<Consumable> consumables;
    private onItemClickListener listener;

    public interface onItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView info;
        public ImageView delete;

        public CardViewHolder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);
            info = itemView.findViewById(R.id.info);
            delete = itemView.findViewById(R.id.delete);

            itemView.setOnClickListener(view -> {
                if(listener != null) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

            delete.setOnClickListener(view -> {
                if(listener != null) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position);
                    }
                }
            });
        }
    }

    public CardAdapterConsumable(List<Consumable> consumables, Context context) {
        this.consumables = consumables;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAdapter(List<Consumable> consumables) {
        if (consumables!=null) {
            this.consumables = consumables;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_consumable, parent, false);
        return new CardViewHolder(v, listener);
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Consumable consumable = consumables.get(position);

        holder.info.setText(
                consumable.getTER_CONSUMABLE_NAME() + "\n" +
                        consumable.getTER_COST() + "тг x" + consumable.getTER_QUANTITY()
        );
    }

    @Override
    public int getItemCount() {
        return consumables.size();
    }

}
