package com.absences.victor.absences.asyncTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.absences.victor.absences.ApiConnector;
import com.absences.victor.absences.LoginActivity;
import com.absences.victor.absences.domains.Modulo;
import com.absences.victor.absences.interfaces.OnCiclosReturn;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CiclosATask extends AsyncTask<Void, Void, ArrayList<Modulo>> {

    private Context context;
    private OnCiclosReturn mListener;

    public CiclosATask(OnCiclosReturn mListener, Context context) {
        this.mListener = mListener;
        this.context = context;
    }

    @Override
    protected ArrayList<Modulo> doInBackground(Void... voids) {

        ArrayList<Modulo> listaCiclos = new ArrayList<>();

        final String URI_GET = "usuarios";

        Gson gson = new Gson();
        String token;

        String uri;
        String method;
        String json = null;

        int ID;

        final ApiConnector apiConnector = new ApiConnector();

        SharedPreferences prefs = context.getSharedPreferences("userObject", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");
        ID = prefs.getInt("userId", -1);

        uri = URI_GET + "/" + ID + "/ciclos";
        method = "GET";

        String response = apiConnector.consult(uri, method, json, token);
        Type tT = new TypeToken<ArrayList<Modulo>>() {}.getType();

        if (response.equals("401")){
            context.startActivity(new Intent(context, LoginActivity.class));
        } else {
            listaCiclos = gson.fromJson(response,tT);
        }

        return listaCiclos;
    }

    @Override
    protected void onPostExecute(ArrayList<Modulo> listaCiclos) {
        super.onPostExecute(listaCiclos);

        if (listaCiclos == null){
            mListener.onCiclosError();
        } else {
            mListener.onCiclosConsultados(listaCiclos);
        }


    }
}
