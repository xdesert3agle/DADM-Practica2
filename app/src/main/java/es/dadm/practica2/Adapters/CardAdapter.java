package es.dadm.practica2.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.ImgUtil;
import es.dadm.practica2.Interfaces.ElementActions;
import es.dadm.practica2.R;
import es.dadm.practica2.Ticket;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder>{
    private List<Ticket> mTicketList;
    private Context mContext;
    private ElementActions mElementActions;

    public CardAdapter(List<Ticket> TicketList, Context context, ElementActions elementActions){
        this.mTicketList = TicketList;
        this.mContext = context;
        this.mElementActions = elementActions;
    }

    class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvDescription) TextView tvDescription;
        @BindView(R.id.tvPrice) TextView tvPrice;
        @BindView(R.id.ivImg) ImageView ivImg;

        CardViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            mElementActions.onItemClicked(getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            mElementActions.onCreateContextMenu(view, contextMenu, getAdapterPosition());
        }
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_card_mode, viewGroup, false);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Ticket ticket = mTicketList.get(position);

        holder.tvTitle.setText(ticket.getTitle());
        holder.tvDescription.setText(ticket.getDescription());
        holder.tvPrice.setText(String.format(mContext.getResources().getString(R.string.TICKET_PRICE), ticket.getPrice()));
        holder.ivImg.setImageBitmap(ImgUtil.getImageAsBitmap(ticket.getImgFilename(), mContext));
    }

    @Override
    public int getItemCount() {
        return mTicketList.size();
    }

    public void setContent(List<Ticket> newTicketList){
        mTicketList = newTicketList;
    }
}