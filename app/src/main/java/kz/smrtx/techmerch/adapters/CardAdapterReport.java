package kz.smrtx.techmerch.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.items.entities.Consumable;
import kz.smrtx.techmerch.items.entities.Report;

public class CardAdapterReport extends RecyclerView.Adapter<CardAdapterReport.CardViewHolder> {

    private final Context context;
    private List<Report> reports;
    private onItemClickListener listener;

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView dateInterval;
        public TextView item;
        public TextView total;
        public TextView created;
        public View line;

        public CardViewHolder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);
            dateInterval = itemView.findViewById(R.id.dateInterval);
            item = itemView.findViewById(R.id.item);
            total = itemView.findViewById(R.id.total);
            created = itemView.findViewById(R.id.created);
            line = itemView.findViewById(R.id.line);

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

    public CardAdapterReport(List<Report> reports, Context context) {
        this.reports = reports;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAdapter(List<Report> reports) {
        if (reports!=null) {
            this.reports = reports;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_report, parent, false);
        return new CardViewHolder(v, listener);
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Report report = reports.get(position);

        holder.dateInterval.setText(report.getDateInterval());
        holder.item.setText(report.getItem());
        holder.total.setText(context.getString(R.string.total) + ": " + report.getTotal() + "тг");
        holder.created.setText(report.getDate());

        if (reports.size()-1 == position)
            holder.line.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

}
