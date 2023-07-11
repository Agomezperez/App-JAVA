package com.example.legisunicor.ui.reporte;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.legisunicor.databinding.FragmentCargasReportesBinding;
import com.example.legisunicor.db.DatabaseHelper;
import com.example.legisunicor.db.models.Reporte;

import java.util.ArrayList;
import java.util.List;

public class CargasReportesFragment extends DialogFragment implements  ReporteAdapter.OnItemClickListener {
    private FragmentCargasReportesBinding binding;
    private List<Reporte> reporteList;
    private ReporteAdapter reporteAdapter;

    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCargasReportesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reporteList = new ArrayList<>();
        reporteAdapter = new ReporteAdapter(reporteList);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(reporteAdapter);

        databaseHelper = new DatabaseHelper(requireContext());
        reporteAdapter.setOnItemClickListener(this);

        List<Reporte> reportes = databaseHelper.obtenerReportes();

        if (reportes.isEmpty()) {
            Toast.makeText(requireContext(), "No hay reportes disponibles", Toast.LENGTH_SHORT).show();
        } else {
            reporteList.clear();
            reporteList.addAll(reportes);
            reporteAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onItemClick(int position) {
        List<Reporte> reportes = databaseHelper.obtenerReportes();
        Reporte reporte = reportes.get(position);
        if (position >= 0 && position < reporteList.size()) {
            Log.d("EliminarReporte", "ID del reporte a eliminar: " + reporte.getId());

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Eliminar Reporte");
            builder.setMessage("¿Estás seguro de que deseas eliminar este reporte?");

            builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int rowsDeleted = databaseHelper.eliminarReportes(reporte.getId());
                    if (rowsDeleted > 0) {
                        reportes.remove(reporte);
                        reporteAdapter.notifyDataSetChanged();
                        Toast.makeText(requireContext(), "Reporte eliminado", Toast.LENGTH_SHORT).show();
                        CargasReportesFragment.this.dismiss();
                    } else {
                        Toast.makeText(requireContext(), "Error al eliminar el reporte", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("Cancelar", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Log.e("onItemClick", "Posición inválida: " + position);
        }

    }


}
