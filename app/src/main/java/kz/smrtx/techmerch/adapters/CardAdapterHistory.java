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
import kz.smrtx.techmerch.items.entities.Request;

public class CardAdapterHistory extends RecyclerView.Adapter<CardAdapterHistory.CardViewHolder> {

    private final Context context;
    private List<History> histories;
    private onItemClickListener listener;

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView dateUpdated;
        public TextView status;
        public TextView fromUser;
        public TextView comment;
        public View line;
        public View card;

        public CardViewHolder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);
            dateUpdated = itemView.findViewById(R.id.dateUpdated);
            status = itemView.findViewById(R.id.status);
            fromUser = itemView.findViewById(R.id.fromUser);
            comment = itemView.findViewById(R.id.comment);
            line = itemView.findViewById(R.id.line);
            card = itemView.findViewById(R.id.card);

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

    public CardAdapterHistory(List<History> histories, Context context) {
        this.histories = histories;
        this.context = context;
    }

    public void setHistoryList(List<History> histories) {
        if (histories!=null) {
            this.histories = histories;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_request, parent, false);
        return new CardViewHolder(v, listener);
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        History history = histories.get(position);

        String date = Ius.getDateByFormat(
                Ius.getDateFromString(
                        history.getCreated(), "yyyy-MM-dd'T'HH:mm:ss.sss"
                ), "dd.MM.yyyy HH:mm");

        holder.dateUpdated.setText(date);
        holder.status.setText(context.getResources().getString(R.string.request_status) + ": " + history.getStatus());
        holder.fromUser.setText(context.getResources().getString(R.string.completing) + ": " + history.getUserNameAppointed());

//        if (history.getComment().length()==0)
            holder.comment.setVisibility(View.GONE);
//        else
//            holder.comment.setText("");

        if (history.getStatus().contains(context.getResources().getString(R.string.rejected)))
            holder.status.setTextColor(context.getResources().getColor(R.color.pink_antique));

        if (histories.size()-1 == position) {
            holder.line.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

}
