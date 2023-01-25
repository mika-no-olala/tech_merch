package kz.smrtx.techmerch.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.items.entities.Note;
import kz.smrtx.techmerch.items.entities.WarehouseJournal;

public class CardAdapterJournal extends RecyclerView.Adapter<CardAdapterJournal.CardViewHolder> {

    private final Context context;
    private List<WarehouseJournal> journal;
    private onItemClickListener listener;
    private onAcceptClickListener acceptListener;

    public interface onItemClickListener {
        void onItemClick(int position);
    }
    public interface onAcceptClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }
    public void setOnAcceptCLickListener(onAcceptClickListener acceptListener) {
        this.acceptListener = acceptListener;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView content;
        public View line;
        private Button accept;

        public CardViewHolder(@NonNull View itemView, onItemClickListener listener, onAcceptClickListener acceptListener) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            line = itemView.findViewById(R.id.line);
            accept = itemView.findViewById(R.id.positive);

            itemView.setOnClickListener(view -> {
                if(listener != null) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

            accept.setOnClickListener(view -> {
                if(acceptListener != null) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        acceptListener.onItemClick(position);
                    }
                }
            });
        }
    }

    public CardAdapterJournal(List<WarehouseJournal> journal, Context context) {
        this.journal = journal;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshAdapter(List<WarehouseJournal> journal) {
        this.journal = journal;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_journal, parent, false);
        return new CardViewHolder(v, listener, acceptListener);
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        WarehouseJournal journalNote = journal.get(position);

        String date = Ius.getDateByFormat(
                Ius.getDateFromString(
                        journalNote.getDate(), "yyyy-MM-dd'T'HH:mm:ss"
                ), "dd MMMM");

        holder.title.setText(context.getString(R.string.supply_title_1) + " "
                + journalNote.getWarehouseNameSource() + ", " + date + " " + context.getString(R.string.on) + " "
                + journalNote.getWarehouseName() + ". " + context.getString(R.string.supply_title_3));

        holder.content.setText(journalNote.getContent());

        if (journal.size()-1 == position) {
            holder.line.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return journal.size();
    }

}
