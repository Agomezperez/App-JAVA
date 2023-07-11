package com.example.legisunicor.ui.abogado;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.legisunicor.R;
import com.example.legisunicor.databinding.FragmentClientesAbogadoBinding;
import com.example.legisunicor.db.DatabaseHelper;
import com.example.legisunicor.db.models.Caso;
import com.example.legisunicor.db.models.User;

import java.util.ArrayList;
import java.util.List;


public class ClientesAbogadoFragment extends Fragment implements CasoAbogadoAdapter.OnItemClickListener {
    private FragmentClientesAbogadoBinding binding;
    private DatabaseHelper databaseHelper;
    private List<User> listaClientes;
    private CasoAbogadoAdapter clienteAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClientesAbogadoBinding.inflate(inflater, container, false);
       return binding.getRoot();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseHelper = new DatabaseHelper(requireContext());

        listaClientes = new ArrayList<>();
        clienteAdapter = new CasoAbogadoAdapter(requireActivity(), listaClientes);

        clienteAdapter.setOnItemClickListener(this);
        binding.recyclerViewCasosCliente.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewCasosCliente.setAdapter(clienteAdapter);

        User idAbogado = databaseHelper.obtenerIdUsuarioLogueado();
        cargarClientesAbogado(idAbogado.getId());
    }

    @Override
    public void onItemClick(int position) {
        User cliente = listaClientes.get(position);
        int idCliente = cliente.getId();

        mostrarCasosAsociados(idCliente);
    }

    private void mostrarCasosAsociados(int idCliente) {
        List<Caso> listaCasos = databaseHelper.obtenerCasosPorCliente(idCliente);

        StringBuilder casosTexto = new StringBuilder();
        for (Caso caso : listaCasos) {
            casosTexto.append("ID del Caso: ").append(caso.getIdCaso()).append("\n");
            casosTexto.append("Nombre del Caso: ").append(caso.getNombreCaso()).append("\n");
            casosTexto.append("Estado del Caso: ").append(caso.getEstado()).append("\n\n");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Casos del Cliente")
                .setMessage(casosTexto.toString())
                .setPositiveButton("Aceptar", null)
                .show();
    }


    private void cargarClientesAbogado(int idAbogado) {
        listaClientes.clear();
        listaClientes.addAll(databaseHelper.obtenerClientesPorAbogado(idAbogado));
        clienteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}