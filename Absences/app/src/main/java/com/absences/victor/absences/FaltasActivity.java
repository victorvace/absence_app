package com.absences.victor.absences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.absences.victor.absences.adapters.FaltasAdapter;
import com.absences.victor.absences.asyncTask.AlumnoATask;
import com.absences.victor.absences.asyncTask.FaltaATask;
import com.absences.victor.absences.asyncTask.HorarioATask;
import com.absences.victor.absences.domains.Alumno;
import com.absences.victor.absences.domains.Falta;
import com.absences.victor.absences.domains.Horario;
import com.absences.victor.absences.domains.Modulo;
import com.absences.victor.absences.interfaces.OnAlumnoReturn;
import com.absences.victor.absences.interfaces.OnFaltasItemClickListener;
import com.absences.victor.absences.interfaces.OnFaltasReturn;
import com.absences.victor.absences.interfaces.OnHorarioReturn;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FaltasActivity extends AppCompatActivity implements OnFaltasReturn, OnAlumnoReturn, OnHorarioReturn, OnFaltasItemClickListener {

    private TextView nombre, apellidos, correo;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Falta> listaFaltas;
    private ArrayList<Horario> listaHorario;
    private List<String> list;
    private List<Integer> listID;

    private FaltasAdapter adapter;
    private ArrayAdapter<String> spinnerAdapter;

    private Integer idModulo, idAlumno, idHorario;
    private Button falta;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faltas);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Faltas");

        spinner = (Spinner) findViewById(R.id.spinner);

        listaHorario = new ArrayList<>();
        list = new ArrayList<>();
        listID = new ArrayList<>();

        HorarioATask horarioATask = new HorarioATask(this, this);
        horarioATask.execute();

        SharedPreferences prefs = getSharedPreferences("userObject", Context.MODE_PRIVATE);
        idModulo = prefs.getInt("ID_ASIGNATURA", 0);
        idAlumno = prefs.getInt("ID_ALUMNO", 0);

        nombre = (TextView) findViewById(R.id.item_tittle);
        apellidos = (TextView) findViewById(R.id.second_item);
        correo = (TextView) findViewById(R.id.third_item);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvf);

        AlumnoATask alumnoATask = new AlumnoATask(this, this, idAlumno);
        alumnoATask.execute();

        listaHorario = new ArrayList<>();

        falta = (Button) findViewById(R.id.action_button_1);
        falta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        final ApiConnector apiConnector = new ApiConnector();
                        final String URI_POST = "faltas";
                        String token;

                        String method = "POST";
                        String json;

                        Alumno alumno = new Alumno(idAlumno);
                        Modulo modulo = new Modulo(idModulo);
                        Horario horario = new Horario(idHorario);

                        Falta falta = new Falta(alumno, horario, modulo);

                        json = gson.toJson(falta, Falta.class);

                        SharedPreferences prefs = getSharedPreferences("userObject", Context.MODE_PRIVATE);
                        token = prefs.getString("token", "");

                        apiConnector.consult(URI_POST, method, json, token);

                        FaltaATask faltaATask = new FaltaATask(FaltasActivity.this, FaltasActivity.this, idAlumno, idModulo);
                        faltaATask.execute();

                    }
                });
            }
        });

        listaFaltas = new ArrayList<>();

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        adapter = new FaltasAdapter(this, listaFaltas);
        adapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        FaltaATask faltaATask = new FaltaATask(this, this, idAlumno, idModulo);
        faltaATask.execute();

    }

    @Override
    public void onFaltasConsultados(ArrayList<Falta> listaFaltas) {
        this.listaFaltas.clear();
        this.listaFaltas.addAll(listaFaltas);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onFaltasError() {
        Toast.makeText(this, "Error en el listado de faltas", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAlumnosConsultados(Alumno alumno) {
        this.nombre.setText(alumno.getNombre());
        this.apellidos.setText(alumno.getApellidos());
        this.correo.setText(alumno.getCorreo());
    }

    @Override
    public void onAlumnosError() {
        Toast.makeText(this, "Error en los datos del alumno", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHorarioConsultados(ArrayList<Horario> listaHorario) {
        this.listaHorario.clear();
        this.listaHorario.addAll(listaHorario);
        this.adapter.notifyDataSetChanged();

        for (int i = 0; i < listaHorario.size(); i++) {
            listID.add(listaHorario.get(i).getIdHorario());
            list.add(listaHorario.get(i).getInicio());
        }

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                idHorario = listID.get(spinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

    @Override
    public void onHorarioError() {
        Toast.makeText(this, "Error con los horarios", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View v) {

    }
}
