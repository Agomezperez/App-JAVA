package com.example.legisunicor.ui.usuarios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.legisunicor.R;
import com.example.legisunicor.db.models.User;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    private LayoutInflater inflater;

    public UserAdapter(Context context, List<User> userList) {
        super(context, 0, userList);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_user, parent, false);

            holder = new ViewHolder();
            holder.tvUsername = convertView.findViewById(R.id.tvUsername);
            holder.tvNombre = convertView.findViewById(R.id.tvNombre);
            holder.tvApellidos = convertView.findViewById(R.id.tvApellidos);
            holder.tvIdRol = convertView.findViewById(R.id.tvIdRol);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User user = getItem(position);

        if (user != null) {
            holder.tvUsername.setText(user.getUsername());
            holder.tvNombre.setText(user.getNombre());
            holder.tvApellidos.setText(user.getApellidos());
            holder.tvIdRol.setText(String.valueOf(user.getIdRol()));

            switch (user.getIdRol()) {
                case 1:
                    holder.tvIdRol.setText("Administrador");
                    break;
                case 2:
                    holder.tvIdRol.setText("Abogado");
                    break;
                case 3:
                    holder.tvIdRol.setText("Cliente");;
                    break;
                default:
                    holder.tvIdRol.setText(0);;
                    break;
            }
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tvUsername;
        TextView tvNombre;
        TextView tvApellidos;
        TextView tvIdRol;
    }
}
