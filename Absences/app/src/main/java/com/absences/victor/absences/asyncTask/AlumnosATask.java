package com.absences.victor.absences.asyncTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.absences.victor.absences.ApiConnector;
import com.absences.victor.absences.LoginActivity;
import com.absences.victor.absences.domains.Matricula;
import com.absences.victor.absences.interfaces.OnAlumnosReturn;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AlumnosATask extends AsyncTask<Void, Void, ArrayList<Matricula>> {

    private OnAlumnosReturn mListener;
    private Context context;
    private int idModulo;

    public AlumnosATask(OnAlumnosReturn mListener, Context context, int id) {
        this.mListener = mListener;
        this.context = context;
        this.idModulo = id;
    }

    @Override
    protected ArrayList<Matricula> doInBackground(Void... voids) {
        ArrayList<Matricula> listaAlumnos = new ArrayList<>();

        final String URI_GET = "matriculas";

        Gson gson = new Gson();
        String token;

        String uri;
        String method;
        String json = null;

        final ApiConnector apiConnector = new ApiConnector();

        SharedPreferences prefs = context.getSharedPreferences("userObject", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");

        uri = URI_GET + "/modulo/" + idModulo;
        method = "GET";

        String response = apiConnector.consult(uri, method, json, token);
        Type tT = new TypeToken<ArrayList<Matricula>>() {
        }.getType();

        if (response.equals("401")) {
            context.startActivity(new Intent(context, LoginActivity.class));
        } else {
            listaAlumnos = gson.fromJson(response, tT);
        }

        return listaAlumnos;

    }

    @Override
    protected void onPostExecute(ArrayList<Matricula> listaAlumnos) {
        super.onPostExecute(listaAlumnos);

        if (listaAlumnos == null) {
            mListener.onAlumnosError();
        } else {
            mListener.onAlumnosConsultados(listaAlumnos);
        }

    }
}
