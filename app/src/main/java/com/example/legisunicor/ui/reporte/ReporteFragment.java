package com.example.legisunicor.ui.reporte;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.legisunicor.databinding.FragmentReporteBinding;
import com.example.legisunicor.db.DatabaseHelper;
import com.example.legisunicor.db.models.Reporte;

import java.util.ArrayList;
import java.util.List;


public class ReporteFragment extends Fragment implements ReporteAdapter.OnItemClickListener, ReporteAsyncTask.FetchDataListener  {
    private List<Reporte> reporteList;

    private DatabaseHelper databaseHelper;
    private ReporteAdapter registroAdapter;

    private ReporteAsyncTask reporteAsyncTask;

    private FragmentReporteBinding binding;

    public ReporteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReporteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reporteList = new ArrayList<>();
        registroAdapter = new ReporteAdapter(reporteList);

        reporteAsyncTask = new ReporteAsyncTask(requireContext(), registroAdapter, ReporteFragment.this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(registroAdapter);

        registroAdapter.setOnItemClickListener(this);

        databaseHelper = new DatabaseHelper(requireContext());

        binding.consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reporteAsyncTask != null) {
                    reporteAsyncTask.cancel(true);
                }

                reporteAsyncTask = new ReporteAsyncTask(requireContext(), registroAdapter, ReporteFragment.this);
                reporteAsyncTask.execute();
            }
        });


        registerForContextMenu(binding.recyclerView);
    }

    @Override
    public void onDataFetched(List<Reporte> itemList) {
        if (itemList != null) {
            reporteList = itemList;
            registroAdapter = new ReporteAdapter(reporteList);
            binding.recyclerView.setAdapter(registroAdapter);
            registroAdapter.setOnItemClickListener(this);
        } else {
            Toast.makeText(requireContext(), "Error al obtener datos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(int position) {
        if (position >= 0 && position < reporteList.size()) {
            Reporte selectedRecord = reporteList.get(position);
            showOptionsDialog(selectedRecord);
        } else {
            Log.e("onItemClick", "Posición inválida: " + position);
        }
    }

    private void showOptionsDialog(Reporte selectedRecord) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Opciones");
        builder.setItems(new CharSequence[]{"Visualizar datos", "Insertar", "Ver reportes insertados"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        visualizarReportes(selectedRecord);
                        break;
                    case 1:
                        insertar(selectedRecord);
                        break;
                    case 2:
                        verReportesRegistrados();
                        break;
                }
            }
        });
        builder.show();
    }

    private void verReportesRegistrados() {
        CargasReportesFragment cargarReportes = new CargasReportesFragment();
        cargarReportes.show(getChildFragmentManager(), "fragment_reportes_view");
    }

    private void visualizarReportes(Reporte selectedRecord) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Detalles del Reporte");

        StringBuilder sb = new StringBuilder();
        sb.append("Periodo: ").append(selectedRecord.getPeriodo()).append("\n")
                .append("Descripción: ").append(selectedRecord.getDescripcion()).append("\n")
                .append("Enero: ").append(selectedRecord.getEnero()).append("\n")
                .append("Febrero: ").append(selectedRecord.getFebrero()).append("\n")
                .append("Marzo: ").append(selectedRecord.getMarzo()).append("\n")
                .append("Abril: ").append(selectedRecord.getAbril()).append("\n")
                .append("Mayo: ").append(selectedRecord.getMayo()).append("\n")
                .append("Junio: ").append(selectedRecord.getJunio()).append("\n")
                .append("Julio: ").append(selectedRecord.getJulio()).append("\n")
                .append("Agosto: ").append(selectedRecord.getAgosto()).append("\n")
                .append("Septiembre: ").append(selectedRecord.getSeptiembre()).append("\n")
                .append("Octubre: ").append(selectedRecord.getOctubre()).append("\n")
                .append("Noviembre: ").append(selectedRecord.getNoviembre()).append("\n")
                .append("Diciembre: ").append(selectedRecord.getDiciembre());

        builder.setMessage(sb.toString());
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void insertar(Reporte reporte) {
        boolean success = databaseHelper.insertReporte(reporte);
        if (success) {
            Toast.makeText(requireContext(), "Reporte insertado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Error al insertar el reporte", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (reporteAsyncTask != null) {
            reporteAsyncTask.cancel(true);
        }
    }


}