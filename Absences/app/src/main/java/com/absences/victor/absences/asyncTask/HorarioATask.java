package com.absences.victor.absences.asyncTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.absences.victor.absences.ApiConnector;
import com.absences.victor.absences.LoginActivity;
import com.absences.victor.absences.domains.Horario;
import com.absences.victor.absences.domains.Modulo;
import com.absences.victor.absences.interfaces.OnHorarioReturn;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HorarioATask extends AsyncTask<Void, Void, ArrayList<Horario>> {

    private Context context;
    private OnHorarioReturn mListener;

    public HorarioATask(OnHorarioReturn mListener, Context context){
        this.mListener = mListener;
        this.context = context;
    }

    @Override
    protected ArrayList<Horario> doInBackground(Void... voids) {

        ArrayList<Horario> listaHorarios = new ArrayList<>();

        final String URI_GET = "horarios";

        Gson gson = new Gson();
        String token;

        String uri;
        String method;
        String json = null;

        final ApiConnector apiConnector = new ApiConnector();

        SharedPreferences prefs = context.getSharedPreferences("userObject", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");

        uri = URI_GET;
        method = "GET";

        String response = apiConnector.consult(uri, method, json, token);
        Type tT = new TypeToken<ArrayList<Horario>>() {}.getType();

        if (response.equals("401")){
            context.startActivity(new Intent(context, LoginActivity.class));
        } else {
            listaHorarios = gson.fromJson(response,tT);
        }

        return listaHorarios;
    }

    @Override
    protected void onPostExecute(ArrayList<Horario> listaHorarios) {
        super.onPostExecute(listaHorarios);

        if (listaHorarios == null) {
            mListener.onHorarioError();
        } else {
            mListener.onHorarioConsultados(listaHorarios);
        }
    }
}
