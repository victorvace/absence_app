package com.absences.victor.absences.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.absences.victor.absences.R;
import com.absences.victor.absences.domains.Falta;
import com.absences.victor.absences.interfaces.OnFaltasItemClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FaltasAdapter extends RecyclerView.Adapter<FaltasAdapter.FaltasViewHolder> implements View.OnClickListener {

    private ArrayList<Falta> listaFaltas;
    private OnFaltasItemClickListener mlistener;
    private Context context;


    public FaltasAdapter(Context context, ArrayList<Falta> listaFaltas) {
        this.context = context;
        this.listaFaltas = listaFaltas;
    }

    @Override
    public FaltasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_falta, parent, false);
        itemView.setOnClickListener(this);
        return new FaltasViewHolder(itemView);
    }

    public void setOnItemClickListener(OnFaltasItemClickListener mListener) {
        this.mlistener = mListener;
    }

    @Override
    public void onBindViewHolder(FaltasAdapter.FaltasViewHolder holder, int position) {

        context = holder.itemView.getContext();

        holder.item_hora.setText(listaFaltas.get(position).getHorario().getInicio());

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd");
        holder.item_dia.setText(simpleDate.format(listaFaltas.get(position).getFecha()));
    }

    @Override
    public int getItemCount() {
        return listaFaltas.size();
    }


    @Override
    public void onClick(View v) {
        mlistener.onItemClick(v);
    }

    public class FaltasViewHolder extends RecyclerView.ViewHolder {

        TextView item_hora, item_dia;

        public FaltasViewHolder(View itemView) {
            super(itemView);

            item_hora = (TextView) itemView.findViewById(R.id.item_hora);
            item_dia = (TextView) itemView.findViewById(R.id.item_dia);

        }
    }
}
