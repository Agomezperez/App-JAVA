package com.example.legisunicor.ui.abogado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legisunicor.R;
import com.example.legisunicor.db.models.Caso;
import com.example.legisunicor.db.models.User;

import java.util.List;

//CasoAbogadoAdapter
public class CasoAbogadoAdapter extends RecyclerView.Adapter<CasoAbogadoAdapter.ClienteViewHolder> {

    private Context context;
    private List<User> listaClientes;
    private OnItemClickListener listener;

    public CasoAbogadoAdapter(Context context, List<User> listaClientes) {
        this.context = context;
        this.listaClientes = listaClientes;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cliente_abogado, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        User cliente = listaClientes.get(position);
        holder.bind(cliente);
    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
    }

    public class ClienteViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewNombreCliente;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombreCliente = itemView.findViewById(R.id.textViewNombreCliente);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

        public void bind(User cliente) {
            textViewNombreCliente.setText(cliente.getNombre());
        }
    }
}
