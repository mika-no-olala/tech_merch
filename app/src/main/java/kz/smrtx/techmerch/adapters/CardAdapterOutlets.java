package kz.smrtx.techmerch.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kz.smrtx.techmerch.GPSTracker;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.items.entities.Outlet;
import kz.smrtx.techmerch.items.entities.SalePointItem;
import kz.smrtx.techmerch.items.viewmodels.RequestViewModel;

public class CardAdapterOutlets extends RecyclerView.Adapter<CardAdapterOutlets.CardViewHolder> {

    private Context context;
    private double lat = 0;
    private double lon = 0;
    private List<SalePointItem> outlets;
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

    public CardAdapterOutlets(List<SalePointItem> outlets, Context context) {
        this.outlets = outlets;
        this.context = context;
    }

    public void setOutletList(List<SalePointItem> outlets) {
        if (outlets!=null) {
            this.outlets = outlets;
            notifyDataSetChanged();
        }
    }

    public void setCoordinates(double latitude, double longitude) {
        lat = latitude;
        lon = longitude;
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
        SalePointItem salePointItem = outlets.get(position);
        double distance = 0;
        if (lat!=0 && lon!=0) {
            if (salePointItem.getLatitude()!=null && salePointItem.getLongitude()!=null) {
                double latPoint = Double.parseDouble(salePointItem.getLatitude());
                double lonPoint = Double.parseDouble(salePointItem.getLongitude());

                double latKM = Math.abs((latPoint - lat)*111.32);
                double lonKM = Math.abs(lonPoint - lon) * 40075 * Math.cos(Math.abs(latPoint-lat))/360;
                distance = Math.pow(Math.pow(latKM, 2) + Math.pow(lonKM, 2), 0.5);
                distance = Math.floor(distance * 10) / 10;
            }
        }
        
        holder.code.setText(salePointItem.getId());
        holder.name.setText(salePointItem.getName());
        holder.address.setText(context.getResources().getString(R.string.address) + ": " + salePointItem.getHouse());
        holder.requests.setText(context.getResources().getString(R.string.requests) + ": " + 0);
        holder.distance.setText(distance + " км");
        
    }

    @Override
    public int getItemCount() {
        return outlets.size();
    }

}
