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

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.items.entities.History;
import kz.smrtx.techmerch.items.entities.Note;

public class CardAdapterNotes extends RecyclerView.Adapter<CardAdapterNotes.CardViewHolder> {

    private final Context context;
    private List<Note> notes;
    private onItemClickListener listener;

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView created;
        public TextView author;
        public TextView note;
        public View line;

        public CardViewHolder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);
            created = itemView.findViewById(R.id.created);
            author = itemView.findViewById(R.id.author);
            note = itemView.findViewById(R.id.note);
            line = itemView.findViewById(R.id.line);

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

    public CardAdapterNotes(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }


    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_note, parent, false);
        return new CardViewHolder(v, listener);
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Note note = notes.get(position);

        String date = Ius.getDateByFormat(
                Ius.getDateFromString(
                        note.getNOT_CREATED(), "yyyy-MM-dd'T'HH:mm:ss"
                ), "dd.MM.yyyy HH:mm");

        holder.author.setText(note.getNOT_USE_NAME());
        holder.created.setText(date);
        holder.note.setText(note.getNOT_TEXT());

        if (notes.size()-1 == position) {
            holder.line.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

}
