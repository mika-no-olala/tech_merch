package kz.smrtx.techmerch.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.items.entities.Outlet;

public class CardAdapterOutlets extends RecyclerView.Adapter<CardAdapterOutlets.CardViewHolder> {

    private ArrayList<Outlet> outlets;
    private onItemClickListener listener;

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView code;
        public TextView name;
        public TextView address;
        public TextView requests;
        public TextView distance;

        public CardViewHolder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);
            code = itemView.findViewById(R.id.code);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            requests = itemView.findViewById(R.id.requests);
            distance = itemView.findViewById(R.id.distance);

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

    public CardAdapterOutlets(ArrayList<Outlet> outlets) {
        this.outlets = outlets;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_outlet, parent, false);
        return new CardViewHolder(v, listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Outlet outlet = outlets.get(position);
        
        holder.code.setText(outlet.getOUT_CODE());
        holder.name.setText(outlet.getOUT_NAME());
        holder.address.setText(holder.address.getText().toString() + ": " + outlet.getOUT_ADDRESS());
        holder.requests.setText(holder.requests.getText().toString() + ": " + outlet.getOUT_REQUEST_NUMBER());
        holder.distance.setText(outlet.getOUT_DISTANCE() + " км");
        
    }

    @Override
    public int getItemCount() {
        return outlets.size();
    }

}
