package com.absences.victor.absences;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.absences.victor.absences.domains.Usuario;
import com.google.gson.Gson;

public class RegisterActivity extends AppCompatActivity {

    private static final int REQUEST_SING_UP = 1;

    private EditText input_user, input_email, input_password, confirm_password;
    private Button btn_login_activity, link_signup_activity;

    private final static String URI_REGISTER = "usuarios/register";
    private Gson gson = new Gson();

    private String uri = null;
    private String method = null;
    private String json = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        input_user = (EditText) findViewById(R.id.input_user);
        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);

        btn_login_activity = (Button) findViewById(R.id.btn_login_activity);
        btn_login_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(RegisterActivity.this, LoginActivity.class), REQUEST_SING_UP);
                finish();
            }
        });

        link_signup_activity = (Button) findViewById(R.id.link_signup_activity);
        link_signup_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();

            }
        });

        uri = URI_REGISTER;
        method = "POST";

    }

    private void register() {

        ApiConnector apiConnector = new ApiConnector();

        link_signup_activity.setEnabled(true);

        if (!validate()) {
            onRegisterFailed();
            return;
        }

        String username = input_user.getText().toString();
        String email = input_email.getText().toString();
        String contrasena = apiConnector.convertSHA256(input_password.getText().toString());

        Usuario user = new Usuario(username, email, contrasena);
        json = gson.toJson(user);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ApiConnector apiConnector = new ApiConnector();
                apiConnector.consult(uri, method, json, "");
            }
        });

        startActivityForResult(new Intent(RegisterActivity.this, LoginActivity.class), REQUEST_SING_UP);
        finish();

    }

    @Override
    public void onBackPressed() {
        // Deshabilita poder volver desde MainActivity tras hacer registro.
        moveTaskToBack(true);
    }

    public void onRegisterFailed() {
        Toast.makeText(this, "Register failed", Toast.LENGTH_SHORT).show();

        link_signup_activity.setEnabled(true);
    }


    public boolean validate() {
        boolean valid = true;

        String username = input_user.getText().toString();
        String email = input_email.getText().toString();
        String password = input_password.getText().toString();
        String confirmation = confirm_password.getText().toString();

        if (username.isEmpty() || username.length() < 3) {
            input_user.setError("Demasiado corto");
            valid = false;
        } else if (username.length() > 45) {
            input_user.setError("Demasiado largo");
            valid = false;
        } else {
            input_user.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("e-mail no válido");
            valid = false;
        } else if (email.length() > 100) {
            input_email.setError("Demasiado largo");
            valid = false;
        } else {
            input_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            input_password.setError("Mínimo 4 carácteres");
            valid = false;
        } else if (password.length() > 45) {
            input_password.setError("Demasiado largo");
            valid = false;
        } else {
            input_password.setError(null);
        }

        if (confirmation.isEmpty() || !confirmation.equals(password)) {
            input_password.setError("No coinciden");
            confirm_password.setError("No coinciden");
            valid = false;
        } else {
            confirm_password.setError(null);
        }

        return valid;
    }
}
