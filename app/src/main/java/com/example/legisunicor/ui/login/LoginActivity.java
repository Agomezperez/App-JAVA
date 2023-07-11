package com.example.legisunicor.ui.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.legisunicor.MainActivity;
import com.example.legisunicor.R;
import com.example.legisunicor.db.DatabaseHelper;
import com.example.legisunicor.db.models.User;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView user = (TextView) findViewById(R.id.inputEmail);
        TextView pass = (TextView) findViewById(R.id.inputPassword);

        Button loginbtn = (Button) findViewById(R.id.btnLogin);
        TextView registerBtn = findViewById(R.id.btnRegister);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user.getText().toString();
                String password = pass.getText().toString();

                LoginTask loginTask = new LoginTask();
                loginTask.execute(username, password);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroUserActivity.class);
                startActivity(intent);
            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... params) {
            String username = params[0];
            String password = params[1];

            DatabaseHelper databaseHelper = new DatabaseHelper(LoginActivity.this);
            User usuario = databaseHelper.login(username, password);
            return usuario;
        }

        @Override
        protected void onPostExecute(User usuario) {
            if (usuario != null) {
                int idUsuario = usuario.getId();
                int idRol = usuario.getIdRol();

                if (idRol == 1) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso como administrador", Toast.LENGTH_SHORT).show();
                } else if (idRol == 2) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso como abogado", Toast.LENGTH_SHORT).show();
                } else if (idRol == 3) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso como cliente", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Login incorrecto", Toast.LENGTH_SHORT).show();
            }
        }
    }
}