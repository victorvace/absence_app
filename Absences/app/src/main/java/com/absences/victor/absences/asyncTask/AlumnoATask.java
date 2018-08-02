package com.absences.victor.absences.asyncTask;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.absences.victor.absences.ApiConnector;
import com.absences.victor.absences.LoginActivity;
import com.absences.victor.absences.domains.Alumno;
import com.absences.victor.absences.interfaces.OnAlumnoReturn;
import com.google.gson.Gson;

public class AlumnoATask extends AsyncTask<Void, Void, Alumno> {

    private Context context;
    private OnAlumnoReturn mListener;
    private int idAlumno;

    public AlumnoATask(OnAlumnoReturn mListener, Context context, Integer idAlumno){
        this.mListener = mListener;
        this.context = context;
        this.idAlumno = idAlumno;
    }

    @Override
    protected Alumno doInBackground(Void... voids) {

        Alumno alumno = new Alumno();

        final String URI_GET = "alumnos";

        Gson gson = new Gson();
        String token;

        String uri;
        String method;
        String json = null;

        final ApiConnector apiConnector = new ApiConnector();

        SharedPreferences prefs = context.getSharedPreferences("userObject", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");

        uri = URI_GET + "/" + idAlumno;
        method = "GET";

        String response = apiConnector.consult(uri, method, json, token);

        if (response.equals("401")){
            context.startActivity(new Intent(context, LoginActivity.class));
        } else {
            alumno = gson.fromJson(response, Alumno.class);
        }

        return alumno;
    }

    @Override
    protected void onPostExecute(Alumno alumno) {
        super.onPostExecute(alumno);

        if (alumno == null){
            mListener.onAlumnosError();
        } else {
            mListener.onAlumnosConsultados(alumno);
        }
    }
}
