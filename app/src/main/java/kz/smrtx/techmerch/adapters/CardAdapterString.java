package kz.smrtx.techmerch.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.items.entities.Element;

public class CardAdapterString extends RecyclerView.Adapter<CardAdapterString.CardViewHolder> {

    private List<Element> elements;
    private onItemClickListener listener;

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView text;

        public CardViewHolder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);
            text = itemView.findViewById(R.id.text);

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

    public CardAdapterString(List<Element> elements) {
        this.elements = elements;
    }

    public void setAdapter(List<Element> elements) {
        this.elements = elements;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_text, parent, false);
        return new CardViewHolder(v, listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        String str = elements.get(position).getName();
        
        holder.text.setText(str);
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

}
