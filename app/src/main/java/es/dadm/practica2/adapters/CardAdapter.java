package es.dadm.practica2.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.Bill;
import es.dadm.practica2.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder>{
    private ArrayList<Bill> mBillList;
    private final Context mContext;

    public CardAdapter(ArrayList<Bill> billList, Context context){
        this.mBillList = billList;
        this.mContext = context;
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cvBill) CardView cvBill;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvPrice) TextView tvPrice;

        CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_recycler, viewGroup, false);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Bill bill = mBillList.get(position);

        holder.tvTitle.setText(bill.getTitle());
        holder.tvPrice.setText(String.format(mContext.getResources().getString(R.string.TAB_CARDS_BILL_PRICE), bill.getAmount()));
    }

    @Override
    public int getItemCount() {
        return mBillList.size();
    }
}