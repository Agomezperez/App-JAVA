package com.example.legisunicor.ui.clietne_casos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legisunicor.databinding.FragmentListarCasosClienteBinding;
import com.example.legisunicor.db.DatabaseHelper;
import com.example.legisunicor.db.models.Caso;
import com.example.legisunicor.db.models.User;

import java.util.ArrayList;
import java.util.List;

public class ListarCasosClienteFragment extends Fragment {

    private FragmentListarCasosClienteBinding binding;
    private RecyclerView recyclerView;
    private ListarCasoAdapter casoAdapter;
    private List<Caso> listaCasos;

    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListarCasosClienteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseHelper = new DatabaseHelper(requireContext());
        recyclerView = binding.recyclerViewListCasos;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        listaCasos = new ArrayList<>();
        casoAdapter = new ListarCasoAdapter(listaCasos);
        recyclerView.setAdapter(casoAdapter);

        User idUsuarioLogueado = databaseHelper.obtenerIdUsuarioLogueado();

        if (idUsuarioLogueado.getId() != -1) {
            cargarCasosCliente(idUsuarioLogueado);
        } else {
            Toast.makeText(requireContext(), "No se encontró un usuario logueado", Toast.LENGTH_SHORT).show();
        }
    }


    private void cargarCasosCliente(User user) {
        List<Caso> casos = databaseHelper.obtenerCasosPorCliente(user.getId());

        listaCasos.clear();
        listaCasos.addAll(casos);
        casoAdapter.notifyDataSetChanged();
    }
}
