package com.absences.victor.absences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.absences.victor.absences.asyncTask.LoginATask;
import com.absences.victor.absences.domains.Usuario;
import com.absences.victor.absences.interfaces.OnLoginReturn;

public class LoginActivity extends AppCompatActivity implements OnLoginReturn {

    private static final int REQUEST_SING_UP = 1;

    private EditText input_email, input_password;
    private Button btn_login, link_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        link_sign_up = (Button) findViewById(R.id.link_signup);
        link_sign_up.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

    }

    public void login() {

        ApiConnector apiConnector = new ApiConnector();

        btn_login.setEnabled(false);

        if (!validate()) {
            onLoginFailed();
            return;
        }

        String email = input_email.getText().toString();
        String contrasena = apiConnector.convertSHA256(input_password.getText().toString());

        Usuario user = new Usuario(email, contrasena);

        LoginATask loginATask = new LoginATask(this, this, user);
        loginATask.execute();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginFailed() {
        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();

        btn_login.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = input_email.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("e-mail no válido");
            valid = false;
        } else if (email.length() > 100) {
            input_email.setError("Demasiado largo");
            valid = false;
        } else {
            input_email.setError(null);
        }

        if (input_password.getText().toString().isEmpty() || input_password.getText().toString().length() < 4) {
            input_password.setError("Mínimo 4 carácteres");
            valid = false;
        } else if (input_password.getText().toString().length() > 45) {
            input_password.setError("Demasiado largo");
            valid = false;
        } else {
            input_password.setError(null);
        }

        return valid;
    }


    @Override
    public void onLoginConsultados(Usuario usuario) {
        SharedPreferences prefs = getSharedPreferences("userObject", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        if (!token.isEmpty()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivityForResult(intent, REQUEST_SING_UP);
            finish();
        }
    }

    @Override
    public void onLoginError() {
        Toast.makeText(this, "Error en el Login", Toast.LENGTH_LONG).show();
        btn_login.setEnabled(true);
    }
}
