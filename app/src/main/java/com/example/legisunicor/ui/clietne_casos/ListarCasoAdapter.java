package com.example.legisunicor.ui.clietne_casos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legisunicor.R;
import com.example.legisunicor.db.models.Caso;

import java.util.List;

public class ListarCasoAdapter extends RecyclerView.Adapter<ListarCasoAdapter.CasoViewHolder> {
    private List<Caso> casosList;

    public ListarCasoAdapter(List<Caso> casosList) {
        this.casosList = casosList;
    }

    @NonNull
    @Override
    public CasoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_caso, parent, false);
        return new CasoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CasoViewHolder holder, int position) {
        Caso caso = casosList.get(position);
        holder.bind(caso);
    }

    @Override
    public int getItemCount() {
        return casosList.size();
    }

    public class CasoViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNombreCaso;
        private TextView textViewEstadoCaso;

        public CasoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombreCaso = itemView.findViewById(R.id.tvNombreCaso);
            textViewEstadoCaso = itemView.findViewById(R.id.tvEstado);
        }

        public void bind(Caso caso) {
            textViewNombreCaso.setText(caso.getNombreCaso());
            textViewEstadoCaso.setText(caso.getEstado());
        }
    }
}
