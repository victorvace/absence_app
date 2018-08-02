package com.absences.victor.absences.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.absences.victor.absences.R;
import com.absences.victor.absences.domains.Modulo;
import com.absences.victor.absences.interfaces.OnCicloItemClickListener;

import java.util.ArrayList;

public class CicloAdapter extends RecyclerView.Adapter<CicloAdapter.CicloViewHolder> implements View.OnClickListener {

    private ArrayList<Modulo> listaCiclos;
    private OnCicloItemClickListener mlistener;

    private Context context;

    public CicloAdapter(Context context, ArrayList<Modulo> listaCiclos) {
        this.context = context;
        this.listaCiclos = listaCiclos;
    }

    public void setOnItemClickListener(OnCicloItemClickListener mListener) {
        this.mlistener = mListener;
    }

    @Override
    public CicloViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ciclo, parent, false);
        itemView.setOnClickListener(this);

        CicloViewHolder cvh = new CicloViewHolder(itemView);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CicloViewHolder holder, int position) {
        holder.siglas.setText(listaCiclos.get(position).getSiglas());
        holder.nombre.setText(listaCiclos.get(position).getNombre());
        holder.curso.setText(listaCiclos.get(position).getCurso().getNombre());
        holder.ciclo.setText(listaCiclos.get(position).getCurso().getCiclo().getNombre());
    }

    @Override
    public int getItemCount() {
        return listaCiclos.size();
    }


    @Override
    public void onClick(View v) {
        mlistener.onItemClick(v);
    }


    public class CicloViewHolder extends RecyclerView.ViewHolder {
        private TextView siglas, nombre, curso, ciclo;

        public CicloViewHolder(View itemView) {
            super(itemView);

            siglas = (TextView) itemView.findViewById(R.id.avatar_text);
            curso = (TextView) itemView.findViewById(R.id.item_tittle);
            nombre = (TextView) itemView.findViewById(R.id.second_item);
            ciclo = (TextView) itemView.findViewById(R.id.third_item);
        }
    }
}