package es.dadm.practica2.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
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
import es.dadm.practica2.Interfaces.TicketActions;
import es.dadm.practica2.R;
import es.dadm.practica2.Ticket;

public class TileAdapter extends RecyclerView.Adapter<TileAdapter.TileViewHolder>{
    private List<Ticket> mTicketList;
    private Context mContext;
    private TicketActions mTicketActions;

    public TileAdapter(List<Ticket> TicketList, Context context, TicketActions ticketActions){
        this.mTicketList = TicketList;
        this.mContext = context;
        this.mTicketActions = ticketActions;
    }

    class TileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.ivTicketImg) ImageView ivTicketImg;

        TileViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            mTicketActions.onItemClicked(getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            mTicketActions.onCreateContextMenu(view, contextMenu, getAdapterPosition());
        }
    }

    @Override
    public TileViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_tickets_tile_mode, viewGroup, false);

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int desiredWidth = displayMetrics.widthPixels / 2;

        Log.d("Width", String.valueOf(desiredWidth));

        v.getLayoutParams().width = desiredWidth;
        v.getLayoutParams().height = desiredWidth;

        return new TileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TileViewHolder holder, int position) {
        Ticket ticket = mTicketList.get(position);

        holder.ivTicketImg.setImageBitmap(ImgUtil.getImageAsBitmap(ticket.getImgFilename(), mContext));
        holder.tvTitle.setText(ticket.getTitle());
    }

    @Override
    public int getItemCount() {
        return mTicketList.size();
    }

    public void setContent(List<Ticket> newTicketList){
        mTicketList = newTicketList;
    }
}