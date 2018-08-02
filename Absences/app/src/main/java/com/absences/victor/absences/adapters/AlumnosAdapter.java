package com.absences.victor.absences.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.absences.victor.absences.R;
import com.absences.victor.absences.domains.Matricula;
import com.absences.victor.absences.interfaces.OnAlumnoItemClickListener;

import java.util.ArrayList;

public class AlumnosAdapter extends RecyclerView.Adapter<AlumnosAdapter.AlumnosViewHolder> implements View.OnClickListener {

    private ArrayList<Matricula> listaAlumnos;
    private OnAlumnoItemClickListener listener;
    private Context context;

    public AlumnosAdapter(Context context, ArrayList<Matricula> listaAlumnos) {
        this.context = context;
        this.listaAlumnos = listaAlumnos;
    }

    @Override
    public AlumnosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alumno, parent, false);
        itemView.setOnClickListener(this);
        return new AlumnosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AlumnosViewHolder holder, int position) {

        context = holder.itemView.getContext();

        String nombre = listaAlumnos.get(position).getAlumno().getNombre();
        String apellidos = listaAlumnos.get(position).getAlumno().getApellidos();
        String textoAvatar = nombre.substring(0, 1) + apellidos.substring(0, 1);

        holder.nombre.setText(nombre);
        holder.apellidos.setText(apellidos);
        holder.email.setText(listaAlumnos.get(position).getAlumno().getCorreo());

        holder.avatar_text.setText(textoAvatar);
    }

    @Override
    public int getItemCount() {
        return listaAlumnos.size();
    }

    public void setOnItemClickListener(OnAlumnoItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onItemClick(v);
    }

    public class AlumnosViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, apellidos, email, avatar_text;

        public AlumnosViewHolder(View itemView) {
            super(itemView);

            nombre = (TextView) itemView.findViewById(R.id.item_tittle);
            apellidos = (TextView) itemView.findViewById(R.id.second_item);
            email = (TextView) itemView.findViewById(R.id.third_item);
            avatar_text = (TextView) itemView.findViewById(R.id.avatar_text);


        }
    }
}
