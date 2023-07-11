package com.example.legisunicor.ui.abogado;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.legisunicor.R;
import com.example.legisunicor.databinding.FragmentAbogadoBinding;
import com.example.legisunicor.db.DatabaseHelper;
import com.example.legisunicor.db.models.Caso;
import com.example.legisunicor.db.models.User;

import java.util.ArrayList;
import java.util.List;

public class AbogadoFragment extends Fragment {
    private FragmentAbogadoBinding binding;

    private DatabaseHelper databaseHelper;
    private List<Caso> casoList;
    private CasoAdapter casoAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAbogadoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseHelper = new DatabaseHelper(requireContext());

        casoList = databaseHelper.listarCasos();
        casoAdapter = new CasoAdapter(casoList, new CasoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Caso caso) {
                mostrarDialogoOpciones(caso);

            }
        });
        binding.recyclerViewCasos.setAdapter(casoAdapter);
        binding.recyclerViewCasos.setLayoutManager(new LinearLayoutManager(requireContext()));


        binding.btnCrearCaso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogCrearCaso();
            }
        });
    }

    private void mostrarDialogCrearCaso() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Crear Caso");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_crear_caso, null);
        builder.setView(dialogView);

        EditText etNombreCaso = dialogView.findViewById(R.id.et_nombre_caso);
        EditText etDescripcionCaso = dialogView.findViewById(R.id.et_descripcion_caso);
        Spinner spinnerEstado = dialogView.findViewById(R.id.spinnerEstados);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.estados, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);
        Spinner spinnerClientes = dialogView.findViewById(R.id.spinner_clientes);

        List<User> clientes = databaseHelper.obtenerClientes(3);
        List<String> nombresClientes = new ArrayList<>();
        for (User cliente : clientes) {
            nombresClientes.add(cliente.getNombre());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, nombresClientes);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClientes.setAdapter(spinnerAdapter);

        builder.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombreCaso = etNombreCaso.getText().toString();
                String descripcionCaso = etDescripcionCaso.getText().toString();
                String estadoSeleccionado = spinnerEstado.getSelectedItem().toString();
                User clienteSeleccionado = clientes.get(spinnerClientes.getSelectedItemPosition());
                int idCliente = clienteSeleccionado.getId();

                Caso caso = new Caso(0, nombreCaso, descripcionCaso, estadoSeleccionado, idCliente);
                User idUsuarioLogueado = databaseHelper.obtenerIdUsuarioLogueado();

                long casoId = databaseHelper.crearCaso(caso, idUsuarioLogueado.getId());

                if (casoId != -1) {
                    casoList.clear();
                    casoList.addAll(databaseHelper.listarCasos());
                    casoAdapter.notifyDataSetChanged();
                    Toast.makeText(requireContext(), "Caso creado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Error al crear el caso", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void mostrarDialogoOpciones(Caso caso) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Opciones de caso")
                .setItems(new CharSequence[]{"Actualizar caso", "Eliminar caso"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which) {
                            case 0:
                                mostrarDialogoActualizarCaso(caso);
                                break;
                            case 1:
                                eliminarCaso(caso);
                                break;
                        }
                    }
                })
                .show();
    }


    private void mostrarDialogoActualizarCaso(Caso caso) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_actualizar_caso, null);

        EditText etNombreCaso = dialogView.findViewById(R.id.etNombreCaso);
        EditText etDescripcion = dialogView.findViewById(R.id.etDescripcion);
        Spinner spinnerEstado = dialogView.findViewById(R.id.spinnerEstado);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.estados, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);

        Spinner spinnerClientes = dialogView.findViewById(R.id.spinner_clientes_update);

        List<User> clientes = databaseHelper.obtenerClientes(3);
        List<String> nombresClientes = new ArrayList<>();
        for (User cliente : clientes) {
            nombresClientes.add(cliente.getNombre());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, nombresClientes);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClientes.setAdapter(spinnerAdapter);

        etNombreCaso.setText(caso.getNombreCaso());
        etDescripcion.setText(caso.getDescripcionBreve());

        builder.setView(dialogView)
                .setTitle("Actualizar Caso")
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String nuevoNombreCaso = etNombreCaso.getText().toString();
                        String nuevaDescripcion = etDescripcion.getText().toString();
                        String estadoSeleccionado = spinnerEstado.getSelectedItem().toString();
                        User clienteSeleccionado = clientes.get(spinnerClientes.getSelectedItemPosition());
                        int idCliente = clienteSeleccionado.getId();

                        caso.setNombreCaso(nuevoNombreCaso);
                        caso.setDescripcionBreve(nuevaDescripcion);
                        caso.setEstado(estadoSeleccionado);
                        caso.setIdCliente(idCliente);
                        databaseHelper.actualizarCaso(caso);
                        casoList.clear();
                        casoList.addAll(databaseHelper.listarCasos());
                        casoAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarCaso(Caso caso) {
        databaseHelper.eliminarCaso(caso.getIdCaso());
        casoList.remove(caso);
        casoAdapter.notifyDataSetChanged();
        Toast.makeText(requireContext(), "Caso eliminado correctamente.", Toast.LENGTH_SHORT).show();

    }

}
