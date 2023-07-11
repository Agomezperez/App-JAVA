package com.example.legisunicor.ui.abogado;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legisunicor.R;
import com.example.legisunicor.db.models.Caso;

import java.util.List;

public class CasoAdapter extends RecyclerView.Adapter<CasoAdapter.CasoViewHolder> {
    private List<Caso> casos;

    private OnItemClickListener onItemClickListener;

    public CasoAdapter(List<Caso> casos, OnItemClickListener onItemClickListener) {
        this.casos = casos;
        this.onItemClickListener = onItemClickListener;

    }

    @NonNull
    @Override
    public CasoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_caso, parent, false);
        return new CasoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CasoViewHolder holder, int position) {
        Caso caso = casos.get(position);

        holder.tvNombre.setText(caso.getNombreCaso());
        holder.tvDescripcion.setText(caso.getDescripcionBreve());
        holder.tvEstado.setText(caso.getEstado());
        holder.tvIdCliente.setText(String.valueOf(caso.getIdCliente()));
    }

    @Override
    public int getItemCount() {
        return casos.size();
    }

    public class CasoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDescripcion, tvEstado, tvIdCliente;

        public CasoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_nombre);
            tvDescripcion = itemView.findViewById(R.id.tv_descripcion);
            tvEstado = itemView.findViewById(R.id.tv_estado);
            tvIdCliente = itemView.findViewById(R.id.tv_id_cliente);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Caso caso = casos.get(position);
                        onItemClickListener.onItemClick(caso);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Caso caso);
    }
}
