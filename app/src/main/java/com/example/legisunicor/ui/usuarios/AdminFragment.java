package com.example.legisunicor.ui.usuarios;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.legisunicor.R;
import com.example.legisunicor.databinding.FragmentAdminBinding;
import com.example.legisunicor.db.DatabaseHelper;
import com.example.legisunicor.db.models.User;

import java.util.List;

public class AdminFragment extends Fragment {
    private FragmentAdminBinding binding;
    private DatabaseHelper databaseHelper;
    private List<User> usuarios;
    private UserAdapter usuarioAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseHelper = new DatabaseHelper(requireContext());

        usuarios = databaseHelper.listarUsuarios();
        usuarioAdapter = new UserAdapter(requireContext(), usuarios);
        binding.listViewUsers.setAdapter(usuarioAdapter);

        binding.btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User idUsuarioLogueado = databaseHelper.obtenerIdUsuarioLogueado();
                showCreateUserDialog();
            }
        });

        binding.listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                User usuario = usuarios.get(position);
                showDeleteUserDialog(usuario);

                return true;
            }
        });

        binding.listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User usuario = usuarios.get(position);
                showUpdateUserDialog(usuario);
            }
        });
    }

    private void showCreateUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Crear Usuario");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_user, null);
        final EditText etUsername = dialogView.findViewById(R.id.etUsername);
        final EditText etPassword = dialogView.findViewById(R.id.etPassword);
        final EditText etNombre = dialogView.findViewById(R.id.etNombre);
        final EditText etApellidos = dialogView.findViewById(R.id.etApellidos);
        final Spinner spinnerRol = dialogView.findViewById(R.id.spinnerRol);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.roles_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRol.setAdapter(adapter);

        builder.setView(dialogView);

        builder.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String nombre = etNombre.getText().toString();
                String apellidos = etApellidos.getText().toString();
                int rol = spinnerRol.getSelectedItemPosition();

                User usuario = new User(0, username, password, nombre, apellidos, rol, 0);
                long id = databaseHelper.crearUsuario(usuario);

                if (id != -1) {
                    usuarios.clear();
                    usuarios.addAll(databaseHelper.listarUsuarios());
                    usuarioAdapter.notifyDataSetChanged();

                    Toast.makeText(requireContext(), "Usuario creado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Error al crear el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);

        builder.show();
    }

    private void showDeleteUserDialog(final User usuario) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Eliminar Usuario");
        builder.setMessage("¿Estás seguro de que quieres eliminar este usuario?");

        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int rowsAffected = databaseHelper.eliminarUsuario(usuario.getId());

                if (rowsAffected > 0) {
                    usuarios.remove(usuario);
                    usuarioAdapter.notifyDataSetChanged();

                    Toast.makeText(requireContext(), "Usuario eliminado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "No se puede eliminar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);

        builder.show();
    }

    private void showUpdateUserDialog(final User usuario) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Actualizar Usuario");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_update_user, null);
        final EditText etUsername = dialogView.findViewById(R.id.etUsername);
        final EditText etPassword = dialogView.findViewById(R.id.etPassword);
        final EditText etNombre = dialogView.findViewById(R.id.etNombre);
        final EditText etApellidos = dialogView.findViewById(R.id.etApellidos);
        final Spinner spinnerRol = dialogView.findViewById(R.id.spinnerRolesUpdate);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.roles_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRol.setAdapter(adapter);

        etUsername.setText(usuario.getUsername());
        etPassword.setText(usuario.getPassword());
        etNombre.setText(usuario.getNombre());
        etApellidos.setText(usuario.getApellidos());

        builder.setView(dialogView);

        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String nombre = etNombre.getText().toString();
                String apellidos = etApellidos.getText().toString();
                int rol = (int) spinnerRol.getSelectedItemId();

                usuario.setUsername(username);
                usuario.setPassword(password);
                usuario.setNombre(nombre);
                usuario.setApellidos(apellidos);
                usuario.setIdRol(rol);

                int rowsAffected = databaseHelper.actualizarUsuario(usuario);

                if (rowsAffected > 0) {
                    usuarioAdapter.notifyDataSetChanged();

                    Toast.makeText(requireContext(), "Usuario actualizado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Error al actualizar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);

        builder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
