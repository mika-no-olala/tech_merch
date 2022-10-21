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
import kz.smrtx.techmerch.items.entities.SalePointItem;
import kz.smrtx.techmerch.items.entities.User;

public class CardAdapterString extends RecyclerView.Adapter<CardAdapterString.CardViewHolder> {

    private List<Element> elements;
    private List<User> users;
    private List<SalePointItem> salePointItems;
    private String listType;
    private onItemClickListener listener;

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public View line;

        public CardViewHolder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
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

    public CardAdapterString() {
    }

    public void setAdapterElement(List<Element> elements) {
        this.elements = elements;
        listType = "e";
        notifyDataSetChanged();
    }

    public void setAdapterUser(List<User> users) {
        this.users = users;
        listType = "u";
        notifyDataSetChanged();
    }

    public void setAdapterAddress(List<SalePointItem> salePointItems) {
        this.salePointItems = salePointItems;
        listType = "a";
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
        switch (listType) {
            case "a":
                holder.text.setText(
                        "[" + salePointItems.get(position).getId() + "]" + " \n" +
                                salePointItems.get(position).getName() + " - " + salePointItems.get(position).getStreet());
                break;

            case "e":
                String str = elements.get(position).getName();
                holder.text.setText(str);
                break;

            case "u":
                holder.text.setText(
                        "[" + users.get(position).getCode() + "] - " + users.get(position).getRoleName()  + "\n" +
                                users.get(position).getName());
                break;
        }

        if (getItemCount()==position)
            holder.line.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        if (listType.equals("a"))
            return salePointItems.size();
        else if (listType.equals("e"))
            return elements.size();
        else
            return users.size();
    }

}
