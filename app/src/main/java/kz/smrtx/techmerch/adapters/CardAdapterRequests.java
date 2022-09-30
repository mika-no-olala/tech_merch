package kz.smrtx.techmerch.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import kz.smrtx.techmerch.Ius;
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
        public TextView salePoint;
        public TextView status;
        public TextView fromUser;
        public TextView comment;
        public View line;

        public CardViewHolder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);
            dateUpdated = itemView.findViewById(R.id.dateUpdated);
            salePoint = itemView.findViewById(R.id.salePoint);
            status = itemView.findViewById(R.id.status);
            fromUser = itemView.findViewById(R.id.fromUser);
            comment = itemView.findViewById(R.id.comment);
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

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Request request = requests.get(position);

        String date = Ius.getDateByFormat(
                Ius.getDateFromString(
                        request.getREQ_UPDATED(), "yyyy-MM-dd'T'HH:mm:ss"
                ), "dd.MM.yyyy HH:mm");

        holder.dateUpdated.setText(date);
        holder.salePoint.setText(request.getREQ_SAL_NAME());
        holder.status.setText(context.getResources().getString(R.string.request_status) + ": " + request.getREQ_STATUS());

        if (request.getREQ_USE_PHONE()!=null)
            holder.fromUser.setText(context.getResources().getString(R.string.from) + ": " + request.getREQ_USE_NAME()
            + " - " + request.getREQ_USE_PHONE());
        else
            holder.fromUser.setText(context.getResources().getString(R.string.from) + ": " + request.getREQ_USE_NAME());

        if (request.getREQ_COMMENT().length()==0)
            holder.comment.setVisibility(View.GONE);
        else
            holder.comment.setText(request.getREQ_COMMENT());

        if (request.getREQ_STATUS().contains(context.getResources().getString(R.string.rejected)))
            holder.status.setTextColor(context.getResources().getColor(R.color.pink_antique));

        if (requests.size()-1 == position)
            holder.line.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

}
