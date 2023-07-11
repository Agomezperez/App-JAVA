package com.example.legisunicor.ui.fichero;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.legisunicor.R;
import com.example.legisunicor.databinding.FragmentCargarRolesDesdeArchivoBinding;
import com.example.legisunicor.db.DatabaseHelper;
import com.example.legisunicor.db.models.Roles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


public class CargarRolesDesdeArchivoFragment extends Fragment {
    private FragmentCargarRolesDesdeArchivoBinding binding;
    private static final int READ_REQUEST_CODE = 42; // Código de solicitud para la selección de archivo
    private DatabaseHelper databaseHelper;
    private TableLayout tableLayout;

    public CargarRolesDesdeArchivoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCargarRolesDesdeArchivoBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseHelper = new DatabaseHelper(requireContext());
        tableLayout = view.findViewById(R.id.table_roles);

        Button btnSeleccionarArchivo = view.findViewById(R.id.btn_seleccionar_archivo);
        btnSeleccionarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarArchivoTxt();
            }
        });


    }

    private void seleccionarArchivoTxt() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/plain");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                cargarDatosDesdeArchivo(uri);
            }
        }
    }

    private void cargarDatosDesdeArchivo(Uri uri) {
        try {
            InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] campos = linea.split(",");

                if (campos.length >= 2) {
                    int idRol = Integer.parseInt(campos[0].trim());
                    String nombre = campos[1].trim();

                    Roles rol = new Roles(idRol, nombre);

                    databaseHelper.insertarRol(rol);
                }
            }

            reader.close();
            mostrarRoles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarRoles() {
        tableLayout.removeAllViews();

        List<Roles> roles = databaseHelper.obtenerRoles();
        for (Roles rol : roles) {
            TableRow row = new TableRow(requireContext());
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            );
            row.setLayoutParams(layoutParams);

            TextView tvId = new TextView(requireContext());
            tvId.setText(String.valueOf(rol.getIdRol()));
            row.addView(tvId);

            TextView tvNombre = new TextView(requireContext());
            tvNombre.setText(rol.getRolname());
            row.addView(tvNombre);

            tableLayout.addView(row);
        }
    }
}