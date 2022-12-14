package kz.smrtx.techmerch.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.items.entities.SalePointItem;
import kz.smrtx.techmerch.items.entities.Warehouse;

public class CardAdapterWarehouses extends RecyclerView.Adapter<CardAdapterWarehouses.CardViewHolder> {

    private Context context;
    private List<Warehouse> warehouseList;
    private onItemClickListener listener;

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView equipment;
        public TextView quantity;
        public TextView address;
        public TextView warehouse;

        public CardViewHolder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);
            equipment = itemView.findViewById(R.id.equipment);
            quantity = itemView.findViewById(R.id.quantity);
            warehouse = itemView.findViewById(R.id.warehouse);
            address = itemView.findViewById(R.id.address);

            itemView.setOnClickListener(view -> {
                if(listener != null) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public CardAdapterWarehouses(List<Warehouse> warehouseList, Context context) {
        this.warehouseList = warehouseList;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setWarehouseList(List<Warehouse> warehouseList) {
        if (warehouseList!=null) {
            this.warehouseList = warehouseList;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_warehouse, parent, false);
        return new CardViewHolder(v, listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Warehouse warehouse = warehouseList.get(position);
        
        holder.equipment.setText(warehouse.getWAC_CONTENT_NAME());
        holder.quantity.setText("x" + warehouse.getWAC_QUANTITY());
        holder.warehouse.setText(warehouse.getWAC_WAR_NAME());
        holder.address.setText(warehouse.getWAC_LOC_NAME() + ", " + warehouse.getWAC_WAR_ADDRESS());
    }

    @Override
    public int getItemCount() {
        return warehouseList.size();
    }

}
