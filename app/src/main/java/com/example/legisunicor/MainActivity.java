package com.example.legisunicor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.legisunicor.db.DatabaseHelper;
import com.example.legisunicor.db.models.User;
import com.example.legisunicor.ui.abogado.AbogadoFragment;
import com.example.legisunicor.ui.abogado.ClientesAbogadoFragment;
import com.example.legisunicor.ui.clietne_casos.ListarCasosClienteFragment;
import com.example.legisunicor.ui.fichero.CargarRolesDesdeArchivoFragment;
import com.example.legisunicor.ui.login.LoginActivity;
import com.example.legisunicor.ui.reporte.ReporteFragment;
import com.example.legisunicor.ui.usuarios.AdminFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav = (NavigationView) findViewById(R.id.navmenu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        databaseHelper = new DatabaseHelper(MainActivity.this);
        User idUsuario = databaseHelper.obtenerIdUsuarioLogueado();

        if (savedInstanceState == null) {
            if (idUsuario.getIdRol() == 1) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AdminFragment()).commit();
                nav.setCheckedItem(R.id.clientes);
            } else if (idUsuario.getIdRol() == 2) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AbogadoFragment()).commit();
                nav.setCheckedItem(R.id.nav_casos);
            } else if (idUsuario.getIdRol() == 3) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListarCasosClienteFragment()).commit();
                nav.setCheckedItem(R.id.nav_mis_casos);
            }
        }

        databaseHelper = new DatabaseHelper(MainActivity.this);
        NavigationView navigationView = findViewById(R.id.navmenu);

        Menu navMenu = navigationView.getMenu();
        MenuItem usuarios = navMenu.findItem(R.id.clientes);
        MenuItem casos = navMenu.findItem(R.id.nav_casos);
        MenuItem misCasos = navMenu.findItem(R.id.nav_mis_casos);
        MenuItem misClientes = navMenu.findItem(R.id.nav_mis_cliente);
        MenuItem roles = navMenu.findItem(R.id.nav_roles);
        MenuItem reportes = navMenu.findItem(R.id.nav_reporte);

        if (idUsuario.getIdRol() == 1) {
            usuarios.setVisible(true);
            casos.setVisible(true);
            misCasos.setVisible(true);
            misClientes.setVisible(true);
            roles.setVisible(true);
            reportes.setVisible(true);
        } else {
            usuarios.setVisible(false);
            roles.setVisible(false);
        }

        if (idUsuario.getIdRol() == 2 || idUsuario.getIdRol() == 1) {
            casos.setVisible(true);
            misClientes.setVisible(true);
            reportes.setVisible(true);
        } else {
            casos.setVisible(false);
            misClientes.setVisible(false);
            reportes.setVisible(false);
        }

        if (idUsuario.getIdRol() == 3 || idUsuario.getIdRol() == 1) {
            misCasos.setVisible(true);
        } else {
            misCasos.setVisible(false);
        }

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                switch (menuItem.getItemId()) {
                    case R.id.clientes:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AdminFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_roles:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CargarRolesDesdeArchivoFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_casos:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AbogadoFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_mis_cliente:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ClientesAbogadoFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_reporte:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReporteFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_mis_casos:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListarCasosClienteFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_logout:
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        databaseHelper.cerrarSesion();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }

                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}