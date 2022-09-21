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
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.entities.SalePointItem;

public class CardAdapterRequests extends RecyclerView.Adapter<CardAdapterRequests.CardViewHolder> {

    private final Context context;
    private List<Request> requests;
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

        public CardViewHolder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);
            dateUpdated = itemView.findViewById(R.id.dateUpdated);
            status = itemView.findViewById(R.id.status);
            fromUser = itemView.findViewById(R.id.fromUser);
            comment = itemView.findViewById(R.id.comment);

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

    public CardAdapterRequests(List<Request> requests, Context context) {
        this.requests = requests;
        this.context = context;
    }

    public void setOutletList(List<Request> requests) {
        if (requests!=null) {
            this.requests = requests;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_request, parent, false);
        return new CardViewHolder(v, listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Request request = requests.get(position);
        
        holder.dateUpdated.setText(request.getREQ_UPDATED());
        holder.status.setText(context.getResources().getString(R.string.request_status) + ": " + request.getREQ_STATUS());
        holder.fromUser.setText(context.getResources().getString(R.string.from) + ": " + request.getREQ_USE_NAME());
        holder.comment.setText(request.getREQ_COMMENT());
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

}
