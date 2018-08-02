package com.absences.victor.absences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.absences.victor.absences.domains.Usuario;
import com.google.gson.Gson;

public class SettingsActivity extends AppCompatActivity {

    private EditText input_password, input_new_password_1, input_new_password_2, input_password_baja_1, input_password_baja_2;
    private Button change_password, baja_button;

    private final static String URI_MOD = "usuarios/mod";
    private final static String URI_DROP = "usuarios/drop";

    private String email = null;

    private Gson gson = new Gson();
    private String token = "";
    private String username = "";

    private String uri = null;
    private String method = "POST";
    private String json = null;

    private Usuario userReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Ajustes");

        input_password = (EditText) findViewById(R.id.input_password);
        input_new_password_1 = (EditText) findViewById(R.id.input_new_password_1);
        input_new_password_2 = (EditText) findViewById(R.id.input_new_password_2);
        input_password_baja_1 = (EditText) findViewById(R.id.input_password_baja_1);
        input_password_baja_2 = (EditText) findViewById(R.id.input_password_baja_2);
        change_password = (Button) findViewById(R.id.change_password);

        SharedPreferences prefs = getSharedPreferences("userObject", Context.MODE_PRIVATE);
        email = prefs.getString("email", "");
        token = prefs.getString("token", "");

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePass();
            }
        });
        baja_button = (Button) findViewById(R.id.baja_button);
        baja_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bajaService();
            }
        });

    }

    private void bajaService() {
        final ApiConnector apiConnector = new ApiConnector();

        uri = URI_DROP;

        if (!validateBaja()) {
            bajaFailed();
            return;
        }

        final String pass = apiConnector.convertSHA256(input_password_baja_1.getText().toString());

        Usuario user = new Usuario(email, pass);
        json = gson.toJson(user);

        Toast.makeText(this, "Sentimos mucho que nos dejes", Toast.LENGTH_SHORT).show();


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String response = apiConnector.consult(uri, method, json, token);

                userReturn = gson.fromJson(response, Usuario.class);

                SharedPreferences prefs = getSharedPreferences("userObject", Context.MODE_PRIVATE);
                prefs.edit().clear().commit();

                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));

            }
        });

    }

    private void changePass() {

        final ApiConnector apiConnector = new ApiConnector();

        if (!validate()) {
            changePasswordFailed();
            return;
        }

        uri = URI_MOD;

        SharedPreferences prefs = getSharedPreferences("userObject", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");
        username = prefs.getString("username", "");

        final String pass = apiConnector.convertSHA256(input_password.getText().toString());
        final String passChanged = apiConnector.convertSHA256(input_new_password_1.getText().toString());

        Usuario user = new Usuario(username, email, pass, passChanged);
        json = gson.toJson(user);

        Toast.makeText(this, "Contraseña cambiada", Toast.LENGTH_SHORT).show();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String response = apiConnector.consult(uri, method, json, token);

                userReturn = gson.fromJson(response, Usuario.class);

                if (userReturn.getContrasena() != pass && userReturn.getContrasena().equals(passChanged)) {

                    SharedPreferences prefs = getSharedPreferences("userObject", Context.MODE_PRIVATE);
                    prefs.edit().clear().commit();

                    startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                }
            }
        });

    }

    public void changePasswordFailed() {
        Toast.makeText(this, "Action failed", Toast.LENGTH_SHORT).show();
    }

    public void bajaFailed() {
        Toast.makeText(this, "Error al darse de baja", Toast.LENGTH_SHORT).show();
    }

    public boolean validate() {
        boolean valid = true;

        if (input_password.getText().toString().isEmpty() || input_password.getText().toString().length() < 4) {
            input_password.setError("Mínimo 4 carácteres");
            valid = false;
        } else if (input_password.getText().toString().length() > 45) {
            input_password.setError("Demasiado largo");
            valid = false;
        } else {
            input_password.setError(null);
        }

        if (input_new_password_1.getText().toString().isEmpty() || input_new_password_1.getText().toString().length() < 4) {
            input_new_password_1.setError("Mínimo 4 carácteres");
            valid = false;
        } else if (input_new_password_1.getText().toString().length() > 45) {
            input_new_password_1.setError("Demasiado largo");
            valid = false;
        } else {
            input_new_password_1.setError(null);
        }

        if (input_new_password_2.getText().toString().isEmpty() || !input_new_password_2.getText().toString().equals(input_new_password_1.getText().toString())) {
            input_new_password_1.setError("No coinciden");
            input_new_password_2.setError("No coinciden");
            valid = false;
        } else {
            input_new_password_2.setError(null);
        }

        return valid;
    }

    public boolean validateBaja() {
        boolean valid = true;

        if (input_password_baja_1.getText().toString().isEmpty() || input_password_baja_1.getText().toString().length() < 4) {
            input_password_baja_1.setError("Mínimo 4 carácteres");
            valid = false;
        } else if (input_password_baja_1.getText().toString().length() > 45) {
            input_password_baja_1.setError("Demasiado largo");
            valid = false;
        } else {
            input_password_baja_1.setError(null);
        }

        if (input_password_baja_2.getText().toString().isEmpty() || !input_password_baja_2.getText().toString().equals(input_password_baja_1.getText().toString())) {
            input_password_baja_1.setError("No coinciden");
            input_password_baja_2.setError("No coinciden");
            valid = false;
        } else {
            input_password_baja_2.setError(null);
        }

        change_password.setEnabled(valid);

        return valid;
    }
}
