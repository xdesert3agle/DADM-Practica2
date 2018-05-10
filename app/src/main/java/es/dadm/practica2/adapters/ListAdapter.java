package es.dadm.practica2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.Ticket;
import es.dadm.practica2.R;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>{
    private ArrayList<Ticket> mTicketList;
    private final Context mContext;

    public ListAdapter(ArrayList<Ticket> TicketList, Context context){
        this.mTicketList = TicketList;
        this.mContext = context;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvPrice) TextView tvPrice;

        ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_bills_list_mode, viewGroup, false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        Ticket Ticket = mTicketList.get(position);

        holder.tvTitle.setText(Ticket.getTitle());
        holder.tvPrice.setText(String.format(mContext.getResources().getString(R.string.TAB_CARDS_BILL_PRICE), Ticket.getAmount()));
    }

    @Override
    public int getItemCount() {
        return mTicketList.size();
    }
}