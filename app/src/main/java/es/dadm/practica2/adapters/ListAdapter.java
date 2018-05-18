package es.dadm.practica2.adapters;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.ImgUtil;
import es.dadm.practica2.R;
import es.dadm.practica2.Ticket;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private List<Ticket> mTicketList;
    private Ticket mTicket;
    private Context mContext;
    private OnItemClickListener mClickListener;

    public interface OnItemLongClickListener {
        boolean onItemLongClicked(int position);
    }

    public interface OnItemClickListener {
        boolean onItemClicked(int position);
    }

    public ListAdapter(List<Ticket> TicketList, Context context, OnItemClickListener onItemClickListener){
        this.mTicketList = TicketList;
        this.mContext = context;
        this.mClickListener = onItemClickListener;
    }

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvDescription) TextView tvDescription;
        @BindView(R.id.tvPrice) TextView tvPrice;
        @BindView(R.id.ivTicketImg) ImageView ivTicketImg;

        private View v;

        ListViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.v = itemView;

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onItemClicked(getAdapterPosition());
        }
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_tickets_list_mode, viewGroup, false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        final Ticket ticket = mTicketList.get(position);

        holder.tvTitle.setText(ticket.getTitle());
        holder.tvDescription.setText(ticket.getDescription());
        holder.tvPrice.setText(String.format(mContext.getResources().getString(R.string.TICKET_PRICE), ticket.getPrice()));
        holder.ivTicketImg.setImageBitmap(ImgUtil.getImageAsBitmap(ticket.getImgFilename(), mContext));

        holder.v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(mContext, "Pulsación larga sobre '" + ticket.getTitle() + "'.", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        holder.v.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                Toast.makeText(mContext, "Menu creado supuestamente", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionBarMenu){
            Toast.makeText(this, getString(R.string.aboutMenuToast), Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public int getItemCount() {
        return mTicketList.size();
    }

    public void setContent(List<Ticket> newTicketList){
        mTicketList = newTicketList;
    }
}