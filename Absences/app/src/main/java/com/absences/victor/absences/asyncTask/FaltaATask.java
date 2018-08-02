package com.absences.victor.absences.asyncTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.absences.victor.absences.ApiConnector;
import com.absences.victor.absences.LoginActivity;
import com.absences.victor.absences.domains.Falta;
import com.absences.victor.absences.interfaces.OnFaltasReturn;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FaltaATask extends AsyncTask<Void, Void, ArrayList<Falta>> {

    private Context context;
    private OnFaltasReturn mListener;
    private int idAlumno, idModulo;

    public FaltaATask(OnFaltasReturn mListener, Context context, Integer idAlumno, Integer idModulo) {
        this.mListener = mListener;
        this.context = context;
        this.idAlumno = idAlumno;
        this.idModulo = idModulo;
    }

    @Override
    protected ArrayList<Falta> doInBackground(Void... voids) {

        ArrayList<Falta> listaFaltas = new ArrayList<>();

        final String URI_GET = "faltas";

        Gson gson = new Gson();
        String token;

        String uri;
        String method;
        String json = null;

        final ApiConnector apiConnector = new ApiConnector();

        SharedPreferences prefs = context.getSharedPreferences("userObject", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");

        uri = URI_GET + "/modulo/" + idModulo + "/alumno/" + idAlumno;
        method = "GET";

        String response = apiConnector.consult(uri, method, json, token);
        Type tT = new TypeToken<ArrayList<Falta>>() {
        }.getType();

        if (response.equals("401")) {
            context.startActivity(new Intent(context, LoginActivity.class));
        } else {
            listaFaltas = gson.fromJson(response, tT);
        }

        return listaFaltas;
    }

    @Override
    protected void onPostExecute(ArrayList<Falta> listaFaltas) {
        super.onPostExecute(listaFaltas);

        if (listaFaltas == null) {
            mListener.onFaltasError();
        } else {
            mListener.onFaltasConsultados(listaFaltas);
        }
    }
}
