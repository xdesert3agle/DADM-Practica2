package es.dadm.practica2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.ImgProvider;
import es.dadm.practica2.R;
import es.dadm.practica2.Ticket;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>{
    private List<Ticket> mTicketList;
    private final Context mContext;

    public ListAdapter(List<Ticket> TicketList, Context context){
        this.mTicketList = TicketList;
        this.mContext = context;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvDescription) TextView tvDescription;
        @BindView(R.id.tvPrice) TextView tvPrice;
        @BindView(R.id.ivTicketImg)
        ImageView ivTicketImg;

        ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_tickets_list_mode, viewGroup, false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        Ticket ticket = mTicketList.get(position);
        ImgProvider imgProvider = new ImgProvider(mContext);

        holder.tvTitle.setText(ticket.getTitle());
        holder.tvDescription.setText(ticket.getDescription());
        holder.tvPrice.setText(String.format(mContext.getResources().getString(R.string.TICKET_PRICE), ticket.getPrice()));
        holder.ivTicketImg.setImageBitmap(imgProvider.getImgAsBitmap(ticket.getImgFilename()));
    }

    @Override
    public int getItemCount() {
        return mTicketList.size();
    }

    public void setContent(List<Ticket> newTicketList){
        mTicketList = newTicketList;
    }
}