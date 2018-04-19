package es.dadm.practica2.util;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.Factura;
import es.dadm.practica2.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CuestionViewHolder>{

    private List<Factura> cuestiones;

    public CardAdapter(List<Factura> facturas){
        this.cuestiones = facturas;
    }

    @Override
    public CuestionViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
        return new CuestionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CuestionViewHolder holder, int position) {
        Factura factura = cuestiones.get(position);

        holder.tvNombre.setText(factura.getShortDesc());
        holder.tvPrecio.setText(String.valueOf(factura.getAmount()) + " €");
    }

    @Override
    public int getItemCount() {
        return cuestiones.size();
    }

    static class CuestionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cvFactura) CardView cvFactura;
        @BindView(R.id.tvNombre) TextView tvNombre;
        @BindView(R.id.tvPrecio) TextView tvPrecio;

        CuestionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}