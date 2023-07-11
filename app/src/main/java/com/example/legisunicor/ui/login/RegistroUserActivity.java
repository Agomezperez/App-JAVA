package com.example.legisunicor.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.legisunicor.R;
import com.example.legisunicor.databinding.ActivityRegistroUserBinding;
import com.example.legisunicor.db.DatabaseHelper;

public class RegistroUserActivity extends AppCompatActivity {
    private ActivityRegistroUserBinding binding;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.roles_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerRoles.setAdapter(adapter);

        binding.btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.etUsername.getText().toString();
                String password = binding.etPassword.getText().toString();
                String nombre = binding.etNombre.getText().toString();
                String apellidos = binding.etApellidos.getText().toString();
                int idRol = binding.spinnerRoles.getSelectedItemPosition() ;

                boolean registroExitoso = databaseHelper.registrarUsuario(username, password, nombre, apellidos, idRol);

                if (registroExitoso) {
                    Toast.makeText(RegistroUserActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegistroUserActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}