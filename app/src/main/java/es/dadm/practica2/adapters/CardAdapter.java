package es.dadm.practica2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.Ticket;
import es.dadm.practica2.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder>{
    private List<Ticket> mTicketList;
    private final Context mContext;

    public CardAdapter(List<Ticket> TicketList, Context context){
        this.mTicketList = TicketList;
        this.mContext = context;
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvDescription) TextView tvDescription;
        @BindView(R.id.tvPrice) TextView tvPrice;

        CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_tickets_card_mode, viewGroup, false);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Ticket Ticket = mTicketList.get(position);

        holder.tvTitle.setText(Ticket.getTitle());
        holder.tvDescription.setText(Ticket.getDescription());
        holder.tvPrice.setText(String.format(mContext.getResources().getString(R.string.TICKET_PRICE), Ticket.getPrice()));
    }

    @Override
    public int getItemCount() {
        return mTicketList.size();
    }

    public void setContent(List<Ticket> newTicketList){
        mTicketList = newTicketList;
    }
}