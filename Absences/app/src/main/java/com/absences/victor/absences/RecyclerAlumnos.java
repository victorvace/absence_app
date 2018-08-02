package com.absences.victor.absences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.absences.victor.absences.adapters.AlumnosAdapter;
import com.absences.victor.absences.asyncTask.AlumnosATask;
import com.absences.victor.absences.domains.Matricula;
import com.absences.victor.absences.interfaces.OnAlumnoItemClickListener;
import com.absences.victor.absences.interfaces.OnAlumnosReturn;

import java.util.ArrayList;

public class RecyclerAlumnos extends AppCompatActivity implements OnAlumnosReturn, OnAlumnoItemClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Matricula> listaAlumnos;

    private AlumnosAdapter adapter;

    private Integer idModulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_alumnos);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Alumnos");

        SharedPreferences prefs = getSharedPreferences("userObject", Context.MODE_PRIVATE);
        idModulo = prefs.getInt("ID_ASIGNATURA", 0);

        mRecyclerView = (RecyclerView) findViewById(R.id.rva);

        listaAlumnos = new ArrayList<>();

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        adapter = new AlumnosAdapter(this, listaAlumnos);
        adapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        AlumnosATask alumnosATask = new AlumnosATask(this, this, idModulo);
        alumnosATask.execute();

    }

    @Override
    public void onItemClick(View v) {
        int position = mRecyclerView.getChildAdapterPosition(v);

        SharedPreferences prefs = getSharedPreferences("userObject", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putInt("ID_ALUMNO", listaAlumnos.get(position).getAlumno().getIdAlumno());
        prefsEditor.commit();

        startActivity(new Intent(this, FaltasActivity.class));
    }

    @Override
    public void onAlumnosConsultados(ArrayList<Matricula> listaAlumnos) {
        this.listaAlumnos.clear();
        this.listaAlumnos.addAll(listaAlumnos);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onAlumnosError() {
        Toast.makeText(RecyclerAlumnos.this, "Error con el listado de alumnos", Toast.LENGTH_SHORT).show();
    }

}
