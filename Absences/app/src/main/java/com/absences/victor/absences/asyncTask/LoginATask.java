package com.absences.victor.absences.asyncTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.absences.victor.absences.ApiConnector;
import com.absences.victor.absences.LoginActivity;
import com.absences.victor.absences.MainActivity;
import com.absences.victor.absences.domains.Usuario;
import com.absences.victor.absences.interfaces.OnLoginReturn;
import com.google.gson.Gson;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class LoginATask extends AsyncTask<Void, Void, Usuario> {

    private Context context;
    private OnLoginReturn mListener;
    private Usuario user;

    private Gson gson = new Gson();

    public LoginATask(OnLoginReturn mListener, Context context, Usuario user) {
        this.mListener = mListener;
        this.context = context;
        this.user = user;
    }

    @Override
    protected Usuario doInBackground(Void... voids) {

        try {
            String token = "";
            String json = gson.toJson(user);
            final String URI_LOGIN = "usuarios/login";
            String method = "POST";

            final ApiConnector apiConnector = new ApiConnector();
            String response = apiConnector.consult(URI_LOGIN, method, json, token);

            if (response == null) {
                user = null;
            } else {
                user = gson.fromJson(response, Usuario.class);

                SharedPreferences prefs = context.getSharedPreferences("userObject", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putInt("userId", user.getIdUsuario());
                prefsEditor.putString("email", user.getEmail());
                prefsEditor.putString("username", user.getUsuario());
                prefsEditor.putInt("admin", user.getAdmin());
                prefsEditor.putString("token", user.getToken());
                prefsEditor.commit();
            }

        } catch (Exception e) {
            user = null;
        }

        return user;
    }

    @Override
    protected void onPostExecute(Usuario usuario) {
        super.onPostExecute(usuario);

        if (usuario == null) {
            mListener.onLoginError();
        } else {
            mListener.onLoginConsultados(usuario);
        }
    }
}
